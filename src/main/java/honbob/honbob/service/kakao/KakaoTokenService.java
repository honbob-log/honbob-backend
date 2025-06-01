package honbob.honbob.service.kakao;

import honbob.honbob.domain.Member;
import honbob.honbob.dto.auth.kakao.KakaoTokenDto;
import honbob.honbob.dto.auth.kakao.KakaoUserInfoDto;
import honbob.honbob.dto.auth.kakao.UserLoginResponse;
import honbob.honbob.global.exception.BusinessException;
import honbob.honbob.global.exception.ExceptionType;
import honbob.honbob.global.jwt.JwtUtil;
import honbob.honbob.repository.MemberRepository;
import honbob.honbob.service.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoTokenService {

    private final WebClient webClient;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    /**
     * 카카오 토큰 요청 (인가 코드를 이용)
     */
    private KakaoTokenDto requestKakaoToken(String code) {
        log.info("카카오 토큰 요청 - 인가 코드: {}", code);

        try {
            KakaoTokenDto tokenResponse = webClient.post()
                    .uri("https://kauth.kakao.com/oauth/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                            .with("client_id", clientId)
                            .with("redirect_uri", redirectUri)
                            .with("code", code))
                    .retrieve()
                    .bodyToMono(KakaoTokenDto.class)
                    .block();

            log.info("카카오 토큰 요청 성공");
            return tokenResponse;
        } catch (WebClientResponseException e) {
            log.error("카카오 토큰 요청 실패 - 상태 코드: {}, 응답: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new BusinessException(ExceptionType.KAKAO_TOKEN_REQUEST_FAILED);
        } catch (Exception e) {
            log.error("카카오 토큰 요청 중 예외 발생", e);
            throw new BusinessException(ExceptionType.KAKAO_TOKEN_REQUEST_FAILED);
        }
    }


    /**
     * 카카오 사용자 정보 요청 (액세스 토큰을 이용)
     */
    private KakaoUserInfoDto requestKakaoUserInfo(String accessToken) {
        log.info("카카오 사용자 정보 요청");

        KakaoUserInfoDto userInfoDto = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoUserInfoDto.class)
                .block();

        log.info("카카오 사용자 정보 요청 성공 - ID: {}, 닉네임: {}", userInfoDto.getId(), userInfoDto.getNickname());
        return userInfoDto;
    }

    /**
     * 리프레시 토큰을 쿠키에 저장
     */
    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 30); // 30일
        response.addCookie(refreshTokenCookie);
        log.info("리프레시 토큰 쿠키 설정 완료");
    }

    /**
     * 카카오 로그인 프로세스 처리 (HttpServletResponse 없이 토큰만 처리)
     * 인가 코드로 토큰을 받고, 사용자 정보를 조회하여 로그인/회원가입 처리
     */
    @Transactional
    public UserLoginResponse processKakaoLoginWithCode(String code) {
        try {
            // 1. 인가 코드로 카카오 토큰 요청
            KakaoTokenDto kakaoTokenDto = requestKakaoToken(code);

            // 2. 액세스 토큰으로 사용자 정보 요청
            KakaoUserInfoDto userInfoDto = requestKakaoUserInfo(kakaoTokenDto.getAccessToken());

            Optional<Member> maybeMember = memberRepository.findByAuthId(userInfoDto.getId());
            boolean isMember = maybeMember.isPresent(); // true: 기존 회원, false: 신규 회원

            Member member = maybeMember.orElseGet(() -> {
                // 신규 회원일 때만 이 람다식이 실행됨
                Member newMember = new Member();
                newMember.setAuthId(userInfoDto.getId());
                newMember.setNickname(userInfoDto.getNickname());
                newMember.setProfileImage(userInfoDto.getProfileImageUrl());
                newMember.setEmail(userInfoDto.getEmail());
                return memberRepository.save(newMember);
            });

            // 4. JWT 토큰 생성
            String jwtToken = createJwtToken(member);
            log.info("jwtToken: {}", jwtToken);
            // 5. 로그인 응답 생성 (클라이언트 응답용)
            return UserLoginResponse.withRefreshToken(
                    "로그인 성공",
                    jwtToken,
                    kakaoTokenDto.getRefreshToken(),
                    userInfoDto.getNickname(),
                    userInfoDto.getProfileImageUrl(),
                    isMember
            );

        } catch (WebClientResponseException e) {
            log.error("카카오 API 응답 오류: 상태 코드={}, 응답={}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new BusinessException(ExceptionType.KAKAO_TOKEN_ERROR);
        } catch (Exception e) {
            log.error("카카오 로그인 처리 중 오류 발생", e);
            throw new BusinessException(ExceptionType.KAKAO_TOKEN_ERROR);
        }
    }

    /**
     * 카카오 토큰 갱신 (리프레시 토큰 사용)
     */
    private KakaoTokenDto refreshKakaoToken(String refreshToken) {
        log.info("카카오 토큰 갱신 요청");

        Map<String, String> formData = new HashMap<>();
        formData.put("grant_type", "refresh_token");
        formData.put("client_id", clientId);
        formData.put("refresh_token", refreshToken);

        KakaoTokenDto tokenDto = webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(KakaoTokenDto.class)
                .block();

        log.info("카카오 토큰 갱신 성공");
        return tokenDto;
    }

    /**
     * 액세스 토큰 갱신 (기존 메서드 - 쿠키 설정 포함)
     */
    @Transactional
    public UserLoginResponse refreshKakaoToken(String refreshToken, HttpServletResponse response) {
        try {
            // 1. 리프레시 토큰으로 회원 조회
            Member member = memberRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new BusinessException(ExceptionType.INVALID_TOKEN));

            // 2. 카카오 토큰 갱신 요청
            KakaoTokenDto newTokenDto = refreshKakaoToken(refreshToken);

            // 3. 회원 정보에 새 리프레시 토큰 저장
            member.setRefreshToken(newTokenDto.getRefreshToken());
            memberRepository.save(member);

            // 4. JWT 토큰 생성
            String jwtToken = createJwtToken(member);

            // 5. 리프레시 토큰을 쿠키에 저장
            addRefreshTokenCookie(response, newTokenDto.getRefreshToken());

            // 6. 로그인 응답 생성
            return UserLoginResponse.withRefreshToken(
                    "토큰 갱신 성공",
                    jwtToken,
                    newTokenDto.getRefreshToken(),
                    member.getNickname(),
                    member.getProfileImage(),
                    false
            );

        } catch (WebClientResponseException e) {
            log.error("카카오 API 응답 오류: 상태 코드={}, 응답={}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new BusinessException(ExceptionType.KAKAO_TOKEN_ERROR);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("토큰 갱신 중 오류 발생", e);
            throw new BusinessException(ExceptionType.KAKAO_TOKEN_ERROR);
        }
    }

    // JWT 토큰 생성
    public String createJwtToken(Member member) {
        return jwtUtil.generateToken(member.getId());
    }
}
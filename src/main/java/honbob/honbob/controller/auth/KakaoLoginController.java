package honbob.honbob.controller.auth;

import honbob.honbob.dto.auth.kakao.UserLoginResponse;
import honbob.honbob.global.exception.BusinessException;
import honbob.honbob.global.exception.ExceptionType;
import honbob.honbob.global.response.ResponseBody;
import honbob.honbob.global.response.ResponseUtil;
import honbob.honbob.service.kakao.KakaoTokenService;
import honbob.honbob.service.kakao.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoLoginController {
    private final LoginService loginService;
    private final KakaoTokenService kakaoTokenService;

    /**
     * 카카오 로그인 페이지로 리다이렉트
     */
    @GetMapping("/api/auth/kakao/login")
    public ResponseEntity<ResponseBody<Map<String, String>>> getKakaoLoginUrl() {
        log.info("카카오 로그인 URL 요청");
        String loginUrl = loginService.getKakaoLoginUrl();

        Map<String, String> response = new HashMap<>();
        response.put("loginUrl", loginUrl);

        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(response));
    }


    /**
     * 카카오 로그인 콜백 처리
     * 인가 코드로 카카오 토큰을 요청하고 사용자 정보를 가져옴
     */
    @GetMapping("/auth/login/kakao")
    public void kakaoLoginCallback(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) throws IOException {
        if (!StringUtils.hasText(code)) {
            throw new BusinessException(ExceptionType.KAKAO_CODE_MISSING);
        }
        // code로 카카오 토큰 및 유저 정보 처리
        UserLoginResponse loginResult = kakaoTokenService.processKakaoLoginWithCode(code);
        log.info("result={}",loginResult);
        // 앱 딥링크 URL 생성 (스킴은 앱에 맞게 수정)
        String deepLink = String.format(
                "exp://172.21.80.1:8081/?accessToken=%s&refreshToken=%s&nickname=%s&profileImageUrl=%s&isNewMember=%s",
                URLEncoder.encode(loginResult.getAccessToken(), StandardCharsets.UTF_8),
                URLEncoder.encode(loginResult.getRefreshToken(), StandardCharsets.UTF_8),
                URLEncoder.encode(loginResult.getNickname(), StandardCharsets.UTF_8),
                URLEncoder.encode(loginResult.getProfileImageUrl(), StandardCharsets.UTF_8),
                loginResult.isNewMember()
        );
        log.info("딥링크로 리다이렉트={}",deepLink);
        // 앱 딥링크로 리다이렉트
        response.sendRedirect(deepLink);
    }


    /**
     * 액세스 토큰 갱신 (리프레시 토큰 사용)
     */
    @PostMapping("/api/auth/refresh")
    public ResponseEntity<ResponseBody<UserLoginResponse>> refreshToken(
            @RequestParam("refreshToken") String refreshToken,
            HttpServletResponse response) {

        log.info("토큰 갱신 요청");

        if (!StringUtils.hasText(refreshToken)) {
            log.error("리프레시 토큰이 비어있습니다");
            throw new BusinessException(ExceptionType.INVALID_TOKEN);
        }
        UserLoginResponse refreshResult = kakaoTokenService.refreshKakaoToken(refreshToken, response);
        log.info("토큰 갱신 성공 - 사용자: {}", refreshResult.getNickname());
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse(refreshResult));
    }
}
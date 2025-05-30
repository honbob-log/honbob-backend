package honbob.honbob.service.member;

import honbob.honbob.domain.Member;
import honbob.honbob.global.exception.BusinessException;
import honbob.honbob.global.exception.ExceptionType;
import honbob.honbob.global.jwt.JwtUtil;
import honbob.honbob.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Member processKakaoMember(Long kakaoId, String nickname, String email, String profileImageUrl, String refreshToken) {
        // 기존 카카오 ID로 회원 조회
        return memberRepository.findByKakaoId(kakaoId)
                .map(existingMember -> {
                    // 기존 회원이면 정보 업데이트
                    existingMember.setName(nickname);
                    existingMember.setProfileImage(profileImageUrl);
                    existingMember.setRefreshToken(refreshToken);
                    log.info("기존 회원 정보 업데이트: {}", existingMember.getName());
                    return existingMember;
                })
                .orElseGet(() -> {
                    // 이메일로 회원 조회 (이메일 중복 확인)
                    return memberRepository.findByEmail(email)
                            .map(existingMember -> {
                                // 이메일은 있지만 카카오 연동이 안된 회원인 경우
                                existingMember.setKakaoId(kakaoId);
                                existingMember.setName(nickname);
                                existingMember.setProfileImage(profileImageUrl);
                                existingMember.setRefreshToken(refreshToken);
                                log.info("기존 이메일 회원에 카카오 연동: {}", existingMember.getName());
                                return existingMember;
                            })
                            .orElseGet(() -> {
                                // 신규 회원인 경우
                                Member newMember = Member.createKakaoMember(
                                        kakaoId, nickname, email, profileImageUrl, refreshToken);
                                log.info("신규 회원 등록: {}", newMember.getName());
                                return memberRepository.save(newMember);
                            });
                });
    }

    // JWT 토큰 생성
    public String createJwtToken(Member member) {
        return jwtUtil.generateToken(member.getId());
    }

    // 회원 ID로 회원 조회
    @Transactional(readOnly = true)
    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ExceptionType.MEMBER_NOT_FOUND));
    }
}
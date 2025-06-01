package honbob.honbob.service.member;

import honbob.honbob.domain.Member;

import honbob.honbob.dto.UserProfileRequest;
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

    /*
    유저 닉네임, 프로필 이미지, 유저 설명 설정
     */
    @Transactional
    public void setMemberInfo(UserProfileRequest userProfileRequest, Long memberId){
        // 멤버의 정보를 가져온다
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new BusinessException(ExceptionType.MEMBER_NOT_FOUND));
        // 멤버의 닉네임, 프로필 이미지, 설명을 설정한다.
        member.setMemberInfo(userProfileRequest);
    }

}
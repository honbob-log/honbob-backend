package honbob.honbob.controller;

import honbob.honbob.dto.UserProfileRequest;
import honbob.honbob.global.response.ResponseBody;
import honbob.honbob.global.response.ResponseUtil;
import honbob.honbob.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GlobalLoginController {

    private final MemberService memberService;

    @PostMapping("/api/auth/login/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseBody<String>> setProfile(@Valid @RequestBody UserProfileRequest userProfileRequest, @AuthenticationPrincipal Long memberId){
        memberService.setMemberInfo(userProfileRequest, memberId);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse("유저 정보 설정 완료"));
    }
}

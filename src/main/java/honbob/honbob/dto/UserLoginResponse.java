package honbob.honbob.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값은 JSON으로 변환하지 않음
public class UserLoginResponse {
    private String message;
    private String accessToken;  // JWT 토큰
    private String refreshToken; // 카카오 리프레시 토큰 (선택적)
    private String nickname;
    private String profileImageUrl;
    private boolean isNewMember;

    // 기존 생성자 호환을 위한 팩토리 메서드
    public static UserLoginResponse of(String message, String accessToken, String nickname,
                                       String profileImageUrl, boolean isNewMember) {
        return UserLoginResponse.builder()
                .message(message)
                .accessToken(accessToken)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .isNewMember(isNewMember)
                .build();
    }

    // 리프레시 토큰 포함 버전
    public static UserLoginResponse withRefreshToken(String message, String accessToken,
                                                     String refreshToken, String nickname,
                                                     String profileImageUrl, boolean isNewMember) {
        return UserLoginResponse.builder()
                .message(message)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .isNewMember(isNewMember)
                .build();
    }
}
package honbob.honbob.dto.auth.kakao;

import lombok.Getter;

@Getter
public class UserProfileRequest {
    private String nickname;
    private String profileImageUrl;
    private String description;
}

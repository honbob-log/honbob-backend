package honbob.honbob.dto;

import lombok.Getter;

@Getter
public class UserProfileRequest {
    private String nickname;
    private String profileImageUrl;
    private String description;
}

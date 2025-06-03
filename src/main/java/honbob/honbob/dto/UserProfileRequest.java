package honbob.honbob.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserProfileRequest {
    @NotBlank(message = "닉네임은 필수입니다")
    @Size(min = 1, max = 10, message = "닉네임은 1-10자 사이여야 합니다")
    private String nickname;

    private String profileImageUrl;

    @Size(max = 500, message = "자기소개는 500자를 초과할 수 없습니다")
    private String description;
}

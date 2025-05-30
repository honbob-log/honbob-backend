package honbob.honbob.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class KakaoUserInfoDto {

    private Long id;
    private String nickname;
    private String email;
    private String profileImageUrl;

    @JsonProperty("kakao_account")
    private void unpackKakaoAccount(Map<String, Object> kakaoAccount) {
        if (kakaoAccount == null) return;

        // 이메일 추출
        this.email = (String) kakaoAccount.get("email");

        // 프로필 정보 추출
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        if (profile != null) {
            this.nickname = (String) profile.get("nickname");
            this.profileImageUrl = (String) profile.get("profile_image_url");
        }
    }

    @JsonProperty("properties")
    private void unpackProperties(Map<String, Object> properties) {
        if (properties == null) return;

        // 기본 정보가 없을 경우를 대비해 properties에서도 추출
        if (this.nickname == null) {
            this.nickname = (String) properties.get("nickname");
        }

        if (this.profileImageUrl == null) {
            this.profileImageUrl = (String) properties.get("profile_image");
        }
    }
}
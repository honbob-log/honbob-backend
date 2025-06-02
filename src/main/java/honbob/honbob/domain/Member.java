package honbob.honbob.domain;

import honbob.honbob.dto.auth.kakao.UserProfileRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
//TODO : OAuth 별 데이터 다른데 어떻게 통합할지 생각
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String email;

    @Column
    private String password;

    private String nickname;

    private String profileImage;

    private String description;

    // 카카오 관련 필드 추가
    @Column(unique = true)
    private Long authId;

    @Column
    private String refreshToken;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Recipe> recipeList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Favorite> favoriteList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Review> reviewList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<UserIngredient> userIngredientList;

    public void setMemberInfo(UserProfileRequest userProfileRequest){
        this.profileImage = userProfileRequest.getProfileImageUrl();
        this.description = userProfileRequest.getDescription();
        this.nickname = userProfileRequest.getNickname();
    }
}
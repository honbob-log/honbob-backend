package honbob.honbob.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;


    private String email;

    @Column
    private String password;


    private String profileImage;

    // 카카오 관련 필드 추가
    @Column(unique = true)
    private Long kakaoId;

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

    // 카카오 회원 생성 메서드
    public static Member createKakaoMember(Long kakaoId, String name, String email, String profileImage, String refreshToken) {
        Member member = new Member();
        member.setKakaoId(kakaoId);
        member.setName(name);
        member.setEmail(email);
        member.setProfileImage(profileImage);
        member.setRefreshToken(refreshToken);
        member.setPassword(""); // 소셜 로그인은 비밀번호가 없음
        return member;
    }
}
package honbob.honbob.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Recipe {

    @Id @GeneratedValue
    @Column(name = "recipe_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer cookTime;

    @Column(nullable = false)
    private String difficulty;

    @Column(nullable = false)
    private Integer viewCount = 0;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @Column(nullable = false)
    private Integer favoriteCount = 0;

    @Column(nullable = false)
    private Integer reviewCount = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 빠져있던 양방향 관계들 추가
    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> recipeIngredientList = new ArrayList<>();

    @OneToMany(mappedBy = "recipe")
    private List<RecipeStep> recipeStepList = new ArrayList<>();
    
    @OneToMany(mappedBy = "recipe")
    private List<RecipeTag> recipeTagList = new ArrayList<>();
    
    @OneToMany(mappedBy = "recipe")
    private List<RecipeImage> recipeImageList = new ArrayList<>();
    
    @OneToMany(mappedBy = "recipe")
    private List<Favorite> favoriteList = new ArrayList<>();
    
    @OneToMany(mappedBy = "recipe")
    private List<Review> reviewList = new ArrayList<>();
}
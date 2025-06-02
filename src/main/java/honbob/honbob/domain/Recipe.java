package honbob.honbob.domain;

import honbob.honbob.dto.recipe.Difficulty;
import jakarta.persistence.*;
import lombok.Builder;
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

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private Integer amount;

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

    @Builder
    public Recipe(String title, String description, Integer cookTime, Difficulty difficulty, Member member, Integer amount) {
        this.title = title;
        this.description = description;
        this.cookTime = cookTime;
        this.difficulty = difficulty;
        this.member = member;
        this.amount = amount;
    }
    public void addRecipeIngredient(RecipeIngredient recipeIngredient) {
        // 1) 자식 객체(recipeIngredient)에 대한 recipe 설정
        recipeIngredient.assignRecipe(this);
        // 2) 이 Recipe 엔티티의 리스트에도 추가
        this.recipeIngredientList.add(recipeIngredient);
    }

    public void addRecipeStep(RecipeStep recipeStep) {
        recipeStep.assignRecipe(this);
        this.recipeStepList.add(recipeStep);
    }

    public void addRecipeImage(RecipeImage recipeImage) {
        recipeImage.assignRecipe(this);
        this.recipeImageList.add(recipeImage);
    }

}
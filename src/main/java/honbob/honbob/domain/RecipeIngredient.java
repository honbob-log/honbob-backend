package honbob.honbob.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class RecipeIngredient {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "ing_id", nullable = false)
    private Ingredient ingredient;

    @Column(nullable = false)
    private Integer amount;

    @Builder
    public RecipeIngredient(Ingredient ingredient, Integer amount){
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public void assignRecipe(Recipe recipe){
        this.recipe = recipe;
    }
}

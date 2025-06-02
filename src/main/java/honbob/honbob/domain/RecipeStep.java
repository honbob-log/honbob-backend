package honbob.honbob.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RecipeStep {

    @Id @GeneratedValue
    @Column(name = "recipe_step_id")
    private Long id;

    private Integer stepOrder;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Builder
    public RecipeStep(Integer stepOrder, String description, String imageUrl) {
        this.stepOrder = stepOrder;
        this.description = description;
        this.imageUrl = imageUrl;
    }
    public void assignRecipe(Recipe recipe){
        this.recipe = recipe;
    }
}

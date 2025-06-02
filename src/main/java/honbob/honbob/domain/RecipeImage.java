package honbob.honbob.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RecipeImage {

    @Id @GeneratedValue
    @Column(name = "recipe_image_id")
    private Long id;
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Builder
    public RecipeImage(String url){
        this.url = url;
    }
    public void assignRecipe(Recipe recipe){
        this.recipe = recipe;
    }
}

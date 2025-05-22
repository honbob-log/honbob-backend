package honbob.honbob.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Ingredient {

    @Id @GeneratedValue
    @Column(name = "ingredient_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "ingredient")
    private List<UserIngredient> userIngredientList;

    @OneToMany(mappedBy = "ingredient")
    private List<RecipeIngredient> recipeIngredientList;
}

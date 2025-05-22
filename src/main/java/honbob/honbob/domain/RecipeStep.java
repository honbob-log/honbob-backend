package honbob.honbob.domain;

import jakarta.persistence.*;
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
}

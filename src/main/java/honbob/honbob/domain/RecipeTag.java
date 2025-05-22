package honbob.honbob.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RecipeTag {

    @Id @GeneratedValue
    @Column(name = "recipe_tag_id")
    private Long id;

    private String tagName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}

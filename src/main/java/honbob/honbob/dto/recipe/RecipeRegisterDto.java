package honbob.honbob.dto.recipe;

import honbob.honbob.domain.RecipeIngredient;
import honbob.honbob.domain.RecipeStep;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.util.List;

@Getter
public class RecipeRegisterDto {

    private String title;
    private String description;
    private String imageUrl;
    private String tag;
    private Integer cookTime;

    // TODO: enum 고려
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    private Integer amount;

    // TODO : 재료 여러개인데 어떻게 받을지
    //
    private List<IngredientDto> ingredient;
    // TODO : 단계별로 설명, 이미지가 들어가야 하는데 어떻게 받을지
    private List<StepDto> step;

    @Getter
    public static class IngredientDto {
        private Long ingredientId;
        private String name;
        private Integer amount;
    }

    @Getter
    public static class StepDto {
        private Integer stepOrder; // 단계 순서
        private String description; // 설명
        private String imageUrl; // 이미지 URL
    }
}

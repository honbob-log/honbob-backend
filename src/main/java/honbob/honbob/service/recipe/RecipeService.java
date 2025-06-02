package honbob.honbob.service.recipe;

import honbob.honbob.domain.*;
import honbob.honbob.dto.recipe.RecipeRegisterDto;
import honbob.honbob.global.exception.BusinessException;
import honbob.honbob.global.exception.ExceptionType;
import honbob.honbob.repository.MemberRepository;
import honbob.honbob.repository.recipe.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final MemberRepository memberRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final RecipeImageRepository recipeImageRepository;

    /*
    레시피 등록
     */
    @Transactional
    public void recipeRegister(RecipeRegisterDto recipeRegisterDto, Long memberId){

        // 유저를 찾아야함
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new BusinessException(ExceptionType.MEMBER_NOT_FOUND));
        // 레시피 생성해서 등록
        Recipe recipe = Recipe.builder()
                .title(recipeRegisterDto.getTitle())
                .description(recipeRegisterDto.getDescription())
                .cookTime(recipeRegisterDto.getCookTime())
                .difficulty(recipeRegisterDto.getDifficulty())
                .member(member)
                .amount(recipeRegisterDto.getAmount())
                .build();

        // 레시미 이미지 등록
        if(recipeRegisterDto.getImageUrl() != null){
            RecipeImage recipeImage = RecipeImage.builder()
                    .url(recipeRegisterDto.getImageUrl())
                    .build();
            recipe.addRecipeImage(recipeImage);
            recipeImageRepository.save(recipeImage);
        }

        // 레시피 재료 등록
        for (RecipeRegisterDto.IngredientDto ingredientDto : recipeRegisterDto.getIngredient()){
            Ingredient ingredient = ingredientRepository.findById(ingredientDto.getIngredientId())
                    .orElseGet(()->{
                        Ingredient newIngredient = Ingredient.builder()
                                .name(ingredientDto.getName())
                                .build();
                        return ingredientRepository.save(newIngredient);
                    });
            RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                    .ingredient(ingredient)
                    .amount(ingredientDto.getAmount())
                    .build();
            recipe.addRecipeIngredient(recipeIngredient);
            recipeIngredientRepository.save(recipeIngredient);
        }
        // 레시피 조리법 등록
        for (RecipeRegisterDto.StepDto stepDto : recipeRegisterDto.getStep()){
            RecipeStep recipeStep = RecipeStep.builder()
                    .stepOrder(stepDto.getStepOrder())
                    .description(stepDto.getDescription())
                    .imageUrl(stepDto.getImageUrl())
                    .build();
            recipe.addRecipeStep(recipeStep);
            recipeStepRepository.save(recipeStep);
        }
        recipeRepository.save(recipe);

    }
}

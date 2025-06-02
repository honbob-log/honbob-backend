package honbob.honbob.controller.recipe;

import honbob.honbob.domain.Recipe;
import honbob.honbob.dto.recipe.RecipeRegisterDto;
import honbob.honbob.global.response.ResponseBody;
import honbob.honbob.global.response.ResponseUtil;
import honbob.honbob.service.recipe.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    /*
    레시피 등록
     */

    @PostMapping("/api/recipe/register")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseBody<String>> registerRecipe(RecipeRegisterDto recipeRegisterDto, @AuthenticationPrincipal Long memberId) {
        recipeService.recipeRegister(recipeRegisterDto, memberId);
        return ResponseEntity.ok(ResponseUtil.createSuccessResponse("레시피 생성 성공"));
    }
    /*
    메인페이지에서 레시피 조회 하는거 던져줘야함
    인기 레시피, 추천레시피
     */

}

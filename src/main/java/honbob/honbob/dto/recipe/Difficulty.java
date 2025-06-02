package honbob.honbob.dto.recipe;

import lombok.Getter;

@Getter
public enum Difficulty {
    VERY_EASY("매우 쉬움"),
    EASY("쉬움"), 
    NORMAL("보통"),
    HARD("어려움"), 
    VERY_HARD("매우 어려움");
    
    private final String difficulty; // 불변이므로 final
    
    Difficulty(String difficulty) {
        this.difficulty = difficulty;
    }

}
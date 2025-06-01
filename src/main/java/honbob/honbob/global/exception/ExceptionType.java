package honbob.honbob.global.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ExceptionType {

    // Common
    UNEXPECTED_SERVER_ERROR(INTERNAL_SERVER_ERROR,"C001","예상치 못한 에러 발생"),
    BINDING_ERROR(BAD_REQUEST,"C002","바인딩시 에러 발생"),
    ESSENTIAL_FIELD_MISSING_ERROR(NO_CONTENT , "C003","필수적인 필드 부재"),

    // Auth
    KAKAO_TOKEN_ERROR(UNAUTHORIZED,"A001","카카오 토큰 에러"),
    INVALID_TOKEN(UNAUTHORIZED,"A002","유효하지 않은 토큰"),
    TOKEN_EXPIRED(UNAUTHORIZED,"A003","만료된 토큰"),

    // Member
    MEMBER_NOT_FOUND(NOT_FOUND,"M001","회원을 찾을 수 없습니다"),
    // ExceptionType.java에 추가
    KAKAO_CODE_MISSING(BAD_REQUEST, "A004", "카카오 인증 코드 누락"),
    KAKAO_USER_INFO_ERROR(BAD_REQUEST, "A005", "카카오 사용자 정보 조회 실패"),
    KAKAO_TOKEN_REQUEST_FAILED(BAD_REQUEST,"A006","오류");
    private final HttpStatus status;
    private final String code;
    private final String message;
}
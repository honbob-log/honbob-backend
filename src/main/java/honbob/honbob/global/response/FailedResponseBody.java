package honbob.honbob.global.response;

import com.fasterxml.jackson.annotation.JsonTypeName;
import honbob.honbob.global.response.ResponseBody;
import lombok.Getter;
@Getter
@JsonTypeName("false")
public final class FailedResponseBody extends ResponseBody<Void> {

    private final String msg;

    public FailedResponseBody(String code, String msg){
        this.setCode(code);
        this.msg=msg;
    }

}
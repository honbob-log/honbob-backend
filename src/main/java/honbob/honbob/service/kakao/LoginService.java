package honbob.honbob.service.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    /**
     * 카카오 로그인 URL 생성
     * @return 카카오 로그인 페이지 URL
     */
    public String getKakaoLoginUrl() {
        log.debug("카카오 로그인 URL 생성 - clientId: {}, redirectUri: {}", clientId, redirectUri);

        String url = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";

        log.info("카카오 로그인 URL 생성 완료: {}", url);
        return url;
    }
}
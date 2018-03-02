package com.citrix.scenario.student;

import com.citrix.core.Request;
import com.citrix.data.Context;
import com.citrix.data.Input;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.Base64;

public class Login extends Request {
    private static final String CONTENT_TYPE = "text/plain; charset=UTF-8";
    private static final String RELATIVE_URL = "/login";
    private static final String USER_AGENT = "WebClient";

    public Login(String baseUrl) {
        super(baseUrl);
    }

    @Override
    protected ClientResponse execute(Context context, Input input) {
        Mono<ClientResponse> mono = webClient.get()
                .uri(RELATIVE_URL)
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((input.getUserName() + ":" + input.getPassword()).getBytes(Charset.forName("US-ASCII"))))
                .exchange()
                .onErrorResume(e -> Mono.just(get500ClientResponse()));

        return mono.block();
    }

    @Override
    protected void postProcess(Context context, Input input, ClientResponse clientResponse) {
        if (clientResponse != null && clientResponse.headers() != null) {
            String cookieStr = clientResponse.headers().header("Set-Cookie").get(0);
            if (cookieStr != null) {
                String sessionToken = cookieStr.split(";")[0];
                if (sessionToken != null) {
                    String[] tokens = sessionToken.split("=");
                    context.setCookieKey(tokens[0]);
                    context.setCookieValue(tokens[1]);
                }
            }
        }
    }

    @Override
    protected WebClient createWebClient(String baseUrl) {
        return WebClient.builder()
                .clientConnector(connector)
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .build();
    }

    @Override
    protected String getRelativeUrl() {
        return RELATIVE_URL;
    }
}

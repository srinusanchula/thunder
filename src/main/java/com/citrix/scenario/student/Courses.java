package com.citrix.scenario.student;

import com.citrix.core.Request;
import com.citrix.data.Context;
import com.citrix.data.Input;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

public class Courses extends Request {
    private static final String CONTENT_TYPE = "text/plain; charset=UTF-8";
    private static final String RELATIVE_URL = "/courses";
    private static final String USER_AGENT = "WebClient";

    public Courses(String baseUrl) {
        super(baseUrl);
    }

    @Override
    protected ClientResponse execute(Context context, Input input) {
        Mono<ClientResponse> mono = webClient.get()
                .uri(RELATIVE_URL)
                .cookie(context.getCookieKey(), context.getCookieValue())
                .exchange()
                .onErrorResume(e -> Mono.just(get500ClientResponse()));

        return mono.block();
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
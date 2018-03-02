package com.citrix.scenario.demo;

import com.citrix.core.Request;
import com.citrix.data.Context;
import com.citrix.data.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class DeviceRequest extends Request {
    private static final String CONTENT_TYPE = "application/json; charset=UTF-8";
    private static final String USER_AGENT = "WebClient";
    private static final Logger logger = LoggerFactory.getLogger(DeviceRequest.class);
    private final String RELATIVE_URL = "/device?deviceId={deviceId}";

    public DeviceRequest(String baseUrl) {
        super(baseUrl);
    }

    @Override
    protected ClientResponse execute(Context context, Input input) {
        Mono<ClientResponse> mono = webClient.get()
                .uri(RELATIVE_URL, input.getDeviceId())
                .exchange()
                .onErrorResume(e -> Mono.just(get500ClientResponse()));

        return mono.block();
    }

    @Override
    protected WebClient createWebClient(String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
//                .filter(logRequest())
                .build();
    }

    @Override
    protected String getRelativeUrl() {
        return RELATIVE_URL;
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> logger.info("{}={}", name, value)));
            return next.exchange(clientRequest);
        };
    }
}

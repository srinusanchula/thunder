package com.citrix.core;

import com.citrix.data.Context;
import com.citrix.data.Input;
import com.citrix.metrics.EndpointCounter;
import com.citrix.metrics.EndpointRegistry;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class Request {
    protected static ReactorClientHttpConnector connector = new ReactorClientHttpConnector(
            options -> options.option(
                    ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                    .compression(true)
                    .afterNettyContextInit(ctx ->
                    {
                        ctx.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                    }));
    protected final WebClient webClient;
    private final EndpointCounter endpointCounter;

    protected Request(final String baseUrl) {
        this.webClient = createWebClient(baseUrl);

        this.endpointCounter = EndpointRegistry.registerEndpoint(getRelativeUrl());
    }

    public final void fire(Context context, Input input) {
        endpointCounter.incrementInvocationCount();

        long t1 = System.currentTimeMillis();
        ClientResponse clientResponse = execute(context, input);
        long delta = System.currentTimeMillis() - t1;

        endpointCounter.addToRTT(delta);
        endpointCounter.incrementResponseCount();
        endpointCounter.incrementResponseCode(clientResponse.statusCode().value());

        postProcess(context, input, clientResponse);
    }

    protected abstract String getRelativeUrl();

    protected abstract ClientResponse execute(Context context, Input input);

    protected void postProcess(Context context, Input input, ClientResponse clientResponse) {
        //Do Nothing
    }

    protected abstract WebClient createWebClient(final String baseUrl);

    public final void warmup(Context context, Input input) {
        execute(context, input);
    }

    protected ClientResponse get500ClientResponse() {
        return new ClientResponse() {
            @Override
            public HttpStatus statusCode() {
                return HttpStatus.GATEWAY_TIMEOUT;
            }

            @Override
            public Headers headers() {
                return null;
            }

            @Override
            public MultiValueMap<String, ResponseCookie> cookies() {
                return null;
            }

            @Override
            public <T> T body(BodyExtractor<T, ? super ClientHttpResponse> bodyExtractor) {
                return null;
            }

            @Override
            public <T> Mono<T> bodyToMono(Class<? extends T> aClass) {
                return null;
            }

            @Override
            public <T> Mono<T> bodyToMono(ParameterizedTypeReference<T> parameterizedTypeReference) {
                return null;
            }

            @Override
            public <T> Flux<T> bodyToFlux(Class<? extends T> aClass) {
                return null;
            }

            @Override
            public <T> Flux<T> bodyToFlux(ParameterizedTypeReference<T> parameterizedTypeReference) {
                return null;
            }

            @Override
            public <T> Mono<ResponseEntity<T>> toEntity(Class<T> aClass) {
                return null;
            }

            @Override
            public <T> Mono<ResponseEntity<T>> toEntity(ParameterizedTypeReference<T> parameterizedTypeReference) {
                return null;
            }

            @Override
            public <T> Mono<ResponseEntity<List<T>>> toEntityList(Class<T> aClass) {
                return null;
            }

            @Override
            public <T> Mono<ResponseEntity<List<T>>> toEntityList(ParameterizedTypeReference<T> parameterizedTypeReference) {
                return null;
            }
        };
    }
}

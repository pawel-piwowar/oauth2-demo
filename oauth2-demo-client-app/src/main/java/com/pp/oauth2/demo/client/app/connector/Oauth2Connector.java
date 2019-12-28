package com.pp.oauth2.demo.client.app.connector;

import com.pp.oauth2.demo.client.app.model.Oauth2Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Service
public class Oauth2Connector {

    public Mono<Oauth2Token> getToken(String code)  {

        final String  redirectUri = "http://localhost:8081/api/oauth2/account";

        WebClient webClient = WebClient
                .builder()
                .baseUrl("http://localhost:8080/oauth")
                .filter(logRequest())
                .build();

        Mono<Oauth2Token> response = webClient
                .post()
                .uri( uriBuilder -> uriBuilder
                        .path("/token")
                        .queryParam("code", code)
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("redirect_uri", redirectUri)
                        .build())
                .header("content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header("Authorization", "Basic " + Base64Utils
                        .encodeToString(("demo-client-app" + ":" + "123456").getBytes(UTF_8)))
                 .retrieve()
                 .onStatus(HttpStatus::is4xxClientError, resp -> {
                    log.error("Error: " + resp);
                    return Mono.error(new RuntimeException("4xx"));
                })
                 .bodyToMono(Oauth2Token.class);

        return response;
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("Header: {}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }


}

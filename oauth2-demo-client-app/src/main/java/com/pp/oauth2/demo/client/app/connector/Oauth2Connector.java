package com.pp.oauth2.demo.client.app.connector;

import com.pp.oauth2.demo.client.app.model.Account;
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
        return  WebClient
                .builder()
                .baseUrl("http://localhost:8082/oauth")
                .filter(logRequest())
                .build()
                .post()
                .uri( uriBuilder -> uriBuilder
                        .path("/token")
                        .queryParam("code", code)
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("redirect_uri", "http://localhost:8081/api/oauth2/account")
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
    }

    public Mono<Account> getResource(Oauth2Token token)  {
        return WebClient
                .builder()
                .baseUrl("http://localhost:8080")
                .filter(logRequest())
                .filter(logResponse())
                .build()
                .get()
                .uri( uriBuilder -> uriBuilder
                        .path("/api/accounts/default")
                        .build())
                .header("Authorization", "Bearer " + token.getAccessToken())
                .exchange()
                .flatMap(resp -> resp.bodyToMono(Account.class));
    }

    private static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Response: \n");
                clientResponse
                        .headers()
                        .asHttpHeaders()
                        .forEach((name, values) -> values.forEach(value -> sb.append(name + ":" + values)));
                log.debug(sb.toString());
            }
            return Mono.just(clientResponse);
        });
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("Header: {}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }


}

package com.pp.oauth2.demo.client.app.connector;

import com.pp.oauth2.demo.client.app.model.Account;
import com.pp.oauth2.demo.client.app.model.Oauth2Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AccountsConnector {
    public Mono<Account> getAccount(Oauth2Token token)  {
        WebClient webClient = WebClient
                .builder()
                .baseUrl("http://localhost:8080")
                .build();

        Mono<Account> response = webClient
                .get()
                .uri( uriBuilder -> uriBuilder
                        .path("/api/accounts/default")
                        .build())
                .header("authorization", "Bearer " + token.getAccessToken())
                .exchange()
                .flatMap(resp -> resp.bodyToMono(Account.class));

        return response;
    }
}

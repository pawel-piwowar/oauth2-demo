package com.pp.oauth2.demo.client.app.controllers;

import com.pp.oauth2.demo.client.app.connector.Oauth2Connector;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@AllArgsConstructor
public class Oauth2Controller {

    private Oauth2Connector oauth2Connector;

    @GetMapping("/api/oauth2/resource-app-redirect")
    public Mono<Void> redirect(ServerHttpResponse response) {
        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8082/oauth/authorize")
                .queryParam("client_id", "demo-client-app")
                .queryParam("response_type", "code")
                .queryParam("scope", "read_account")
                .build()
                .toUri();
        response.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
        response.getHeaders().setLocation(uri);
        return response.setComplete();
    }

    @GetMapping("/api/oauth2/account")
    public Mono<ResponseEntity> getAccount(@RequestParam(required = false) String code,
                                           @RequestParam(required = false) String error,
                                           @RequestParam(name = "error_description", required = false) String errorDescription) {
        if (error != null) {
            return Mono.just(ResponseEntity.ok().body(error + " " + errorDescription));
        }
        return oauth2Connector.getToken(code)
                .flatMap(oauth2Token -> oauth2Connector.getResource(oauth2Token))
                .map(account -> ResponseEntity.ok().body(account));
    }


    @RequestMapping("/")
    public Mono<Void> indexRedirect(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
        response.getHeaders().setLocation(URI.create("/index.html"));
        return response.setComplete();
    }


}

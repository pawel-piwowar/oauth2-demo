package com.pp.auth2.demo.auth.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServer extends ResourceServerConfigurerAdapter
{

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setClientId("demo-client-app");
        tokenService.setClientSecret("123456");
        tokenService.setCheckTokenEndpointUrl("http://localhost:8082/oauth/check_token");

        resources
                .resourceId("oauth2-resource")
                .tokenServices(tokenService);
    }



    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                    .antMatchers("/api/accounts/default/**")
                    .access("hasRole('USER') and #oauth2.hasScope('read_account')");
    }
}
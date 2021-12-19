package com.msaid.gamelove.config;

import com.msaid.gamelove.security.CustomAuthFilter;
import com.msaid.gamelove.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@Log4j2
public class SecurityConfiguration {

    private final GameLoveProperties gameLoveProperties;
    private final JwtUtil tokenProvider;

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                ReactiveAuthenticationManager reactiveAuthenticationManager) {
        log.warn("Security enabled: {}", gameLoveProperties.isSecurityEnabled());
        if (gameLoveProperties.isSecurityEnabled()) {
            return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                    .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                    .authenticationManager(reactiveAuthenticationManager)
                    .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                    .authorizeExchange(it -> it
                            .pathMatchers("/auth/**").permitAll()
                            .pathMatchers("/v2/api-docs", "/configuration/ui",
                                    "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/swagger-ui/**", "/webjars/**").permitAll()
                            .anyExchange().hasAuthority("USER"))
                    //todo : note ADMIN roles can be added also
                    .addFilterAt(new CustomAuthFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                    .build();
        }
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                //.cors().disable()
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.anyExchange().permitAll()).build();


    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
                                                                       PasswordEncoder passwordEncoder) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }
}

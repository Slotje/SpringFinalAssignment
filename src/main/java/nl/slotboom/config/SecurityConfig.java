package nl.slotboom.config;

import lombok.RequiredArgsConstructor;
import nl.slotboom.auth.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static nl.slotboom.constants.APIConstants.*;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    // Injecting JwtAuthenticationFilter bean
    private final JwtAuthenticationFilter jwtAuthFilter;

    // Injecting AuthenticationProvider bean
    private final AuthenticationProvider authenticationProvider;

    // Injecting LogoutHandler bean
    private final LogoutHandler logoutHandler;

    // Defining a SecurityFilterChain bean to configure security for HttpRequests
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        RequestMatcher[] requestMatchers = {
                new AntPathRequestMatcher("/" + API + "/" + VERSION + "/" + AUTH_ENDPOINT + "/**")
        };

        // Configuring the HttpSecurity object to disable CSRF protection, authorize specific requests and require authentication for all other requests
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(requestMatchers)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Configuring logout behavior
                .logout()
                .logoutUrl("/user/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        return http.build();
    }
}


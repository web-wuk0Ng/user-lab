package com.example.userlab;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
@Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
@Bean SecurityFilterChain security(HttpSecurity http, JwtService jwt, AuthMapper mapper) throws Exception {
return http.cors(c -> {}).csrf(c -> c.disable())
.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
.authorizeHttpRequests(a -> a.requestMatchers("/api/auth/login", "/swagger-ui/**", "/v3/api-docs/**", "/error").permitAll().anyRequest().authenticated())
.exceptionHandling(e -> e.authenticationEntryPoint((req,res,ex) -> res.sendError(401)).accessDeniedHandler((req,res,ex) -> res.sendError(403)))
.addFilterBefore(new JwtFilter(jwt,mapper), UsernamePasswordAuthenticationFilter.class)
.build();
}
}

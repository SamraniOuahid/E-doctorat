package com.example.demo.security.config;

import com.example.demo.security.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        private final CustomOAuth2UserService customOAuth2UserService;
        private final com.example.demo.security.oauth2.OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
        private final com.example.demo.security.jwt.JwtAuthenticationFilter jwtAuthFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                .cors(org.springframework.security.config.Customizer.withDefaults()) // Active la config
                                                                                                     // CORS par défaut
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/public/**", "/error").permitAll()
                                                .requestMatchers("/api/auth/**").permitAll() // Authentification doit
                                                                                             // être publique
                                                                                             // (login/register)
                                                .requestMatchers("/api/professeurs/**").hasRole("PROFESSEUR")
                                                .requestMatchers("/api/directeur-labo/**").hasRole("DIRECTEUR_LABO")
                                                // ajoute d'autres rôles ici
                                                .anyRequest().authenticated())
                                .oauth2Login(oauth -> oauth
                                                .loginPage("/oauth2/authorization/google") // ou page front custom
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(customOAuth2UserService))
                                                .successHandler(oAuth2LoginSuccessHandler))
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(
                                                                new org.springframework.security.web.authentication.HttpStatusEntryPoint(
                                                                                org.springframework.http.HttpStatus.UNAUTHORIZED)));

                // PAS de formLogin() → uniquement Google
                http.addFilterBefore(jwtAuthFilter,
                                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
                org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
                configuration.setAllowedOrigins(java.util.List.of("http://localhost:5173", "http://localhost:9090"));
                configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(java.util.List.of("*"));
                configuration.setAllowCredentials(true);
                org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

}

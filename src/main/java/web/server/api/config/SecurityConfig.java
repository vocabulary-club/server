package web.server.api.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import web.server.api.jwt.JwtUtil;
import web.server.api.jwt.MyJwtFilter;
import web.server.api.jwt.MyLogoutFilter;
import web.server.api.jwt.MyLogoutHelper;
import web.server.api.oauth2.MyClientRegistrationRepository;
import web.server.api.oauth2.MyOAuth2AuthorizedClientService;
import web.server.api.oauth2.MySuccessHandler;
import web.server.api.service.MyOAuth2UserService;
import web.server.api.service.TokenService;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.url}")
    private String appUrl;

    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final MyLogoutHelper myLogoutHelper;

    private final MyOAuth2UserService myOAuth2UserService;
    private final MySuccessHandler mySuccessHandler;

    private final MyClientRegistrationRepository myClientRegistrationRepository;
    private final MyOAuth2AuthorizedClientService myOAuth2AuthorizedClientService;
    private final JdbcTemplate jdbcTemplate;

    public SecurityConfig(
            // token related
            JwtUtil jwtUtil,
            TokenService tokenService,
            MyLogoutHelper myLogoutHelper,
            // oauth2 related
            MyOAuth2UserService myOAuth2UserService,
            MySuccessHandler mySuccessHandler,
            // social client registration
            MyClientRegistrationRepository myClientRegistrationRepository,
            MyOAuth2AuthorizedClientService myOAuth2AuthorizedClientService,
            JdbcTemplate jdbcTemplate) {

        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
        this.myLogoutHelper = myLogoutHelper;

        this.myOAuth2UserService = myOAuth2UserService;
        this.mySuccessHandler = mySuccessHandler;

        this.myClientRegistrationRepository = myClientRegistrationRepository;
        this.myOAuth2AuthorizedClientService = myOAuth2AuthorizedClientService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList(appUrl));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(List.of("Set-Cookie", "Authorization"));

                        return configuration;
                    }
                }));

        http.csrf((auth) -> auth.disable());

        http.formLogin((auth) -> auth.disable());

        http.httpBasic((auth) -> auth.disable());

        http.oauth2Login((oauth2) -> oauth2
                .clientRegistrationRepository(myClientRegistrationRepository.clientRegistrationRepository())
                .authorizedClientService(myOAuth2AuthorizedClientService.oAuth2AuthorizedClientService(jdbcTemplate,
                        myClientRegistrationRepository.clientRegistrationRepository()))
                .userInfoEndpoint((config) -> config.userService(myOAuth2UserService))
                .successHandler(mySuccessHandler)
                // ADD THIS: Point the login page to something that doesn't exist
                // or your React login route to stop the auto-generation
                .loginPage(appUrl + "/login")
        );

        http.exceptionHandling(e -> e
                // ADD THIS: Return 401 instead of redirecting to the default login page
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        );

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login/**", "/oauth2/**").permitAll()
                .requestMatchers("/token/renew", "/privacy-policy", "/term-of-service").permitAll()
                .anyRequest().authenticated());

        http.addFilterAfter(new MyJwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(new MyLogoutFilter(myLogoutHelper), LogoutFilter.class);

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

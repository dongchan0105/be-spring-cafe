package codesquad.codestagram.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 모든 요청을 인증 없이 허용
                )
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (테스트 환경)
                .formLogin(login -> login.disable()) // 기본 로그인 페이지 비활성화
                .logout(logout -> logout.disable()); // 로그아웃 비활성화

        return http.build();
    }
}

package generator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
				.csrf().disable()
				.authorizeExchange()
				.pathMatchers("/user/public/**").permitAll() // 允许匿名访问
				.pathMatchers("/video/public/**").permitAll() // 允许匿名访问
				.anyExchange().authenticated() // 其他都需要认证
				.and()
				.oauth2Login(); // 启用SSO登录
		return http.build();
	}
}

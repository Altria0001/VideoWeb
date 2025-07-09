package generator.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

	private final KeycloakConfigResolver keycloakConfigResolver;

	@Autowired
	public SecurityConfig(KeycloakConfigResolver keycloakConfigResolver) {
		this.keycloakConfigResolver = keycloakConfigResolver;
	}

	/**
	 * 配置会话认证策略
	 */
	@Override
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
	}

	/**
	 * 配置安全策略
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		http
				.authorizeRequests()
				.antMatchers("/public/**").permitAll()  // 公开路径
				.antMatchers("/user/**").hasAnyRole("user", "admin")  // 用户和管理员可访问
				.antMatchers("/admin/**").hasRole("admin")  // 仅管理员可访问
				.anyRequest().authenticated();  // 其他路径需要认证
	}

	/**
	 * 配置Keycloak身份验证提供者
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(keycloakAuthenticationProvider());
	}
}
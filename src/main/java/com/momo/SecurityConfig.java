package com.momo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
//				.requestMatchers(new AntPathRequestMatcher("/**")).hasRole(null)
		.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
		
		
		
		
		
				.csrf((csrf) -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
				// 인증메일 발송을 위한 csrf 관련 제외 추가
				.csrf((csrf) -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/mail/**")))
				.headers((headers) -> headers.addHeaderWriter(
						new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
				.formLogin((formLogin) -> formLogin
						.usernameParameter("memberid")
						
						.loginPage("/member/login")
						.failureUrl("/member/loginfailed")
						.defaultSuccessUrl("/"))
				.logout((logout) -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
						.logoutSuccessUrl("/")
						.invalidateHttpSession(true));
		/*
//				.oauth2Login(Customizer.withDefaults()); 
		
		        // OAuth 2.0 로그인 방식 설정
		        http
		                .oauth2Login((auth) -> auth.loginPage("/oauth-login/login")
		                        .defaultSuccessUrl("/oauth-login")
		                        .failureUrl("/oauth-login/login")
		                        .permitAll());

		        http
		                .logout((auth) -> auth
		                        .logoutUrl("/oauth-login/logout"));

		        http
		                .csrf((auth) -> auth.disable());
		*/
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	/*
	@Bean
	AuthenticationFailureHandler customAuthFailureHandler(){
	        return new MomoAuthenticationFailureHandler();
	    }
	    */

}

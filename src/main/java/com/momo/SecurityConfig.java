package com.momo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer.UserInfoEndpointConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.momo.auth.OAuth2UserCustomService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	private final OAuth2UserCustomService oAuth2UserCustomService;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
//				.requestMatchers(new AntPathRequestMatcher("/**")).hasRole(null)
				.requestMatchers(new AntPathRequestMatcher("/admin")).hasAnyAuthority("ROLE_ADMIN")
				.requestMatchers(new AntPathRequestMatcher("/member/mypage/**")).hasAnyRole("ADMIN", "MEMBER", "SOCIAL")
				.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())




				// csrf 사이트 위변조 방지 설정이며, 개발 시에만 ignore 합니다
				.csrf((csrf) -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
				// 인증메일 발송을 위한 ignore
				.csrf((csrf) -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/mail/**")))
				.headers((headers) -> headers.addHeaderWriter(
						new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
				.formLogin((formLogin) -> formLogin
				.usernameParameter("memberid")
				.loginPage("/member/login")
				.failureUrl("/member/loginfailed")
				.defaultSuccessUrl("/member/mypage"))
				.logout((logout) -> logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true));

		//OAuth2 로그인 추가
		http.oauth2Login((oauth2) -> oauth2
				.userInfoEndpoint
				((userInfoEndpointConfig) -> userInfoEndpointConfig.userService(oAuth2UserCustomService)));

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/", "/oauth2/**", "/login/**").permitAll()
                .anyRequest().authenticated());
        

		/*
		 * 
		 *  	.oauth2Login
 				.loginPage(Customizer.withDefaults()); 
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

//	비밀번호 암호화
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

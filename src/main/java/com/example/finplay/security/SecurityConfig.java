package com.example.finplay.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserDetailsServiceImpl userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().disable()
			.csrf().disable()
			.authorizeRequests()
			.mvcMatchers("/test3").permitAll()
			.mvcMatchers("/index").permitAll()
			.mvcMatchers("/login").permitAll()
			.mvcMatchers("/signup").permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.formLogin()
			.loginPage("/login").permitAll()
			.failureUrl("/index")
			.defaultSuccessUrl("/profile", true)
//			.failureUrl("/login-error.html")
			.and()
			.logout()
			.logoutSuccessUrl("/index.html");
	}

	@Bean
	PasswordEncoder passwordEncoderBean() {
		return new BCryptPasswordEncoder(8, new SecureRandom());
	}
}

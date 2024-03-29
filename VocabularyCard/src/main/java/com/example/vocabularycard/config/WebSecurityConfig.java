package com.example.vocabularycard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.vocabularycard.service.LoginService;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	private final LoginService loginService;

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/css/**","/js/**","/images/**","/favicon.ico");
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests().antMatchers("/","/index","/login","/login/**","/regist","/regist/**").permitAll().antMatchers("/vocabularyCard/**").hasAnyRole("USER","OWNER").anyRequest().authenticated()
		.and().formLogin().loginPage("/login").usernameParameter("loginId").passwordParameter("password").loginProcessingUrl("/login/do").defaultSuccessUrl("/vocabularyCard").failureUrl("/login/failure").permitAll()
		.and().logout().permitAll();

		http.requiresChannel()
	      .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
	      .requiresSecure();
	}
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(loginService).passwordEncoder(passwordEncoder());
	}
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


}

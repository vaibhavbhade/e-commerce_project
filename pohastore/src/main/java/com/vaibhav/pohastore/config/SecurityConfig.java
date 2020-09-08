package com.vaibhav.pohastore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.vaibhav.pohastore.serviceimpl.UserSecurityService;
import com.vaibhav.pohastore.utility.SecurityUtility;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
@Autowired
private Environment env;
	
@Autowired
private UserSecurityService userSecurityService;

 public BCryptPasswordEncoder passwordEncoder() {
	 return SecurityUtility.passwordEncoder();
 }
 
 public static final String[] PUBLIC_MATCHERS= {
		 "/css/**",
		 "/js/**",
		 "/image/**",
		 "/",
		 "/newUser",
		 "/forgetPassword",
		 "/login",
		 "/fonts/**",
		 "/bookshelf",
		 "/bookDetail"
 };
	


@Override
	public void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
	
	
	http.csrf().disable().cors().disable().formLogin().failureUrl("/login?error").defaultSuccessUrl("/")
	.loginPage("/login").permitAll().and()
	.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/?logout").deleteCookies("remember-me").permitAll().and().rememberMe();
	http.authorizeRequests().antMatchers(PUBLIC_MATCHERS).permitAll().anyRequest().authenticated();
	}

@Autowired
private void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

	auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());

}

}

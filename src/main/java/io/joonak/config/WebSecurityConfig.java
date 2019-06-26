package io.joonak.config;

import io.joonak.account.handler.AuthEntryPoint;
import io.joonak.account.handler.AuthFailureHandler;
import io.joonak.account.handler.AuthSuccessHandler;
import io.joonak.account.service.LoadAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoadAccountService loadAccountService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint entryPoint() {
        return new AuthEntryPoint();
    }

    @Bean
    public AuthSuccessHandler authSuccessHandler() {
        return new AuthSuccessHandler();
    }

    @Bean
    public AuthFailureHandler authFailureHandler() {
        return new AuthFailureHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loadAccountService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                // static resources
                .antMatchers("/image/**", "/css/**", "/js/**")
                // swagger documents
                .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs", "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .antMatchers("/error").permitAll()
                    .antMatchers(HttpMethod.POST, "/account").anonymous()
                    .antMatchers("/sign-in").anonymous()
                    .antMatchers("/**").authenticated()
            .and()
                .exceptionHandling()
                    .authenticationEntryPoint(entryPoint())
            .and()
                .formLogin()
                    .loginProcessingUrl("/sign-in")
                    .usernameParameter("email")
                    .successHandler(authSuccessHandler())
                    .failureHandler(authFailureHandler())
            .and()
                .logout()
                    .logoutUrl("/sign-out");
    }
}
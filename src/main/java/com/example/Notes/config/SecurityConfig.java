package com.example.Notes.config;

import com.example.Notes.service.PersonDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;


@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    private final PersonDetailsService personDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService).passwordEncoder(getPasswordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
         http.csrf().disable()
                 .authorizeRequests()
                 .antMatchers("/admin/**").hasRole("ADMIN")
                 .antMatchers("/api/auth/registration/person", "/api/auth/login",
                         "/swagger-ui", "/api/auth/registration/admin").permitAll()
                 .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                 .anyRequest().authenticated()
                 .and()
//                 .httpBasic()
                 .formLogin().loginPage("/api/auth/login")
                 .loginProcessingUrl("/process_login")
                 .defaultSuccessUrl("/note/create", true)
                 .failureUrl("/api/auth/registration/person")
                 .and()
                 .logout().logoutUrl("/logout")
                 .logoutSuccessUrl("/api/auth/login")
                 .and()
//                 .logoutSuccessUrl("/api/auth/login")
//                 .invalidateHttpSession(true)
//                 .clearAuthentication(true)
//                 .and()
                 .sessionManagement()
                 .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .scopeSeparator(",")
                .additionalQueryStringParams(null)
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }



    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

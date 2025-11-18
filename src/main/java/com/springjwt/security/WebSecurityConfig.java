package com.springjwt.security;

import com.springjwt.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().and() // Configuration CORS ici
                .authorizeRequests()
                .antMatchers("/signin").permitAll()
                .antMatchers("/vendeurs/**").permitAll()
                .antMatchers("/admins/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/signup").permitAll()
                .antMatchers("/users/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/addUser/**").permitAll()
                .antMatchers("/deleteUser/**").permitAll()
                .antMatchers("/getUserById/**").permitAll()
                .antMatchers("/update/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/photo/{nom}/**").permitAll()
                .antMatchers("/getUserById/{userId}/**").permitAll()
                .antMatchers("/users/updatevendeurType/{userId}/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Autoriser ces origines
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Autoriser ces méthodes
        configuration.setAllowedHeaders(Arrays.asList("*")); // Autoriser tous les en-têtes
        configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Headers","Access-Control-Allow-Methods"));
        configuration.setAllowCredentials(true); // Autoriser les cookies
        configuration.setMaxAge(3600L); // Durée maximale de la requête préalable (preflight)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


}

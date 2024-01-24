package com.contect.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.contect.manager.services.UserDetailsServiceImpl;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    UserDetailsService getUserDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider AuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(this.getPasswordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    CustomAuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    // configure security that who can access links
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/**").permitAll()
                        .anyRequest()
                        .authenticated())
                // .formLogin(withDefaults()); // default login page

                /* to use custom login page */
                .formLogin(form -> form
                        .loginPage("/login") // custom login page
                        .permitAll()
                        // .failureUrl("/loginFailure") // if login fails then this url will be
                        // redirected
                        // .successHandler(getAuthenticationSuccessHandler()) // on successful login
                        // handle which url need to be redirected
                        .defaultSuccessUrl("/")
                // .and()
                // .logout()
                // .logoutSuccessUrl("/") // if logout then this url will be redirected
                );

        return httpSecurity.build();
    }
}

/*
 * requestMatchers("/admin/**").hasRole("ADMIN")
 * it measn that user that has role as ADMIN can only access url starts with
 * /admin
 * 
 * .requestMatchers("/**").permitAll()
 * .permitAll() is used to give permission to everyone. In our case url starts
 * with / will be accessible to everyone
 * 
 * At first we have checked for admin and user. then we checked for /** and this
 * is important
 * 
 * To use custom login page we have to configure
 * 
 * .faliureUrl("/loginFailure") // if login fails then this url will be
 * redirected
 * 
 * 
 * 
 * .successHandler(getAuthenticationSuccessHandler()) // on successful login
 * 
 * 
 * 
 */
package com.pedros.passinv2.infra.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Autowired
    private lateinit var securityFilter: SecurityFilter

    @Bean
    @Primary
    fun securityFilterChain(httpSecurity: HttpSecurity): DefaultSecurityFilterChain? {
        return httpSecurity.csrf(
            Customizer {
                crsfConfig -> crsfConfig.disable()
            }
        ).sessionManagement(Customizer {
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }).authorizeHttpRequests(Customizer {
            authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                .anyRequest().authenticated()
        })
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter::class.java)
        .build()
    }

    @Bean
    @Primary
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    @Primary
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
package com.sharingmap.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig
//    (
//    private val userDetailsService: UserDetailsService,
//)
{
//    private fun authManager(http: HttpSecurity): AuthenticationManager {
//        val authenticationManagerBuilder = http.getSharedObject(
//            AuthenticationManagerBuilder::class.java
//        )
//        authenticationManagerBuilder.userDetailsService(userDetailsService)
//        return authenticationManagerBuilder.build()
//    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable().invoke {
            authorizeHttpRequests {
                authorize("/cities", hasAuthority("ROLE_ADMIN"))
                authorize("/categories", hasAuthority("ROLE_ADMIN"))
                authorize("/subcategories", hasAuthority("ROLE_ADMIN"))
                authorize("/users", hasAuthority("ROLE_ADMIN"))

                //Документация. Потом убрать под админа
                authorize("/swagger-ui.html", permitAll)
                authorize("/swagger-ui/**", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                authorize("/v3/api-docs.yaml", permitAll)

                authorize("/items", permitAll)
                authorize("/signup/**", permitAll)
                authorize(anyRequest, authenticated)
            }
            formLogin { }
            logout { }
            //httpBasic { }
        }
        return http.build()
    }
}
package com.sharingmap.security.config

import com.sharingmap.security.jwt.AuthEntryPointJwt
import com.sharingmap.security.jwt.JwtTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity(debug = true)
class SecurityConfig(private val jwtTokenFilter: JwtTokenFilter,
                     private val bCryptPasswordEncoder: BCryptPasswordEncoder,
                     private val userDetailsService: UserDetailsService,
                     private val authEntryPointJwt: AuthEntryPointJwt)
{

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .addFilterAfter(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling().authenticationEntryPoint(authEntryPointJwt)
            .and().cors().and().csrf().disable()

            .invoke {
            authorizeHttpRequests {
                authorize("/cities", hasAuthority("ROLE_USER")) //для теста, поменять на админа потом
                authorize("/categories", hasAuthority("ROLE_ADMIN"))
                authorize("/subcategories", hasAuthority("ROLE_ADMIN"))
                authorize("/users", hasAuthority("ROLE_ADMIN"))

                //Документация. Потом убрать под админа
                authorize("/swagger-ui.html", permitAll)
                authorize("/swagger-ui/**", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                authorize("/v3/api-docs.yaml", permitAll)

                authorize("/current", permitAll)
                authorize("/login", permitAll)
                authorize("/items/**", permitAll)
                authorize("/signup/**", permitAll)
                authorize(anyRequest, authenticated)
            }
            //formLogin { }
            logout { }
            //httpBasic { }

        }
        return http.build()
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(bCryptPasswordEncoder)
        return authProvider
    }
}
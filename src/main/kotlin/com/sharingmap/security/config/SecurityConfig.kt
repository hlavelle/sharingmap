package com.sharingmap.security.config

import com.sharingmap.security.jwt.AuthEntryPointJwt
import com.sharingmap.security.jwt.JwtTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableMethodSecurity
@EnableWebSecurity(debug = false)
class SecurityConfig(private val jwtTokenFilter: JwtTokenFilter,
                     private val bCryptPasswordEncoder: BCryptPasswordEncoder,
                     private val userDetailsService: UserDetailsService,
                     private val authEntryPointJwt: AuthEntryPointJwt)
{
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterAfter(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling { exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(authEntryPointJwt)
            }
            .cors { cors -> cors.disable() }
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests {
                authorize ->
                authorize.requestMatchers("/send").permitAll()
                authorize.requestMatchers("/cities/{id}").permitAll()
                authorize.requestMatchers("/cities/all").permitAll()
                authorize.requestMatchers("/categories/{id}").permitAll()
                authorize.requestMatchers("/categories/all").permitAll()
                authorize.requestMatchers("/locations/{id}").permitAll()
                authorize.requestMatchers("/subcategories/{id}").permitAll()
                authorize.requestMatchers("/locations/{cityId}/all").permitAll()
                authorize.requestMatchers("/items/all").permitAll()
                authorize.requestMatchers("/subcategories/all").permitAll()
                authorize.requestMatchers("/swagger-ui.html").permitAll()
                authorize.requestMatchers("/items/{id}").permitAll()
                authorize.requestMatchers("/v3/api-docs/**").permitAll()
                authorize.requestMatchers("/swagger-ui/**").permitAll()
                authorize.requestMatchers("/login").permitAll()
                authorize.requestMatchers("/v3/api-docs.yaml").permitAll()
                authorize.requestMatchers("/logout").authenticated()
                authorize.requestMatchers("/is_auth").authenticated()
                authorize.requestMatchers("/resetPassword/**").permitAll()
                authorize.requestMatchers("/signup/**").permitAll()
                authorize.requestMatchers("{itemId}/image/urls").authenticated()
                authorize.requestMatchers("/refreshToken").permitAll()
                authorize.requestMatchers("/settings/{id}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("user/image/urls").authenticated()
                authorize.requestMatchers("/settings/create").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/settings/all").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/settings/update/{id}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/settings/delete/{id}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/cities/delete/{id}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/cities/update/{id}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/categories/update/{id}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/cities/create").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/categories/create").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/categories/delete/{id}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/subcategories/delete/{id}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/subcategories/update/{id}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/locations/create").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/subcategories/create").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/locations/update/{id}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/locations/delete/{id}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/users/{userId}/contacts").authenticated()
                authorize.requestMatchers("/users/{userId}/items").permitAll()
                authorize.requestMatchers("/users/update").authenticated()
                authorize.requestMatchers("/users/{id}").permitAll()
                authorize.requestMatchers("/users/myself").authenticated()
                authorize.requestMatchers("/users/delete").authenticated()
                authorize.requestMatchers("/admin/users/{id}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/admin/users/all").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/admin/users/update/{userId}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/admin/users/delete/{userId}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/items/update").authenticated()
                authorize.requestMatchers("/items/create").authenticated()
                authorize.requestMatchers("/admin/items/create/{userId}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/items/delete/{itemId}").authenticated()
                authorize.requestMatchers("/admin/items/update").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/admin/items/delete/{itemId}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/contacts/{id}").authenticated()
                authorize.requestMatchers("/contacts/myself").authenticated()
                authorize.requestMatchers("/contacts/update").authenticated()
                authorize.requestMatchers("/contacts/create").authenticated()
                authorize.requestMatchers("/admin/contacts/create/{userId}").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/contacts/delete/{contactId}").authenticated()
                authorize.requestMatchers("/admin/contacts/update").hasAuthority("ROLE_ADMIN")
                authorize.requestMatchers("/admin/contacts/delete/{contactId}").hasAuthority("ROLE_ADMIN")
                authorize.anyRequest().authenticated()
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

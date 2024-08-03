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
        val baseUrl: String = "lmcdkmcdvnkjadasckmvskmvdksdvv"

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .addFilterAfter(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling().authenticationEntryPoint(authEntryPointJwt)
            .and()
            .formLogin().permitAll()
            .loginProcessingUrl("/login1")
            .defaultSuccessUrl("/admin", true)
            .failureUrl("/errorPage")
            .and()
            .cors().and()
            .csrf().disable()

            .invoke {
            authorizeHttpRequests {
                authorize("/cities/{id}", permitAll)
                authorize("/cities/all", permitAll)

                authorize("/categories/{id}", permitAll)
                authorize("/categories/all", permitAll)

                authorize("/locations/{id}", permitAll)
                authorize("/locations/{cityId}/all", permitAll)

                authorize("/subcategories/{id}", permitAll)
                authorize("/subcategories/all", permitAll)

                authorize("/items/all", permitAll)
                authorize("/items/{id}", permitAll)

                authorize("/swagger-ui.html", permitAll)
                authorize("/swagger-ui/**", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                authorize("/v3/api-docs.yaml", permitAll)

                authorize("/login", permitAll)
                authorize("/is_auth", authenticated)
                authorize("/logout", authenticated)

                authorize("/signup/**", permitAll)
                authorize("/resetPassword/**", permitAll)
                authorize("/refreshToken", permitAll)

                authorize("{itemId}/image/urls", authenticated)
                authorize("user/image/urls", authenticated)

                authorize("/settings/{id}", hasAuthority("ROLE_ADMIN"))
                authorize("/settings/all", hasAuthority("ROLE_ADMIN"))
                authorize("/settings/create", hasAuthority("ROLE_ADMIN"))
                authorize("/settings/delete/{id}", hasAuthority("ROLE_ADMIN"))
                authorize("/settings/update/{id}", hasAuthority("ROLE_ADMIN"))

                authorize("/cities/update/{id}", hasAuthority("ROLE_ADMIN"))
                authorize("/cities/delete/{id}", hasAuthority("ROLE_ADMIN"))
                authorize("/cities/create", hasAuthority("ROLE_ADMIN"))

                authorize("/categories/update/{id}", hasAuthority("ROLE_ADMIN"))
                authorize("/categories/delete/{id}", hasAuthority("ROLE_ADMIN"))
                authorize("/categories/create", hasAuthority("ROLE_ADMIN"))

                authorize("/subcategories/update/{id}", hasAuthority("ROLE_ADMIN"))
                authorize("/subcategories/delete/{id}", hasAuthority("ROLE_ADMIN"))
                authorize("/subcategories/create", hasAuthority("ROLE_ADMIN"))


                authorize("/locations/create", hasAuthority("ROLE_ADMIN"))
                authorize("/locations/delete/{id}", hasAuthority("ROLE_ADMIN"))
                authorize("/locations/update/{id}", hasAuthority("ROLE_ADMIN"))

                authorize("/users/{userId}/items", permitAll)
                authorize("/users/{userId}/contacts", authenticated)

                authorize("/users/{id}", permitAll)
                authorize("/users/update", authenticated)
                authorize("/users/delete", authenticated)
                authorize("/users/myself", authenticated)
                authorize("/admin/users/all", hasAuthority("ROLE_ADMIN"))
                authorize("/admin/users/{id}", hasAuthority("ROLE_ADMIN"))
                authorize("/admin/users/delete/{userId}", hasAuthority("ROLE_ADMIN"))
                authorize("/admin/users/update/{userId}", hasAuthority("ROLE_ADMIN"))

                authorize("/items/create", authenticated)
                authorize("/items/update", authenticated)
                authorize("/items/delete/{itemId}", authenticated)
                authorize("/admin/items/create/{userId}", hasAuthority("ROLE_ADMIN"))
                authorize("/admin/items/delete/{itemId}", hasAuthority("ROLE_ADMIN"))
                authorize("/admin/items/update", hasAuthority("ROLE_ADMIN"))

                authorize("/contacts/myself", authenticated)
                authorize("/contacts/{id}", authenticated)
                authorize("/contacts/create", authenticated)
                authorize("/contacts/update", authenticated)
                authorize("/contacts/delete/{contactId}", authenticated)
                authorize("/admin/contacts/create/{userId}", hasAuthority("ROLE_ADMIN"))
                authorize("/admin/contacts/delete/{contactId}", hasAuthority("ROLE_ADMIN"))
                authorize("/admin/contacts/update", hasAuthority("ROLE_ADMIN"))
                authorize("/$baseUrl/**", permitAll)
                authorize("/**", permitAll)

                authorize(anyRequest, authenticated)

            }
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

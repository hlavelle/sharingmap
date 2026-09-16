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
import org.springframework.http.HttpMethod

/** Authorities are stored with the `ROLE_` prefix already, so this is `hasAuthority`, not `hasRole`. */
private const val ROLE_ADMIN = "ROLE_ADMIN"

/**
 * Reference data: readable by anyone, writable only by an admin. Listed once so the
 * POST/PUT/DELETE rules cannot drift apart from each other.
 */
private val REFERENCE_DATA = arrayOf(
    "/cities/**", "/categories/**", "/subcategories/**", "/locations/**"
)


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
            .authorizeHttpRequests { authorize ->
                // ─────────────────────────────────────────────────────────────────
                //  ORDER MATTERS. Spring Security evaluates these top to bottom and
                //  stops at the first match.
                //
                //  Two rules keep this correct:
                //   1. Qualify writes by HTTP method. `/cities/{id}` also matches
                //      `/cities/create`, so an unqualified public GET pattern used to
                //      shadow the admin POST rule below it (known-issues #1).
                //   2. Where two rules share a method, the NARROWER path must come
                //      first — see `/users/myself` below.
                //
                //  The chain ends in `anyRequest().authenticated()`, so a new endpoint
                //  is private until someone deliberately opens it. It used to end in
                //  `permitAll()`, which made forgetting a matcher a security hole.
                // ─────────────────────────────────────────────────────────────────

                // ── Public: health ──
                authorize.requestMatchers(HttpMethod.GET, "/ping").permitAll()

                // ── Public: authentication and account recovery ──
                authorize.requestMatchers(
                    HttpMethod.POST,
                    "/login",
                    "/signup",
                    "/signup/confirm",
                    "/refreshToken",
                    "/resetPassword",
                    "/resetPassword/confirm",
                    "/resetPassword/change"
                ).permitAll()
                authorize.requestMatchers(
                    HttpMethod.GET, "/signup/confirm/resentConfirmationToken"
                ).permitAll()

                // ── Public: API documentation ──
                authorize.requestMatchers(
                    HttpMethod.GET,
                    "/swagger-ui.html", "/swagger-ui/**",
                    "/v3/api-docs", "/v3/api-docs/**", "/v3/api-docs.yaml"
                ).permitAll()

                // ⚠ MUST precede the public `/users/{id}` rule below. Both are GET, so
                //   method qualification does not separate them, and "myself" matches
                //   the `{id}` placeholder. Declared narrower-first instead.
                authorize.requestMatchers(HttpMethod.GET, "/users/myself").authenticated()

                // ── Public reads: browsing is anonymous by design ──
                authorize.requestMatchers(
                    HttpMethod.GET,
                    "/items/all", "/items/{itemId}",
                    "/users/{id}", "/users/{userId}/items",
                    "/cities/all", "/cities/{id}",
                    "/categories/all", "/categories/{id}",
                    "/subcategories/all", "/subcategories/{id}",
                    "/locations/{id}", "/locations/{cityId}/all"
                ).permitAll()

                // ── Admin ──
                authorize.requestMatchers("/admin/**").hasAuthority(ROLE_ADMIN)
                authorize.requestMatchers("/settings/**").hasAuthority(ROLE_ADMIN)

                // Reference-data writes. Method-qualified so the public GET rules above
                // cannot shadow them — this is the actual fix for known-issues #1.
                authorize.requestMatchers(
                    HttpMethod.POST, *REFERENCE_DATA
                ).hasAuthority(ROLE_ADMIN)
                authorize.requestMatchers(
                    HttpMethod.PUT, *REFERENCE_DATA
                ).hasAuthority(ROLE_ADMIN)
                authorize.requestMatchers(
                    HttpMethod.DELETE, *REFERENCE_DATA
                ).hasAuthority(ROLE_ADMIN)

                // ── Push notifications ──
                // Sending is an operational tool, not a user-facing action. Token
                // registration below it only needs a session, which `anyRequest()` gives.
                authorize.requestMatchers("/api/notifications/send/**").hasAuthority(ROLE_ADMIN)

                // ── Parked: the Thymeleaf admin console login (decision D5) ──
                // `admin/` is unused and unmaintained, so its form login is closed off
                // rather than left reachable. `denyAll` rather than `authenticated`
                // because a login endpoint you must already be logged in to reach is
                // nonsense — this states the intent. Delete these two lines to revive it,
                // and fix the cookie lifetimes first (known-issues #28).
                authorize.requestMatchers(HttpMethod.GET, "/login1").denyAll()
                authorize.requestMatchers(HttpMethod.POST, "/loginValidate").denyAll()

                // ── Everything else needs a session ──
                // Covers: /items/create|update|delete, /users/update|delete,
                // /contacts/**, /address/**, /logout, /is_auth, the image URL endpoints,
                // and /api/notifications/** (which had NO matcher at all and was
                // therefore fully public — see known-issues #4, tightened further in
                // migration task P1-T4).
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

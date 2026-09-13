package com.sharingmap.security

import com.sharingmap.security.config.SecurityConfig
import com.sharingmap.security.jwt.AuthEntryPointJwt
import com.sharingmap.security.jwt.JwtTokenFilter
import com.sharingmap.security.jwt.JwtTokenProvider
import com.sharingmap.user.UserRepository
import com.sharingmap.user.UserService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.kotlin.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.config.annotation.EnableWebMvc

/**
 * Authorization matrix for [SecurityConfig].
 *
 * This exists because the failure mode it guards is **silent**: before migration task
 * P1-T3 the chain ended in `anyRequest().permitAll()` and several admin-only write
 * endpoints were shadowed by an earlier `permitAll` on `/{resource}/{id}`, which matches
 * any single path segment — including `create`. Nothing failed; the endpoints were simply
 * public. See known-issues #1.
 *
 * ## Why this builds its own context instead of using `@WebMvcTest`
 *
 * `@WebMvcTest` does not slice this application. `SharingmapKotlinApplication` carries an
 * explicit `@ComponentScan(basePackages = ["com.sharingmap.*"])`, and an explicit
 * `@ComponentScan` **bypasses the slice's `TypeExcludeFilter`** — so `@WebMvcTest` pulls in
 * every `@Service` and `@Repository` and then fails on `AddressRepository`, which needs
 * JPA. See known-issues #54.
 *
 * So the context here is assembled by hand: the real [SecurityConfig] plus the beans its
 * constructor asks for. Nothing else is loaded, which also keeps the test fast and immune
 * to unrelated wiring changes.
 *
 * ## What is asserted, and why it is not an exact status code
 *
 * No controllers are registered, so a request that passes authorization ends in a 404.
 * That is deliberate: authorization runs **before** handler dispatch, so the security
 * decision is observable without wiring up a single controller or its service graph.
 * Assertions are therefore on status *classes*:
 *
 *  - denied, anonymous      → 401 (via [AuthEntryPointJwt])
 *  - denied, authenticated  → 403
 *  - allowed                → anything that is neither 401 nor 403
 *
 * Add a row here whenever you add an endpoint. A new endpoint is private by default now,
 * so the failure shows up as a 401 here rather than an unnoticed hole in production.
 */
@ExtendWith(SpringExtension::class)
@WebAppConfiguration
@ContextConfiguration(classes = [SecurityConfigTest.SecurityOnlyContext::class])
class SecurityConfigTest {

    /**
     * The smallest context that can exercise the real filter chain: [SecurityConfig] plus
     * the collaborators its constructor asks for.
     *
     * Only *interfaces* are mocked. [JwtTokenProvider] is constructed for real with mocked
     * repositories rather than being mocked itself, because Kotlin classes are final and
     * mocking them leans on Mockito's inline mock maker — an avoidable dependency here.
     * No test sends an `Authorization` header, so the provider is never actually called.
     */
    @Configuration
    @EnableWebMvc
    @Import(SecurityConfig::class)
    class SecurityOnlyContext {

        @Bean
        fun userService(): UserService = mock()

        @Bean
        fun userRepository(): UserRepository = mock()

        @Bean
        fun jwtTokenProvider(userService: UserService, userRepository: UserRepository) =
            JwtTokenProvider(userService, userRepository, "test-secret", 600_000, "/")

        @Bean
        fun jwtTokenFilter(jwtTokenProvider: JwtTokenProvider) = JwtTokenFilter(jwtTokenProvider)

        @Bean
        fun bCryptPasswordEncoder() = BCryptPasswordEncoder()

        @Bean
        fun authEntryPointJwt() = AuthEntryPointJwt()
    }

    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var mvc: MockMvc

    @BeforeEach
    fun setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()
    }

    // ── public ────────────────────────────────────────────────────────────────

    @ParameterizedTest(name = "anonymous {0} {1} is allowed")
    @CsvSource(
        "GET,  /ping",
        "GET,  /items/all",
        "GET,  /items/11111111-1111-1111-1111-111111111111",
        "GET,  /users/11111111-1111-1111-1111-111111111111",
        "GET,  /users/11111111-1111-1111-1111-111111111111/items",
        "GET,  /cities/all",
        "GET,  /cities/1",
        "GET,  /categories/all",
        "GET,  /categories/1",
        "GET,  /subcategories/all",
        "GET,  /subcategories/1",
        "GET,  /locations/1",
        "GET,  /locations/1/all",
        "POST, /login",
        "POST, /signup",
        "POST, /signup/confirm",
        "POST, /refreshToken",
        "POST, /resetPassword",
        "POST, /resetPassword/confirm",
        "POST, /resetPassword/change",
    )
    @DisplayName("anonymous browsing and account recovery stay public")
    fun publicEndpoints(method: String, path: String) = assertAllowed(method, path, anonymous())

    // ── the regression this task exists for ───────────────────────────────────

    @ParameterizedTest(name = "anonymous {0} {1} is rejected")
    @CsvSource(
        // Every row below was reachable without credentials before P1-T3, because an
        // earlier `permitAll` on `/{resource}/{id}` shadowed the admin rule.
        "POST,   /cities/create",
        "POST,   /categories/create",
        "POST,   /subcategories/create",
        "POST,   /locations/create",
        "PUT,    /subcategories/update",
        "PUT,    /cities/update/1",
        "PUT,    /categories/update/1",
        "PUT,    /locations/update/1",
        "DELETE, /cities/delete/1",
        "DELETE, /categories/delete/1",
        "DELETE, /subcategories/delete/1",
        "DELETE, /locations/delete/1",
    )
    @DisplayName("known-issues #1: reference-data writes are no longer public")
    fun referenceDataWritesRejectAnonymous(method: String, path: String) =
        assertUnauthorized(method, path)

    @ParameterizedTest(name = "ROLE_USER {0} {1} is forbidden")
    @CsvSource(
        "POST,   /cities/create",
        "POST,   /categories/create",
        "POST,   /subcategories/create",
        "POST,   /locations/create",
        "PUT,    /subcategories/update",
        "DELETE, /cities/delete/1",
        "GET,    /settings/all",
        "GET,    /admin/users/all",
        "POST,   /admin/items/create/11111111-1111-1111-1111-111111111111",
        "PUT,    /admin/items/update",
    )
    @DisplayName("a plain user cannot reach admin surface")
    fun adminSurfaceRejectsUser(method: String, path: String) =
        assertForbidden(method, path, asUser())

    @ParameterizedTest(name = "ROLE_ADMIN {0} {1} is allowed")
    @CsvSource(
        "POST,   /cities/create",
        "PUT,    /subcategories/update",
        "DELETE, /locations/delete/1",
        "GET,    /settings/all",
        "GET,    /admin/users/all",
        "PUT,    /admin/items/update",
    )
    @DisplayName("an admin can reach admin surface")
    fun adminSurfaceAllowsAdmin(method: String, path: String) = assertAllowed(method, path, asAdmin())

    // ── authenticated-only ────────────────────────────────────────────────────

    @ParameterizedTest(name = "anonymous {0} {1} is rejected")
    @CsvSource(
        "POST,   /items/create",
        "PUT,    /items/update",
        "DELETE, /items/delete/11111111-1111-1111-1111-111111111111",
        "PUT,    /users/update",
        "DELETE, /users/delete",
        "GET,    /users/myself",
        "GET,    /contacts/myself",
        "POST,   /contacts/create",
        "GET,    /address/all",
        "POST,   /address/add",
        "GET,    /is_auth",
        "GET,    /user/photo/urls",
        "GET,    /11111111-1111-1111-1111-111111111111/image/urls",
        // Had no matcher at all before P1-T3 and was therefore fully public.
        // P1-T4 tightens the send endpoints further. known-issues #4.
        "POST,   /api/notifications/tokens",
        "POST,   /api/notifications/send/user",
    )
    @DisplayName("writes and personal data require a session")
    fun protectedEndpointsRejectAnonymous(method: String, path: String) =
        assertUnauthorized(method, path)

    /**
     * `GET /logout` never reaches [com.sharingmap.security.AuthenticationController.logout].
     *
     * Spring Security's default `LogoutFilter` is enabled (nothing calls `http.logout {}`)
     * and, because CSRF is disabled, its matcher accepts **any** method on `/logout`. The
     * filter runs before authorization, logs the session out and redirects — so the
     * controller's `refreshTokenService.deleteByUserId(...)` is dead code and refresh
     * tokens are never revoked server-side. See known-issues #55.
     *
     * This asserts the behaviour that actually exists rather than the behaviour that is
     * documented, so the defect is visible instead of hidden.
     *
     * ⚠ Do not "fix" this by disabling the default logout on its own. That would activate
     * the controller handler — which takes the user id from a **query parameter**, letting
     * any caller revoke another user's refresh token (known-issues #11). The two must be
     * fixed in the same change.
     */
    @Test
    @DisplayName("known-issues #55: GET /logout is swallowed by Spring Security's LogoutFilter")
    fun logoutIsShadowedByTheFrameworkFilter() {
        val status = perform("GET", "/logout", anonymous())
        Assertions.assertEquals(
            302, status,
            "Expected the framework LogoutFilter to handle /logout. If this is no longer " +
                "302, the endpoint has been un-shadowed — make sure known-issues #11 was " +
                "fixed in the same change."
        )
    }

    /**
     * `GET /users/myself` and `GET /users/{id}` are both GET, so HTTP-method qualification
     * cannot separate them and "myself" matches the `{id}` placeholder. The narrower rule
     * is declared first. If someone reorders those two lines, this is the test that fails.
     */
    @Test
    @DisplayName("/users/myself is not shadowed by the public /users/{id} rule")
    fun myselfIsNotShadowed() = assertUnauthorized("GET", "/users/myself")

    // ── parked admin console (decision D5) ────────────────────────────────────

    @ParameterizedTest(name = "{0} {1} is closed")
    @CsvSource("GET, /login1", "POST, /loginValidate")
    @DisplayName("the parked Thymeleaf admin login is unreachable")
    fun parkedAdminConsoleIsClosed(method: String, path: String) = assertUnauthorized(method, path)

    // ── helpers ───────────────────────────────────────────────────────────────

    private fun anonymous(): RequestPostProcessor = RequestPostProcessor { it }

    private fun asUser(): RequestPostProcessor =
        user("user@example.com").authorities(SimpleGrantedAuthority("ROLE_USER"))

    private fun asAdmin(): RequestPostProcessor =
        user("admin@example.com").authorities(SimpleGrantedAuthority("ROLE_ADMIN"))

    private fun perform(method: String, path: String, who: RequestPostProcessor): Int =
        mvc.perform(request(HttpMethod.valueOf(method.trim()), path.trim()).with(who))
            .andReturn().response.status

    private fun assertAllowed(method: String, path: String, who: RequestPostProcessor) {
        val status = perform(method, path, who)
        Assertions.assertTrue(
            status != 401 && status != 403,
            "$method $path should have been allowed but returned $status"
        )
    }

    private fun assertUnauthorized(method: String, path: String) {
        val status = perform(method, path, anonymous())
        Assertions.assertEquals(401, status, "$method $path should reject anonymous callers")
    }

    private fun assertForbidden(method: String, path: String, who: RequestPostProcessor) {
        val status = perform(method, path, who)
        Assertions.assertEquals(403, status, "$method $path should be forbidden for this role")
    }
}

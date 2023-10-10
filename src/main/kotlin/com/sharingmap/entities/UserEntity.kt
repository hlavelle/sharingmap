package com.sharingmap.entities

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID, generator = "user_generator")
    var id: UUID? = null,

    @Column(nullable = false, unique = true, length = 20)
    private var username: String,

    @Column(nullable = false)
    @get:Size(min = 8, message = "Не меньше 8 знаков")
    private var password: String? = null,

//    @Transient
//    var passwordConfirm: String? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role? = null,

    //    var profilePicture: ImageEntity? = null

    //@get:Size(min = 20, max = 300)
    var bio: String? = null,

    @Column(nullable = false, unique = true)
    //@field:Email(message = "{validation.field.email.invalid-format}")
    var email: String? = null,

    var locked: Boolean = false,

    var enabled: Boolean = false,

    @CreationTimestamp
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return listOf(SimpleGrantedAuthority(role!!.name))
    }

    fun setPassword(password: String) {
        this.password = password
    }
    override fun getPassword() = password
    override fun getUsername() = username

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = !locked

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = enabled
    fun assignRole(roleUser: Role) {
        this.role = roleUser
    }

}
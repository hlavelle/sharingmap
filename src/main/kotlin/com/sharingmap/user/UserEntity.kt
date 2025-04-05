package com.sharingmap.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.sharingmap.annotations.Mockable
import com.sharingmap.image.UserImageEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Mockable
@Table(name = "users")
class UserEntity(

    @Column(nullable = false, unique = false, length = 100)
    private var username: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role,

    @JsonIgnore
    @Column(nullable = false)
    private var password: String,

    @Column(nullable = true)
    var giftedItems: Int? = 0,

    @Column(columnDefinition = "text")
    var bio: String? = null,

    var locked: Boolean = false,

    var enabled: Boolean = false,

    @CreationTimestamp
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID = UUID.randomUUID()
) : UserDetails {
    @OneToOne(fetch = FetchType.LAZY, mappedBy="entity")
    val image: UserImageEntity? = null

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return listOf(SimpleGrantedAuthority(role.name))
    }

    fun setPassword(password: String) {
        this.password = password
    }
    override fun getPassword() = password
    override fun getUsername() = username

    fun setUsername(username: String) {
        this.username = username
    }

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = !locked

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = enabled
    fun assignRole(roleUser: Role) {
        this.role = roleUser
    }
}
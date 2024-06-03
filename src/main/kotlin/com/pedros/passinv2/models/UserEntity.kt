package com.pedros.passinv2.models

import com.pedros.passinv2.utils.enums.UserRole
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

@Data
@Table(name="users")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = ["id"])
class UserEntity() : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private var id: UUID ?= null

    private var name: String ?= null
    private var lastname: String ?= null

    private var email: String ?= null
    private var password: String ?= null
    private var username: String ?= null

    val fullName: String
        get() = "${this.name} ${this.lastname}".trim()

    @Enumerated(EnumType.ORDINAL)
    private var role: UserRole ?= UserRole.USER;

    constructor(name: String, lastname: String, email: String, password: String, username: String, role: UserRole) : this() {
        this.name = name
        this.lastname = lastname
        this.email = email
        this.password = password
        this.username = username
        this.role = role
    }

    fun getEmail(): String {
        return email ?: "unknown"
    }

    fun getRole(): UserRole? {
        return role
    }

    override fun getAuthorities(): List<SimpleGrantedAuthority> {
        return when (this.role) {
            UserRole.ADMINISTRATOR -> {
                listOf(
                    SimpleGrantedAuthority("ROLE_ADMIN"),
                    SimpleGrantedAuthority("ROLE_USER"),
                    SimpleGrantedAuthority("ROLE_EVENT_MAKER")
                )
            }
            UserRole.EVENT_MAKER -> {
                listOf(
                    SimpleGrantedAuthority("ROLE_USER"),
                    SimpleGrantedAuthority("ROLE_EVENT_MAKER")
                )
            }
            else -> {
                listOf(
                    SimpleGrantedAuthority("ROLE_USER"),
                )
            }
        }
    }

    override fun getPassword(): String {
        return this.password ?: ""
    }

    override fun getUsername(): String {
        return this.username ?: ""
    }
}
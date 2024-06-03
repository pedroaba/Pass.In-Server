package com.pedros.passinv2.repository

import com.pedros.passinv2.models.UserEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.security.core.userdetails.UserDetails
import java.util.Optional
import java.util.UUID

interface UserRepository : CrudRepository<UserEntity, UUID> {
    fun findByEmail(email: String): UserDetails?
    fun findByUsername(username: String): UserDetails?
    override fun findById(id: UUID): Optional<UserEntity>
}
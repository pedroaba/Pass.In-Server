package com.pedros.passinv2.service

import com.pedros.passinv2.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthorizationService : UserDetailsService {
    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw UsernameNotFoundException("Username is null")
        }

        return userRepository.findByUsername(username = username) ?: throw UsernameNotFoundException("Username is null")
    }
}
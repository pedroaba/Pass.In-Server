package com.pedros.passinv2.service

import com.pedros.passinv2.domain.dto.requests.RegisterUserDTO
import com.pedros.passinv2.domain.dto.responses.sucesses.ProfileResponseDTO
import com.pedros.passinv2.domain.exceptions.InvalidUserEmailException
import com.pedros.passinv2.domain.exceptions.UserAlreadyExistsException
import com.pedros.passinv2.models.UserEntity
import com.pedros.passinv2.repository.UserRepository
import com.pedros.passinv2.utils.enums.UserRole
import com.pedros.passinv2.domain.validator.EmailValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun register(user: RegisterUserDTO): UserEntity {
        val encoder = BCryptPasswordEncoder()

        val passwordHash = encoder.encode(user.password)
        val username = extractUserNameFromEmail(user.email)

        val userOnDatabase = userRepository.findByEmail(user.email)
        if (userOnDatabase != null) {
            throw UserAlreadyExistsException(user.email)
        }

        val emailValidator = EmailValidator(user.email)
        if (!emailValidator.validate()) {
            throw InvalidUserEmailException(user.email)
        }

        val entity = UserEntity(
            name = user.name,
            lastname = "",
            email = user.email,
            password = passwordHash,
            username = username,
            role = UserRole.USER
        )

        this.userRepository.save(entity)
        return entity
    }

    fun findByUsername(username: String): UserDetails? {
        return userRepository.findByUsername(username)
    }

    fun getUserById(userId: UUID): ProfileResponseDTO {
        val user = userRepository.findById(userId).orElseThrow { RuntimeException("User not found") }

        val response = ProfileResponseDTO(
            username = user.username,
            fullName = user.fullName,
            email = user.getEmail(),
            role = user.getRole() ?: UserRole.USER,
        )
        return response
    }

    fun extractUserNameFromEmail(email: String): String {
        return email.split("@").first()
    }
}
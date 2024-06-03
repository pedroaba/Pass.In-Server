package com.pedros.passinv2.controller

import com.pedros.passinv2.domain.dto.requests.AuthDTO
import com.pedros.passinv2.domain.dto.responses.sucesses.LoginResponseDTO
import com.pedros.passinv2.domain.dto.requests.RegisterUserDTO
import com.pedros.passinv2.domain.dto.responses.LoginResponseErrorDTO
import com.pedros.passinv2.domain.dto.responses.errors.RegisterUserResponseErrorDTO
import com.pedros.passinv2.infra.security.TokenService
import com.pedros.passinv2.models.UserEntity
import com.pedros.passinv2.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {
    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var tokenService: TokenService

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestBody @Validated auth: AuthDTO): Any {
        var token = ""
        val result = runCatching {
            val usernamePassword = UsernamePasswordAuthenticationToken(auth.username, auth.password)
            val auth = authenticationManager.authenticate(usernamePassword)
            token = tokenService.generateToken(auth.principal as UserEntity)
        }

        return result.onSuccess { _ ->
            return ResponseEntity.ok(LoginResponseDTO(token))
        }.onFailure { _ ->
            return ResponseEntity.badRequest().body(LoginResponseErrorDTO())
        }
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody user: RegisterUserDTO): Any {
        val result = runCatching {
            userService.register(user)
        }

        return result.onSuccess { _ ->
            return ResponseEntity.status(HttpStatus.CREATED).build<Void>()
        }.onFailure { exception ->
            val response = RegisterUserResponseErrorDTO(
                message = exception.message ?: "Any error occurred with your data. Please try later.",
            )

            return ResponseEntity.badRequest().body(response)
        }
    }
}
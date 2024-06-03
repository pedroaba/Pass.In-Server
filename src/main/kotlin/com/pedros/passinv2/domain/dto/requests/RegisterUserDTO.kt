package com.pedros.passinv2.domain.dto.requests

data class RegisterUserDTO(
    val name: String,
    val email: String,
    val password: String,
)

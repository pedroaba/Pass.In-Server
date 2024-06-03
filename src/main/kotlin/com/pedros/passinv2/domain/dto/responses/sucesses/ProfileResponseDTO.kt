package com.pedros.passinv2.domain.dto.responses.sucesses

import com.pedros.passinv2.utils.enums.UserRole

data class ProfileResponseDTO(
    val email: String,
    val fullName: String,
    val username: String,
    val role: UserRole
)
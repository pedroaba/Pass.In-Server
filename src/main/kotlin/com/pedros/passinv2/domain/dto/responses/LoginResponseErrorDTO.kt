package com.pedros.passinv2.domain.dto.responses

data class LoginResponseErrorDTO(
    val message: String = "Invalid Credentials. Please check your username and password."
)
package com.pedros.passinv2.domain.exceptions

class InvalidUserEmailException(email: String) : RuntimeException("Email: $email is invalid") {
}
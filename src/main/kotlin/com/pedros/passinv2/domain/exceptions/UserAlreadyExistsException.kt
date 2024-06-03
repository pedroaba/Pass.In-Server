package com.pedros.passinv2.domain.exceptions

class UserAlreadyExistsException(email: String) : RuntimeException("User with email: '$email' already exists")
package com.pedros.passinv2.domain.validator

import java.util.regex.Pattern

class EmailValidator(private val email: String) : Validator {
    private val emailPattern: Pattern =
        Pattern.compile(
            "^[a-z0-9.]+@[a-z0-9]+\\.([a-z]+)?$",
            Pattern.CASE_INSENSITIVE
        )

    override fun validate(): Boolean {
        return emailPattern.matcher(email).matches()
    }
}
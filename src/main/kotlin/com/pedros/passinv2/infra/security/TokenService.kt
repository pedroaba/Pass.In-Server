package com.pedros.passinv2.infra.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.pedros.passinv2.domain.exceptions.TokenGenerationException
import com.pedros.passinv2.models.UserEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class TokenService {
    @Value("\${api.security.token.secret}")
    lateinit var secret: String

    private final val issuer = "passin.v2"

    fun generateToken(user: UserEntity): String {
        try {
            val algorithm = Algorithm.HMAC256(secret)

            val token = JWT.create()
                .withIssuer(issuer)
                .withSubject(user.username)
                .withExpiresAt(generateExpirationToken())
                .sign(algorithm)

            return token
        } catch (e: JWTVerificationException) {
            throw TokenGenerationException(e.message)
        }
    }

    fun validateToken(token: String): String {
        try {
            val algorithm = Algorithm.HMAC256(secret)

            return JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token)
                .subject
        } catch (e: JWTVerificationException) {
            return ""
        }
    }

    private fun generateExpirationToken(): Instant {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC)
    }
}
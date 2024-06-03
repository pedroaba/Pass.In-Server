package com.pedros.passinv2.infra.security

import com.pedros.passinv2.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SecurityFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var userService: UserService

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = recoverToken(request)
        if (token != null) {
            val username = tokenService.validateToken(token)
            val user = userService.findByUsername(username)

            val authentication = UsernamePasswordAuthenticationToken(username, null, user?.authorities ?: listOf())
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun recoverToken(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null
        }

        return authHeader.substring("Bearer ".length)
    }
}

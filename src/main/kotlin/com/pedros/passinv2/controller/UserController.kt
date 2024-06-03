package com.pedros.passinv2.controller

import com.pedros.passinv2.domain.dto.responses.sucesses.ProfileResponseDTO
import com.pedros.passinv2.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/users")
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/{userId}")
    fun profile(@PathVariable userId: UUID): ProfileResponseDTO {
        return userService.getUserById(userId)
    }
}
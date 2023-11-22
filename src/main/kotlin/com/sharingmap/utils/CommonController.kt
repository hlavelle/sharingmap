package com.sharingmap.utils

import org.springframework.web.bind.annotation.*

@RestController
class CommonController() {
    @GetMapping("/ping")
    fun pingHandler(): String {
        return "pong"
    }
}
package com.sharingmap.controllers

import org.springframework.web.bind.annotation.*

@RestController
class CommonController() {
    @GetMapping("/ping")
    fun pingHandler(): String {
        return "pong"
    }
}
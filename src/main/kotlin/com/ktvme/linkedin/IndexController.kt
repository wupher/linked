package com.ktvme.linkedin

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {
    @RequestMapping("/")
    fun index(): String {
        return "version 1.0.1 running"
    }
}
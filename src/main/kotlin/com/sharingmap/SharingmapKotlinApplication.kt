package com.sharingmap

import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = arrayOf("com.sharingmap.*"))
class SharingmapKotlinApplication

fun main(args: Array<String>): Unit = runBlocking {
    runApplication<SharingmapKotlinApplication>(*args)
}

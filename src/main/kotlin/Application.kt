package com.selvas

import com.selvas.infrastructure.di.AppModule
import com.selvas.plugins.configureRouting
import com.selvas.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import plugins.DatabaseFactory
import plugins.configureMonitoring

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureRouting(AppModule())
    configureMonitoring()
}

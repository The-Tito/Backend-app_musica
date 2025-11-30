package com.selvas.plugins

import com.selvas.infrastructure.di.AppModule
import com.selvas.presentation.routes.albumRoutes
import com.selvas.presentation.routes.artistRoutes
import com.selvas.presentation.routes.trackRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting(appModule: AppModule) {
    routing {
        route("/api") {
            artistRoutes(appModule.artistUseCase)
            albumRoutes(appModule.albumUseCase)
            trackRoutes(appModule.tracksUseCase)
        }
    }
}

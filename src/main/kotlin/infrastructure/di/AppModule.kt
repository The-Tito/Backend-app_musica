package com.selvas.infrastructure.di

import com.selvas.application.usecases.AlbumUseCase
import com.selvas.application.usecases.ArtistUseCase
import com.selvas.application.usecases.TracksUseCase
import com.selvas.domain.interfaces.AlbumInterface
import com.selvas.domain.interfaces.ArtistInterface
import com.selvas.domain.interfaces.TrackInterface
import com.selvas.infrastructure.repositories.AlbumRepositoryImpl
import com.selvas.infrastructure.repositories.ArtistRepositoryImpl
import com.selvas.infrastructure.repositories.TrackRepositoryImpl
import com.selvas.presentation.routes.albumRoutes
import com.selvas.presentation.routes.artistRoutes
import com.selvas.presentation.routes.trackRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

class AppModule() {

    val artistRepository: ArtistInterface = ArtistRepositoryImpl()
    val albumRepository: AlbumInterface = AlbumRepositoryImpl()
    val trackRepository: TrackInterface = TrackRepositoryImpl()


    val artistUseCase = ArtistUseCase(artistRepository)
    val albumUseCase = AlbumUseCase(albumRepository, artistRepository)
    val tracksUseCase = TracksUseCase(trackRepository, albumRepository, artistRepository)



}
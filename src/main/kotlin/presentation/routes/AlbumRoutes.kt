package com.selvas.presentation.routes

import com.selvas.application.dto.CreateAlbumRequest
import com.selvas.application.dto.UpdateAlbumRequest
import com.selvas.application.usecases.AlbumUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route


fun Route.albumRoutes(
    albumUseCase: AlbumUseCase,
) {

    route("/albumes") {
        get {
            try {
                val albums = albumUseCase.getAllAlbums()
                call.respond(HttpStatusCode.OK, albums)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Error al obtener álbumes: ${e.message}")
                )
            }
        }


        get("/{id}") {
            try {
                val id = call.parameters["id"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID requerido"))

                val response = albumUseCase.getAlbumById(id)
                call.respond(HttpStatusCode.OK, response)

            } catch (e: NoSuchElementException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Error al obtener álbum: ${e.message}")
                )
            }
        }


        post {
            try {
                val request = call.receive<CreateAlbumRequest>()

                if (request.title.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "El título es requerido"))
                    return@post
                }

                if (request.releaseYear < 1900 || request.releaseYear > 2100) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Año de lanzamiento inválido"))
                    return@post
                }

                val response = albumUseCase.createAlbum(request)
                call.respond(HttpStatusCode.Created, response)

            } catch (e: NoSuchElementException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Error al crear álbum: ${e.message}")
                )
            }
        }


        put("/{id}") {
            try {
                val id = call.parameters["id"]
                    ?: return@put call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID requerido"))

                val request = call.receive<UpdateAlbumRequest>()


                if (request.title == null && request.releaseYear == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Debe proporcionar al menos un campo para actualizar"))
                    return@put
                }


                if (request.title != null && request.title.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "El título no puede estar vacío"))
                    return@put
                }

                if (request.releaseYear != null && (request.releaseYear < 1900 || request.releaseYear > 2100)) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Año de lanzamiento inválido"))
                    return@put
                }

                val response = albumUseCase.updateAlbum(id, request)
                call.respond(HttpStatusCode.OK, response)

            } catch (e: NoSuchElementException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Error al actualizar álbum: ${e.message}")
                )
            }
        }


        delete("/{id}") {
            try {
                val id = call.parameters["id"]
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID requerido"))

                val deleted = albumUseCase.deleteAlbum(id)

                if (deleted) {
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Álbum eliminado exitosamente"))
                } else {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "No se pudo eliminar el álbum"))
                }

            } catch (e: NoSuchElementException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Error al eliminar álbum: ${e.message}")
                )
            }
        }

    }
        route("/artistas/{artistId}/albumes") {
            get {
                try {
                    val artistId = call.parameters["artistId"]
                        ?: return@get call.respond(
                            HttpStatusCode.BadRequest,
                            mapOf("error" to "ID de artista requerido")
                        )

                    val albums = albumUseCase.getAlbumsByArtist(artistId)
                    call.respond(HttpStatusCode.OK, albums)

                } catch (e: NoSuchElementException) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        mapOf("error" to "Error al obtener álbumes: ${e.message}")
                    )
                }
            }
        }
}
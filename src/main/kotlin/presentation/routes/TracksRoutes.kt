package com.selvas.presentation.routes

import com.selvas.application.dto.CreateTrackRequest
import com.selvas.application.dto.UpdateTrackRequest
import com.selvas.application.usecases.TracksUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route


fun Route.trackRoutes(
    tracksUseCase: TracksUseCase
) {

    route("/tracks") {
        get {
            try {
                val tracks = tracksUseCase.getAllTracks()
                call.respond(HttpStatusCode.OK, tracks)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Error al obtener tracks: ${e.message}")
                )
            }
        }


        get("/{id}") {
            try {
                val id = call.parameters["id"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID requerido"))

                val response = tracksUseCase.getTrack(id)
                call.respond(HttpStatusCode.OK, response)

            } catch (e: NoSuchElementException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Error al obtener track: ${e.message}")
                )
            }
        }


        post {
            try {
                val request = call.receive<CreateTrackRequest>()


                if (request.title.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "El título es requerido"))
                    return@post
                }

                if (request.duration <= 0) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "La duración debe ser mayor a 0"))
                    return@post
                }

                if (request.duration > 7200) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "La duración no puede exceder 2 horas"))
                    return@post
                }

                val response = tracksUseCase.createTrack(request)
                call.respond(HttpStatusCode.Created, response)

            } catch (e: NoSuchElementException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Error al crear track: ${e.message}")
                )
            }
        }


        put("/{id}") {
            try {
                val id = call.parameters["id"]
                    ?: return@put call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID requerido"))

                val request = call.receive<UpdateTrackRequest>()


                if (request.title == null && request.duration == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        mapOf("error" to "Debe proporcionar al menos un campo para actualizar")
                    )
                    return@put
                }


                if (request.title != null && request.title.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "El título no puede estar vacío"))
                    return@put
                }

                if (request.duration != null && request.duration <= 0) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "La duración debe ser mayor a 0"))
                    return@put
                }

                if (request.duration != null && request.duration > 7200) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "La duración no puede exceder 2 horas"))
                    return@put
                }

                val response = tracksUseCase.updateTrack(id, request)
                call.respond(HttpStatusCode.OK, response)

            } catch (e: NoSuchElementException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Error al actualizar track: ${e.message}")
                )
            }
        }


        delete("/{id}") {
            try {
                val id = call.parameters["id"]
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID requerido"))

                val deleted = tracksUseCase.deleteTrack(id)

                if (deleted) {
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Track eliminado exitosamente"))
                } else {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "No se pudo eliminar el track"))
                }

            } catch (e: NoSuchElementException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Error al eliminar track: ${e.message}")
                )
            }
        }
    }

    route("/albumess/{albumId}/tracks") {
        get {
            try {
                val albumId = call.parameters["albumId"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID de álbum requerido"))

                val tracks = tracksUseCase.getTracksByAlbum(albumId)
                call.respond(HttpStatusCode.OK, tracks)

            } catch (e: NoSuchElementException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Error al obtener tracks: ${e.message}")
                )
            }
        }
    }
}
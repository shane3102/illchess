package pl.illchess.stockfish.server.config.exception_handlers

import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import pl.illchess.stockfish.domain.commons.BadRequestException

@Provider
class BadRequestExceptionHandler : ExceptionMapper<BadRequestException> {
    override fun toResponse(p0: BadRequestException?): Response {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(p0)
            .build()
    }
}
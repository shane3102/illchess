package pl.illchess.stockfish.server.config.exception_handlers

import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import pl.illchess.stockfish.domain.commons.NotFoundException

@Provider
class NotFoundExceptionHandler : ExceptionMapper<NotFoundException> {
    override fun toResponse(p0: NotFoundException?): Response {
        return Response.status(Response.Status.NOT_FOUND)
            .entity(p0)
            .build()
    }
}
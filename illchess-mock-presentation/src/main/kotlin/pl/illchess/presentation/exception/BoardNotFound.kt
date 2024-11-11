package pl.illchess.presentation.exception

import java.util.UUID

class BoardNotFound(boardId: UUID) : RuntimeException(
    "Board with id = %s not found. Assuming game finished and joining next game".format(boardId)
)
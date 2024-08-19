package pl.illchess.player_info.domain.game.exception

import pl.illchess.player_info.domain.commons.exception.DomainException

class NoChessSquareOfGivenValueException(chessSquareValue: String)
    : DomainException("There is no chess square with given name: $chessSquareValue")
package pl.illchess.player_info.domain.game.exception

import pl.illchess.player_info.domain.commons.exception.DomainException

class NoColorOfGivenValueException(colorStringValue: String) : DomainException(
    "There is no chess color with given value $colorStringValue"
)
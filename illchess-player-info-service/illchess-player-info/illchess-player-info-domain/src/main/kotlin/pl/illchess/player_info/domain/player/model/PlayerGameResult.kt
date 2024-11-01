package pl.illchess.player_info.domain.player.model

enum class PlayerGameResult(val asNumber: Double) {
    WON(1.0),
    DRAWN(0.5),
    LOST(0.0);
}
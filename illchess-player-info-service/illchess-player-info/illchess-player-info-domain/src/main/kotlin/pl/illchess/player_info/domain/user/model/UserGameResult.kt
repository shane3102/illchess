package pl.illchess.player_info.domain.user.model

enum class UserGameResult(val asNumber: Double) {
    WON(1.0),
    DRAWN(0.5),
    LOST(0.0);
}
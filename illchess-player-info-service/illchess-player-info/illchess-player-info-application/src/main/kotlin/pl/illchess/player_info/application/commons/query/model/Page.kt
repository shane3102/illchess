package pl.illchess.player_info.application.commons.query.model

data class Page<T>(
    val content: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalPages: Int
)

package pl.illchess.adapter.commons;

public record ErrorMessage(
        int statusCode,
        String message
) {
}

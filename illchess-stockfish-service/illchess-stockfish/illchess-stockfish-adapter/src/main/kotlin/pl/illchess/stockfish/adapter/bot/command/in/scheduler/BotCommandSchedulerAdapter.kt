package pl.illchess.stockfish.adapter.bot.command.`in`.scheduler

import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.application.bot.command.`in`.DeleteExpiredBotsUseCase

@ApplicationScoped
class BotCommandSchedulerAdapter(
    private val deleteExpiredBotsUseCase: DeleteExpiredBotsUseCase
) {

    @Scheduled(cron = "{bots.expiration-check-cron}")
    fun deleteExpiredBots() {
        deleteExpiredBotsUseCase.deleteExpiredBots()
    }
}
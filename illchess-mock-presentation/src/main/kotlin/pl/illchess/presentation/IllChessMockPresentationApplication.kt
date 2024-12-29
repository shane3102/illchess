package pl.illchess.presentation

import pl.illchess.presentation.player.Player
import pl.illchess.presentation.properties.PropertiesLoader
import kotlin.concurrent.thread

fun main(args: Array<String>) {

    val properties = PropertiesLoader.loadProperties()
    val playerCount = properties.getProperty("player.count").toInt()

    val players: MutableList<Player> = mutableListOf()
    for (i in 0 until playerCount) {
        players.add(i, Player("ziomek$i"))
    }

    players.map { thread { it.play() } }
        .forEach(Thread::start)

}
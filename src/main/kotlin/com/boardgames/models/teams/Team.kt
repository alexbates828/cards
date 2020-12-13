package com.boardgames.models.teams

import com.boardgames.models.GamePlayer
import java.util.*

open class Team(val gamePlayers: List<GamePlayer>) {
    val uuid: UUID = UUID.randomUUID()

    open class Builder(private var gamePlayers: MutableList<GamePlayer> = mutableListOf()) {
        fun addPlayer(gamePlayer: GamePlayer) = apply { this.gamePlayers.add(gamePlayer) }
        fun removePlayer(gamePlayer: GamePlayer) = apply { this.gamePlayers.remove(gamePlayer) }

        fun build() = Team(gamePlayers)

        constructor(team: Team) : this(team.gamePlayers.toMutableList())
    }
}
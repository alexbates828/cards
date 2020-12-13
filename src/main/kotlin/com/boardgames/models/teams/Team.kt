package com.boardgames.models.teams

import com.boardgames.models.GamePlayer
import com.boardgames.models.games.Game
import java.util.*

open class Team<TypedPlayer: GamePlayer>(val gamePlayers: List<TypedPlayer>) {
    val uuid: UUID = UUID.randomUUID()

    open class Builder<TypedPlayer: GamePlayer>(var gamePlayers: MutableList<TypedPlayer> = mutableListOf()) {
        fun addPlayer(gamePlayer: TypedPlayer) = apply { this.gamePlayers.add(gamePlayer) }
        fun removePlayer(gamePlayer: TypedPlayer) = apply { this.gamePlayers.remove(gamePlayer) }

        fun build() = Team(gamePlayers)

        constructor(team: Team<TypedPlayer>) : this(team.gamePlayers.toMutableList())
    }
}
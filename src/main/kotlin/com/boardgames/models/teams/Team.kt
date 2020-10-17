package com.boardgames.models.teams

import com.boardgames.models.GamePlayer
import com.boardgames.models.games.Game
import java.util.*

open class Team<TypedPlayer: GamePlayer>(val gamePlayers: List<TypedPlayer>, startingPoints: Int = 0) {
    val uuid: UUID = UUID.randomUUID()
    var points: Int? = startingPoints

    fun addPoints(points: Int) {
        this.points = this.points?.plus(points)
    }

    open class Builder<TypedPlayer: GamePlayer>(var gamePlayers: MutableList<TypedPlayer> = mutableListOf()) {
        fun addPlayer(gamePlayer: TypedPlayer) = apply { this.gamePlayers.add(gamePlayer) }
        fun removePlayer(gamePlayer: TypedPlayer) = apply { this.gamePlayers.remove(gamePlayer) }

        fun build() = Team(gamePlayers)

        constructor(team: Team<TypedPlayer>) : this(team.gamePlayers.toMutableList())
    }
}
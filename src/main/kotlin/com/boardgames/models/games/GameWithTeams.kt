package com.boardgames.models.games

import com.boardgames.models.GamePlayer
import com.boardgames.models.teams.Team
import java.lang.reflect.Type
import java.util.*

open class GameWithTeams<TypedPlayer: GamePlayer>(val teams: List<Team<TypedPlayer>>): Game {
    override val uuid: UUID = UUID.randomUUID()
    val numPlayers = teams.sumBy { it.gamePlayers.count() }

    fun applyToPlayers(predicate: (TypedPlayer) -> Unit) {
        teams.forEach { team -> team.gamePlayers.forEach { player -> predicate(player) } }
    }

    fun anyPlayers(predicate: (TypedPlayer) -> Boolean): Boolean {
        return teams.any { team -> team.gamePlayers.any { player -> predicate(player) } }
    }

    constructor(vararg players: TypedPlayer): this(players.map { Team(listOf(it)) }) {
        if (players.isEmpty()) {
            throw IllegalArgumentException("A game with teams must have players!")
        } else if (players.toHashSet().size != players.size) {
            throw IllegalArgumentException("Cannot pass duplicate players to the constructor.")
        }
    }

    class Builder<TypedPlayer: GamePlayer>(var teams: MutableList<Team<TypedPlayer>> = mutableListOf()) {
        fun addTeam(team: Team<TypedPlayer>) = apply { this.teams.add(team) }
        fun removeTeam(team: Team<TypedPlayer>) = apply { this.teams.remove(team) }

        fun build(): GameWithTeams<TypedPlayer> = GameWithTeams(teams)
    }
}
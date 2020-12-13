package com.boardgames.models.games

import com.boardgames.models.GamePlayer
import com.boardgames.models.teams.Team
import java.util.*

open class GameWithTeams(val teams: List<Team>): Game {
    override val uuid: UUID = UUID.randomUUID()
    val numPlayers = teams.sumBy { it.gamePlayers.count() }

    fun applyToPlayers(predicate: (GamePlayer) -> Unit) {
        teams.forEach { team -> team.gamePlayers.forEach { player -> predicate(player) } }
    }

    fun anyPlayers(predicate: (GamePlayer) -> Boolean): Boolean {
        return teams.any { team -> team.gamePlayers.any { player -> predicate(player) } }
    }

    fun allPlayers(predicate: (GamePlayer) -> Boolean): Boolean {
        return teams.all {team -> team.gamePlayers.any {player -> predicate(player) } }
    }

    constructor(vararg players: GamePlayer): this(players.map { Team(listOf(it)) }) {
        if (players.isEmpty()) {
            throw IllegalArgumentException("A game with teams must have players!")
        } else if (players.toHashSet().size != players.size) {
            throw IllegalArgumentException("Cannot pass duplicate players to the constructor.")
        }
    }

    class Builder(var teams: MutableList<Team> = mutableListOf()) {
        fun addTeam(team: Team) = apply { this.teams.add(team) }
        fun removeTeam(team: Team) = apply { this.teams.remove(team) }

        fun build(): GameWithTeams = GameWithTeams(teams)
    }
}
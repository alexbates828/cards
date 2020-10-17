package com.boardgames.models.games

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*

data class CribbageGameViewModel(@JsonIgnore private val cribbageGame: CribbageGame) {
    val gameUUID: UUID = cribbageGame.uuid
    val go = cribbageGame.total
    val faceUpCard = cribbageGame.faceUpCard
    val teams = cribbageGame.teams
    val dealer = cribbageGame.dealer
    val inProgress = cribbageGame.isInProgress()
}
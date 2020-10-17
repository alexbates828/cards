package com.boardgames.models.games

import com.boardgames.models.CardGamePlayer
import com.boardgames.models.cards.AbstractCard
import com.boardgames.models.teams.Team
import com.boardgames.scoring.ICardGameScoringStrategy

abstract class CardGame<TypedCard : AbstractCard, TypedPlayer: CardGamePlayer<TypedCard>>(teams: List<Team<TypedPlayer>>, private val scoringStrategy: ICardGameScoringStrategy<TypedCard>) : GameWithTeams<TypedPlayer>(teams) {
    abstract fun hasStarted(): Boolean
    abstract fun hasEnded(): Boolean
    fun isInProgress(): Boolean = this.hasStarted() && !this.hasEnded()
    private fun scoreHand(handToScore: Collection<TypedCard>, communityCards: Collection<TypedCard>): Int = scoringStrategy.scoreHand(handToScore, communityCards)
    fun scoreHand(handToScore: Collection<TypedCard>, communityCard: TypedCard): Int = scoreHand(handToScore, listOf(communityCard))
    fun scorePlayedCards(playedCards: Collection<TypedCard>) = scoringStrategy.scorePlayedCards(playedCards)
}

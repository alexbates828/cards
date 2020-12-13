package com.boardgames

import com.boardgames.PokerUtils.pointValue52
import com.boardgames.models.CardGamePlayer
import com.boardgames.models.GamePlayer
import com.boardgames.models.Standard52CardDeck
import com.boardgames.models.cards.PokerCard
import com.boardgames.models.games.CribbageGame
import com.boardgames.models.games.CribbageGamePlayer
import com.boardgames.models.teams.Team
import com.boardgames.scoring.CribbageScoringStrategy
import org.slf4j.LoggerFactory

class CribbageGameRuntime(private val cribbageGame: CribbageGame, private val scoringStrategy: CribbageScoringStrategy) {
    private val scores = cribbageGame.teams.associateWith { 0 }.toMutableMap()

    init {
        // TODO: Check that a player isn't on multiple teams
        with(cribbageGame) {
            if (numPlayers < 2 || numPlayers > 4) {
                throw IllegalArgumentException("A cribbage game must have between 2 and 4 players!")
            } else if (teams.distinctBy { it.gamePlayers.size }.size != 1) {
                throw IllegalArgumentException("Each team must have the same number of players!")
            }
        }
    }

    fun hasStarted(): Boolean = with(cribbageGame) {
        val pointsAreZero = scores.values.all { it == 0 }
        val noCardsHaveBeenDealt = anyPlayers { it.hand.isNotEmpty() }
        return pointsAreZero && noCardsHaveBeenDealt
    }

    fun hasEnded(): Boolean = scores.values.any { it >= 120 }

    fun isGo(): Boolean = with(cribbageGame) {
        !anyPlayers {
            it.hand.any { card -> card.canPlayCard() }
        }
    }

    fun PokerCard.canPlayCard(): Boolean {
        return !this.hasBeenPlayed && this.value.pointValue52() <= 31 - cribbageGame.total
    }

    private fun dealToPlayers() = with(cribbageGame) {
        val numCardsToDealToEachPlayer = if (numPlayers == 2) 6 else 5
        applyToPlayers { it.hand = deck.draw(numCardsToDealToEachPlayer) }
    }

    private fun dealToCrib() = with(cribbageGame) {
        val numCardsToDealToCrib = if (numPlayers == 3) 1 else 0
        crib = deck.draw(numCardsToDealToCrib)
    }

    private fun selectNewDealer() = with(cribbageGame) {
        dealer = dealer.getNextPlayer()
    }

    fun CribbageGamePlayer.getTeam(): Team<CribbageGamePlayer> {
        return cribbageGame.teams.find { it.gamePlayers.contains(this) }!!
    }

    fun CribbageGamePlayer.getNextPlayer(): CribbageGamePlayer {
        TODO()
    }

    fun CribbageGamePlayer.playCard(card: PokerCard) {
        if (!cribbageGame.anyPlayers { it == this }){
            throw IllegalArgumentException("Player $this is not a part of this game!")
        } else if (!this.hand.contains(card)) {
            logger.debug("Player $this attempted to play card $card that was not in their hand.")
            throw IllegalArgumentException("Player cannot play a card that is not in their hand!")
        } else if (!card.canPlayCard()) {
            throw IllegalArgumentException("Card $card is not eligible for play right now.")
        }

        cribbageGame.playedCardStack.add(card)
        card.hasBeenPlayed = true
        cribbageGame.total += card.value.pointValue52()

        val goPoints = if (isGo()) 1 else 0
        val pointsToAdd = scorePlayedCards(cribbageGame.playedCardStack) + goPoints
        this.getTeam().addPoints(pointsToAdd)
    }

    private fun Team<CardGamePlayer<PokerCard>>.addPoints(points: Int) {
        if (!scores.keys.contains(this)) {
            throw IllegalArgumentException("Attempted to add $points points to team $this not in present game.")
        }

        scores[this] = scores[this]!!.plus(points)
    }

    fun scorePlayedCards(playedCards: Collection<PokerCard>) = scoringStrategy.scorePlayedCards(playedCards)
    private fun scoreHand(handToScore: Collection<PokerCard>, communityCards: Collection<PokerCard>): Int = scoringStrategy.scoreHand(handToScore, communityCards)

    private fun endHand() = with(cribbageGame) {
        var playerToScore = dealer
        do {
            playerToScore = playerToScore.getNextPlayer()
            val handValue = scoreHand(playerToScore.hand, communityCards!!)
            playerToScore.getTeam().addPoints(handValue)
        } while (!hasEnded() && playerToScore != dealer)

        val cribValue = scoreHand(crib!!, communityCards!!)
        dealer.getTeam().addPoints(cribValue)
    }

    /**
     * @return the card above the cut card
     */
    @Throws(IllegalStateException::class)
    fun cut(position: Int): PokerCard = with(cribbageGame) {
        if (communityCards.isNullOrEmpty()) {
            throw IllegalStateException("The face-up card has already been cut! Cannot reset it!")
        } else if (anyPlayers { it.hand.size != 4 } || crib.size != 4) {
            throw IllegalStateException("Cannot cut card until all players have dealt one card to the crib.")
        } else if (position < 0 || position > deck.getSize()) {
            throw IllegalArgumentException("Cannot draw from deck at position $position. Deck is a LinkedList of size ${deck.getSize()}.")
        }
        return deck.showOneAtIndex(position - 1).also { communityCards = listOf(deck.drawOneAtIndex(position)) }
    }

    private fun resetGameBoard() = with(cribbageGame) {
        total = 0
        deck = Standard52CardDeck()
        communityCards = listOf()
        crib = listOf()
        applyToPlayers { it.resetHand() }
    }

    private fun deal() {
        resetGameBoard()
        selectNewDealer()
        cribbageGame.deck.shuffle()
        dealToPlayers()
        dealToCrib()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CribbageGameRuntime::class.java)
    }

}

package com.boardgames.models.games

import com.boardgames.PokerUtils.pointValue52
import com.boardgames.models.PokerCardGamePlayer
import com.boardgames.models.Standard52CardDeck
import com.boardgames.models.cards.PokerCard
import com.boardgames.models.teams.Team
import com.boardgames.scoring.CribbageScoringStrategy
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.IllegalArgumentException

typealias CribbageGamePlayer = PokerCardGamePlayer

class CribbageGame(
        teams: List<Team<CribbageGamePlayer>>
) : CardGame<PokerCard, CribbageGamePlayer>(teams, CribbageScoringStrategy) {
    var total = 0
        private set
    private var deck = Standard52CardDeck()
    var faceUpCard: PokerCard? = null
        private set
    private var crib: List<PokerCard> = listOf()
    var playedCards: LinkedList<PokerCard> = LinkedList()
        private set
    lateinit var dealer: CribbageGamePlayer
        private set

    init {
        // TODO: Check that a player isn't on multiple teams

        if (numPlayers < 2 || numPlayers > 4) {
            throw IllegalArgumentException("A cribbage game must have between 2 and 4 players!")
        } else if (teams.distinctBy { it.gamePlayers.size }.size != 1) {
            throw IllegalArgumentException("Each team must have the same number of players!")
        }
    }

    fun CribbageGamePlayer.getNextPlayer(): CribbageGamePlayer {
        TODO()
    }

    fun CribbageGamePlayer.getTeam(): Team<CribbageGamePlayer> {
        return teams.find { it.gamePlayers.contains(this) }!!
    }

    private fun endHand() {
        var playerToScore = dealer
        do {
            playerToScore = playerToScore.getNextPlayer()
            val handValue = scoreHand(playerToScore.hand, faceUpCard!!)
            playerToScore.getTeam().addPoints(handValue)
        } while (!hasEnded() && playerToScore != dealer)

        val cribValue = scoreHand(crib!!, faceUpCard!!)
        dealer.getTeam().addPoints(cribValue)
    }

    fun CribbageGamePlayer.playCard(card: PokerCard) {
        if (!anyPlayers { it == this }){
            throw IllegalArgumentException("Player $this is not a part of this game!")
        } else if (!this.hand.contains(card)) {
            logger.debug("Player $this attempted to play card $card that was not in their hand.")
            throw IllegalArgumentException("Player cannot play a card that is not in their hand!")
        } else if (!card.canPlayCard()) {
            throw IllegalArgumentException("Card $card is not eligible for play right now.")
        }

        playedCards.add(card)
        card.hasBeenPlayed = true
        total += card.value.pointValue52()

        val goPoints = if (isGo()) 1 else 0
        val pointsToAdd = scorePlayedCards(playedCards) + goPoints
        this.getTeam().addPoints(pointsToAdd)
    }

    fun isGo(): Boolean {
        return !anyPlayers { it.hand.any { card -> card.canPlayCard() } }
    }

    fun PokerCard.canPlayCard(): Boolean {
        return !this.hasBeenPlayed && this.value.pointValue52() <= 31 - total
    }

    private fun resetGameBoard() {
        total = 0
        deck = Standard52CardDeck()
        faceUpCard = null
        crib = listOf()
        applyToPlayers { it.resetHand() }
    }

    private fun dealToPlayers() {
        val numCardsToDealToEachPlayer = if (numPlayers == 2) 6 else 5
        applyToPlayers { it.hand = deck.draw(numCardsToDealToEachPlayer) }
    }

    private fun dealToCrib() {
        val numCardsToDealToCrib = if (numPlayers == 3) 1 else 0
        crib = deck.draw(numCardsToDealToCrib)
    }

    private fun deal() {
        resetGameBoard()
        selectNewDealer()
        deck.shuffle()
        dealToPlayers()
        dealToCrib()
    }

    private fun selectNewDealer() {
        dealer = dealer.getNextPlayer()
    }

    /**
     * @return the card above the cut card
     */
    @Throws(IllegalStateException::class)
    fun cut(position: Int): PokerCard {
        if (faceUpCard != null) {
            throw IllegalStateException("The face-up card has already been cut! Cannot reset it!")
        } else if (anyPlayers { it.hand.size != 4 } || crib.size != 4) {
            throw IllegalStateException("Cannot cut card until all players have dealt one card to the crib.")
        } else if (position < 0 || position > deck.getSize()) {
            throw IllegalArgumentException("Cannot draw from deck at position $position. Deck is a LinkedList of size ${deck.getSize()}.")
        }
        return deck.showOneAtIndex(position - 1).also { faceUpCard = deck.drawOneAtIndex(position) }
    }

    override fun hasStarted(): Boolean {
        val pointsAreZero = teams.all { it.points == 0 }
        val noCardsHaveBeenDealt = anyPlayers { it.hand.isNotEmpty() }
        return pointsAreZero && noCardsHaveBeenDealt
    }

    override fun hasEnded(): Boolean {
        return teams.any { it.points!! >= 120 }
    }

    private companion object {
        val logger = LoggerFactory.getLogger(this::class.java)
    }
}

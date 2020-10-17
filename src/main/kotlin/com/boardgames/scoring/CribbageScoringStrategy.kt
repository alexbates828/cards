package com.boardgames.scoring

import com.boardgames.PokerUtils.countPairs
import com.boardgames.PokerUtils.findLargestRun
import com.boardgames.PokerUtils.findRunsOfSize
import com.boardgames.PokerUtils.isFlush
import com.boardgames.PokerUtils.isRummyRunNoJoker
import com.boardgames.PokerUtils.pointValue52
import com.boardgames.models.cards.PokerCard
import com.boardgames.models.cards.PokerValue
import com.google.common.collect.Sets
import java.lang.IllegalStateException

object CribbageScoringStrategy : ICardGameScoringStrategy<PokerCard> {

    override fun scoreHand(heldCards: Collection<PokerCard>, communityCards: Collection<PokerCard>): Int {
        val cardsToBeScored = heldCards.union(communityCards)

        if (cardsToBeScored.any { it.value == PokerValue.Joker }) {
            throw IllegalStateException("Cribbage hands should not contain any Jokers!")
        } else if (communityCards.size != 1) {
            throw IllegalStateException("There should be exactly one face-up community card in a hand of cribbage!")
        }

        val faceUpCard = communityCards.first()

        var score = 0

        if (heldCards.isFlush()) {
            score += heldCards.count()
            if (faceUpCard.suit == heldCards.first().suit) {
                score += 1
            }
        }

        // Check for knobs
        if (heldCards.any { it.value == PokerValue.Jack && it.suit == faceUpCard.suit }) {
            score += 1
        }

        // Check for fifteens
        val numFifteens = cardsToBeScored.countFifteens()
        score += 2 * numFifteens

        // Check for two/three/four of a kind
        val numPairs = cardsToBeScored.countPairs()
        score += 2 * numPairs

        // Check for runs
        val largestRun = cardsToBeScored.findLargestRun()
        if (largestRun.size >= 3) {
            val runs = cardsToBeScored.findRunsOfSize(largestRun.size)
            score += runs.sumBy { it.size }
        }

        return score
    }

    override fun scorePlayedCards(playedCards: Collection<PokerCard>): Int = scorePlayedCards(playedCards.toList())

    fun scorePlayedCards(playedCards: List<PokerCard>): Int {
        if (playedCards.size <= 1) {
            return 0
        }

        val total = playedCards.sumBy { it.value.pointValue52() }

        var points = 0
        if (total == 15) {
            points += 2
        } else if (total == 31) {
            // Technically, one point is earned for a 31, and the other point for the "go" is handled downstream
            points += 1
        }

        val playedCardsSize = playedCards.size
        val greedyRun = mutableListOf<PokerCard>()

        for (i in 0 until playedCardsSize) {
            greedyRun.add(playedCards[playedCardsSize-1-i])
            if (!greedyRun.isRummyRunNoJoker()) {
                break
            }
        }
        if (!greedyRun.isRummyRunNoJoker()) {
            greedyRun.removeAt(greedyRun.size-1)
        }

        if (greedyRun.size >= 3) {
            points += greedyRun.size
        }

        val greedyPairs = mutableListOf<PokerCard>()
        for (i in 0 until playedCardsSize) {
            greedyPairs.add(playedCards[playedCardsSize-1-i])
            if (greedyPairs.distinctBy { it.value }.size > 1) {
                break
            }
        }
        if (greedyPairs.distinctBy { it.value }.size > 1) {
            greedyPairs.removeAt(greedyPairs.size-1)
        }

        if (greedyPairs.size >= 2) {
            points += 2*greedyPairs.size
        }

        return points
    }

    fun Set<PokerCard>.countFifteens(): Int {
        var numFifteens = 0
        for (i in 1..this.size) {
            val fifteens = Sets.combinations(this, i).filter {
                it.sumBy { card -> card.value.pointValue52() } == 15
            }
            numFifteens += fifteens.size
        }
        return numFifteens
    }

    fun Collection<PokerCard>.countFifteens() = this.toSet().countFifteens()

}

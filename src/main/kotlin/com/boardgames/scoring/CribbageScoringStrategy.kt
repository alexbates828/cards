package com.boardgames.scoring

import com.boardgames.PokerUtils.countPairs
import com.boardgames.PokerUtils.findLargestRun
import com.boardgames.PokerUtils.findRunsOfSize
import com.boardgames.PokerUtils.isFlush
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

package com.boardgames

import com.boardgames.models.cards.PokerCard
import com.boardgames.models.cards.PokerSuit
import com.boardgames.models.cards.PokerValue
import com.google.common.collect.Sets
import java.lang.RuntimeException

object PokerUtils {
    fun Collection<PokerCard>.isFlush(): Boolean {
        val numSuits = this.distinctBy { it.suit }.count()
        return numSuits == 1
    }

    fun Collection<PokerCard>.isSuitedRummyRunNoJoker(): Boolean {
        if (!this.isFlush()) {
            return false
        }
        return this.isRummyRunNoJoker()
    }

    fun Collection<PokerCard>.isRummyRunNoJoker(): Boolean {
        val sortedValues = this.map { it.value.sortValue52() }.sorted()

        for (i in 0.until(sortedValues.size)) {
            if (sortedValues[0] + i != sortedValues[i]) {
                return false
            }
        }
        return true
    }

    fun Set<PokerCard>.findLargestRun(): Set<PokerCard> {
        if (this.isEmpty()) {
            return emptySet()
        }
        for (i in this.size downTo 2) {
            val possibleRuns = this.findRunsOfSize(i)
            if (possibleRuns.isNotEmpty()) {
                return possibleRuns.first()
            }
        }
        return setOf(this.first())
    }

    fun Set<PokerCard>.findRunsOfSize(size: Int): List<Set<PokerCard>> {
        return if (size < 0) {
            throw IllegalArgumentException()
        } else if (size == 0) {
            listOf()
        } else {
            Sets.combinations(this, size).filter { it.isRummyRunNoJoker() }
        }
    }

    fun Set<PokerCard>.countPairs(): Int {
        return if (this.size < 2) {
            0
        } else {
            Sets.combinations(this, 2).filter {
                it.elementAt(0).value == it.elementAt(1).value
            }.size
        }
    }

    fun get52CardDeck(): List<PokerCard> = PokerValue.values().filterNot { it == PokerValue.Joker }.flatMap { value ->
        PokerSuit.values().map { suit ->
            PokerCard(value, suit)
        }
    }

    fun get54CardDeck() = get52CardDeck().union(listOf(PokerCard(PokerValue.Joker, null), PokerCard(PokerValue.Joker, null)))

    fun Collection<PokerCard>.countPairs(): Int = this.toSet().countPairs()
    fun Collection<PokerCard>.findLargestRun() = this.toSet().findLargestRun()
    fun Collection<PokerCard>.findRunsOfSize(size: Int) = this.toSet().findRunsOfSize(size)

    fun PokerValue.sortValue52(): Int {
        return when (this) {
            PokerValue.Ace -> 1
            PokerValue.Two -> 2
            PokerValue.Three -> 3
            PokerValue.Four -> 4
            PokerValue.Five -> 5
            PokerValue.Six -> 6
            PokerValue.Seven -> 7
            PokerValue.Eight -> 8
            PokerValue.Nine -> 9
            PokerValue.Ten -> 10
            PokerValue.Jack -> 11
            PokerValue.Queen -> 12
            PokerValue.King -> 13
            else -> throw RuntimeException()
        }
    }

    fun PokerValue.pointValue52(): Int {
        return when (this) {
            PokerValue.Ace -> 1
            PokerValue.Two -> 2
            PokerValue.Three -> 3
            PokerValue.Four -> 4
            PokerValue.Five -> 5
            PokerValue.Six -> 6
            PokerValue.Seven -> 7
            PokerValue.Eight -> 8
            PokerValue.Nine -> 9
            PokerValue.Ten -> 10
            PokerValue.Jack -> 10
            PokerValue.Queen -> 10
            PokerValue.King -> 10
            else -> throw RuntimeException()
        }
    }
}
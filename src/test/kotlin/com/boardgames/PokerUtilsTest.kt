package com.boardgames

import com.boardgames.PokerUtils.countPairs
import com.boardgames.PokerUtils.findLargestRun
import com.boardgames.PokerUtils.isFlush
import com.boardgames.PokerUtils.isRummyRunNoJoker
import com.boardgames.PokerUtils.isSuitedRummyRunNoJoker
import com.boardgames.models.cards.PokerCard
import com.boardgames.models.cards.PokerSuit
import com.boardgames.models.cards.PokerValue
import org.junit.Assert
import org.junit.jupiter.api.Test

class PokerUtilsTest {
    @Test
    fun `one heart - isFlush - expecting True`() {
        val cards = listOf(
                PokerCard(PokerValue.Five, PokerSuit.Hearts)
        )

        Assert.assertTrue(cards.isFlush())
    }

    @Test
    fun `four hearts - isFlush - expecting True`() {
        val cards = listOf(
                PokerCard(PokerValue.Five, PokerSuit.Hearts),
                PokerCard(PokerValue.Six, PokerSuit.Hearts),
                PokerCard(PokerValue.Seven, PokerSuit.Hearts),
                PokerCard(PokerValue.Eight, PokerSuit.Hearts)
        )

        Assert.assertTrue(cards.isFlush())
    }

    @Test
    fun `club and a spade - isFlush - expecting False`() {
        val cards = listOf(
                PokerCard(PokerValue.Five, PokerSuit.Clubs),
                PokerCard(PokerValue.Six, PokerSuit.Spades)
        )

        Assert.assertFalse(cards.isFlush())
    }

    @Test
    fun `empty list - isRun - expecting True`() {
        val cards = listOf<PokerCard>()
        Assert.assertTrue(cards.isRummyRunNoJoker())
    }

    @Test
    fun `singleton list - isRun - expecting True`() {
        val cards = listOf<PokerCard>(PokerCard(PokerValue.Queen, PokerSuit.Hearts))
        Assert.assertTrue(cards.isRummyRunNoJoker())
    }

    @Test
    fun `two three four offsuit - isRun - expecting True`() {
        val cards = listOf(
                PokerCard(PokerValue.Two, PokerSuit.Hearts),
                PokerCard(PokerValue.Three, PokerSuit.Diamonds),
                PokerCard(PokerValue.Four, PokerSuit.Spades)
        )

        Assert.assertTrue(cards.isRummyRunNoJoker())
    }

    @Test
    fun `two three four offsuit - isSuitedRun - expecting False`() {
        val cards = listOf(
                PokerCard(PokerValue.Two, PokerSuit.Hearts),
                PokerCard(PokerValue.Three, PokerSuit.Diamonds),
                PokerCard(PokerValue.Four, PokerSuit.Spades)
        )

        Assert.assertFalse(cards.isSuitedRummyRunNoJoker())
    }

    @Test
    fun `jack queen king suited - isSuitedRun - expecting True`() {
        val cards = listOf(
                PokerCard(PokerValue.Jack, PokerSuit.Clubs),
                PokerCard(PokerValue.Queen, PokerSuit.Clubs),
                PokerCard(PokerValue.King, PokerSuit.Clubs)
        )

        Assert.assertTrue(cards.isSuitedRummyRunNoJoker())
    }

    @Test
    fun `empty list - countPairs - expecting zero`() {
        val cards = listOf<PokerCard>()

        val numPairs = cards.countPairs()

        Assert.assertEquals(0, numPairs)
    }

    @Test
    fun `singleton list - countPairs - expecting zero`() {
        val cards = listOf<PokerCard>(PokerCard(PokerValue.King, PokerSuit.Diamonds))

        val numPairs = cards.countPairs()

        Assert.assertEquals(0, numPairs)
    }

    @Test
    fun `two nines - countPairs - expecting two`() {
        val cards = listOf(
                PokerCard(PokerValue.Nine, PokerSuit.Hearts),
                PokerCard(PokerValue.Nine, PokerSuit.Clubs)
        )

        val numPairs = cards.countPairs()

        Assert.assertEquals(1, numPairs)
    }

    @Test
    fun `four nines - countPairs - expecting six`() {
        val cards = listOf(
                PokerCard(PokerValue.Nine, PokerSuit.Hearts),
                PokerCard(PokerValue.Nine, PokerSuit.Clubs),
                PokerCard(PokerValue.Nine, PokerSuit.Spades),
                PokerCard(PokerValue.Nine, PokerSuit.Diamonds)
        )

        val numPairs = cards.countPairs()

        Assert.assertEquals(6, numPairs)
    }

    @Test
    fun `two nines and two jacks - countPairs - expecting two`() {
        val cards = listOf(
                PokerCard(PokerValue.Nine, PokerSuit.Hearts),
                PokerCard(PokerValue.Nine, PokerSuit.Clubs),
                PokerCard(PokerValue.Jack, PokerSuit.Spades),
                PokerCard(PokerValue.Jack, PokerSuit.Diamonds)
        )

        val numPairs = cards.countPairs()

        Assert.assertEquals(2, numPairs)
    }

    @Test
    fun `three four five offsuit - findLargestRun size - expecting three`() {
        val cards = setOf(
                PokerCard(PokerValue.Three, PokerSuit.Hearts),
                PokerCard(PokerValue.Four, PokerSuit.Clubs),
                PokerCard(PokerValue.Five, PokerSuit.Diamonds)
        )

        val largestRun = cards.findLargestRun()

        Assert.assertEquals(3, largestRun.size)
    }

    @Test
    fun `ace six seven eight nine offsuit - findLargestRun size - expecting four`() {
        val cards = setOf(
                PokerCard(PokerValue.Ace, PokerSuit.Hearts),
                PokerCard(PokerValue.Six, PokerSuit.Clubs),
                PokerCard(PokerValue.Seven, PokerSuit.Diamonds),
                PokerCard(PokerValue.Eight, PokerSuit.Spades),
                PokerCard(PokerValue.Nine, PokerSuit.Hearts)

        )

        val largestRun = cards.findLargestRun()

        Assert.assertEquals(4, largestRun.size)
    }

    @Test
    fun `nine ten king jack queen offsuit - findLargestRun size - expecting five`() {
        val cards = setOf(
                PokerCard(PokerValue.Nine, PokerSuit.Hearts),
                PokerCard(PokerValue.Ten, PokerSuit.Clubs),
                PokerCard(PokerValue.King, PokerSuit.Diamonds),
                PokerCard(PokerValue.Jack, PokerSuit.Spades),
                PokerCard(PokerValue.Queen, PokerSuit.Hearts)

        )

        val largestRun = cards.findLargestRun()

        Assert.assertEquals(5, largestRun.size)
    }

    @Test
    fun `emptySet - findLargestRun size - expecting zero`() {
        val cards = emptySet<PokerCard>()

        val largestRun = cards.findLargestRun()

        Assert.assertEquals(0, largestRun.size)
    }

    @Test
    fun `two four six eight ten offsuit - findLargestRun size - expecting 1`() {
        val cards = setOf(
                PokerCard(PokerValue.Two, PokerSuit.Hearts),
                PokerCard(PokerValue.Four, PokerSuit.Clubs),
                PokerCard(PokerValue.Six, PokerSuit.Diamonds),
                PokerCard(PokerValue.Eight, PokerSuit.Spades),
                PokerCard(PokerValue.Ten, PokerSuit.Hearts)
        )

        val largestRun = cards.findLargestRun()

        Assert.assertEquals(1, largestRun.size)
    }

    @Test
    fun `three four four five offsuit - findLargestRun size - expecting three`() {
        val cards = setOf(
                PokerCard(PokerValue.Three, PokerSuit.Hearts),
                PokerCard(PokerValue.Four, PokerSuit.Clubs),
                PokerCard(PokerValue.Four, PokerSuit.Diamonds),
                PokerCard(PokerValue.Five, PokerSuit.Diamonds)
        )

        val largestRun = cards.findLargestRun()

        Assert.assertEquals(3, largestRun.size)
    }

    @Test
    fun `get52CardDeck yields a standard 52 card deck`() {
        val deck = PokerUtils.get52CardDeck()

        Assert.assertEquals(52, deck.size)
        Assert.assertTrue(deck.groupBy { it.suit }.values.all { it.size == 13 })
        Assert.assertTrue(deck.groupBy { it.value }.values.all { it.size == 4 })
    }
}

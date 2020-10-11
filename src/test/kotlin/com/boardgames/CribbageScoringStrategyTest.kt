package com.boardgames

import com.boardgames.scoring.CribbageScoringStrategy.countFifteens
import com.boardgames.models.cards.PokerCard
import com.boardgames.models.cards.PokerSuit
import com.boardgames.models.cards.PokerValue
import com.boardgames.scoring.CribbageScoringStrategy
import org.junit.Assert.*
import org.junit.jupiter.api.Test

class CribbageScoringStrategyTest {

    @Test
    fun `two eights and a seven - countFifteens - expecting two`() {
        val cards = listOf(
                PokerCard(PokerValue.Eight, PokerSuit.Hearts),
                PokerCard(PokerValue.Eight, PokerSuit.Clubs),
                PokerCard(PokerValue.Seven, PokerSuit.Diamonds)
        )

        val fifteens = cards.countFifteens()

        assertEquals(2, fifteens)
    }

    @Test
    fun `two twos a three and two jacks - countFifteens - expecting four`() {
        val cards = listOf(
                PokerCard(PokerValue.Two, PokerSuit.Hearts),
                PokerCard(PokerValue.Two, PokerSuit.Clubs),
                PokerCard(PokerValue.Three, PokerSuit.Diamonds),
                PokerCard(PokerValue.Jack, PokerSuit.Clubs),
                PokerCard(PokerValue.Jack, PokerSuit.Spades)
        )

        val fifteens = cards.countFifteens()

        assertEquals(4, fifteens)
    }

    @Test
    fun `four fives and a ten - countFifteens - expecting eight`() {
        val cards = listOf(
                PokerCard(PokerValue.Five, PokerSuit.Hearts),
                PokerCard(PokerValue.Five, PokerSuit.Clubs),
                PokerCard(PokerValue.Five, PokerSuit.Diamonds),
                PokerCard(PokerValue.Five, PokerSuit.Spades),
                PokerCard(PokerValue.Ten, PokerSuit.Spades)
        )

        val fifteens = cards.countFifteens()

        assertEquals(8, fifteens)
    }

    @Test
    fun `two nines - countFifteens - expecting zero`() {
        val cards = listOf(
                PokerCard(PokerValue.Nine, PokerSuit.Hearts),
                PokerCard(PokerValue.Nine, PokerSuit.Clubs)
        )

        val fifteens = cards.countFifteens()

        assertEquals(0, fifteens)
    }

    @Test
    fun `double run offsuit - scoreHand - expecting eight`() {
        val heldCards = listOf(
                PokerCard(PokerValue.Ace, PokerSuit.Hearts),
                PokerCard(PokerValue.Two, PokerSuit.Diamonds),
                PokerCard(PokerValue.Two, PokerSuit.Clubs),
                PokerCard(PokerValue.Three, PokerSuit.Spades)
        )

        val faceUpCard = listOf(PokerCard(PokerValue.Six, PokerSuit.Hearts))

        val score = CribbageScoringStrategy.scoreHand(heldCards, faceUpCard)

        assertEquals(8, score)
    }

    @Test
    fun `double run suited - scoreHand - expecting twelve`() {
        val heldCards = listOf(
                PokerCard(PokerValue.Ace, PokerSuit.Hearts),
                PokerCard(PokerValue.Six, PokerSuit.Hearts),
                PokerCard(PokerValue.Two, PokerSuit.Hearts),
                PokerCard(PokerValue.Three, PokerSuit.Hearts)
        )

        val faceUpCard = listOf(PokerCard(PokerValue.Two, PokerSuit.Diamonds))

        val score = CribbageScoringStrategy.scoreHand(heldCards, faceUpCard)

        assertEquals(12, score)
    }

    @Test
    fun `double run with fifteens offsuit - scoreHand - expecting twelve`() {
        val heldCards = listOf(
                PokerCard(PokerValue.Six, PokerSuit.Hearts),
                PokerCard(PokerValue.Seven, PokerSuit.Hearts),
                PokerCard(PokerValue.Seven, PokerSuit.Clubs),
                PokerCard(PokerValue.Eight, PokerSuit.Hearts)
        )

        val faceUpCard = listOf(PokerCard(PokerValue.Three, PokerSuit.Diamonds))

        val score = CribbageScoringStrategy.scoreHand(heldCards, faceUpCard)

        assertEquals(12, score)
    }

    @Test
    fun `fifteens and knobs - scoreHand - expecting 29`() {
        val heldCards = listOf(
                PokerCard(PokerValue.Five, PokerSuit.Spades),
                PokerCard(PokerValue.Five, PokerSuit.Hearts),
                PokerCard(PokerValue.Five, PokerSuit.Clubs),
                PokerCard(PokerValue.Jack, PokerSuit.Diamonds)
        )

        val faceUpCard = listOf(PokerCard(PokerValue.Five, PokerSuit.Diamonds))

        val score = CribbageScoringStrategy.scoreHand(heldCards, faceUpCard)

        assertEquals(29, score)
    }

    @Test
    fun `face card run and fifteens but no knobs - scoreHand - expecting 17`() {
        val heldCards = listOf(
                PokerCard(PokerValue.Jack, PokerSuit.Spades),
                PokerCard(PokerValue.Queen, PokerSuit.Hearts),
                PokerCard(PokerValue.King, PokerSuit.Clubs),
                PokerCard(PokerValue.Five, PokerSuit.Spades)
        )

        val faceUpCard = listOf(PokerCard(PokerValue.Five, PokerSuit.Diamonds))

        val score = CribbageScoringStrategy.scoreHand(heldCards, faceUpCard)

        assertEquals(17, score)
    }

    @Test
    fun `face card run and fifteens with knobs - scoreHand - expecting 18`() {
        val heldCards = listOf(
                PokerCard(PokerValue.Jack, PokerSuit.Spades),
                PokerCard(PokerValue.Queen, PokerSuit.Hearts),
                PokerCard(PokerValue.King, PokerSuit.Clubs),
                PokerCard(PokerValue.Five, PokerSuit.Diamonds)
        )

        val faceUpCard = listOf(PokerCard(PokerValue.Five, PokerSuit.Spades))

        val score = CribbageScoringStrategy.scoreHand(heldCards, faceUpCard)

        assertEquals(18, score)
    }

    @Test
    fun `all evens offsuit no pairs - scoreHand - expecting 0`() {
        val heldCards = listOf(
                PokerCard(PokerValue.Two, PokerSuit.Spades),
                PokerCard(PokerValue.Four, PokerSuit.Hearts),
                PokerCard(PokerValue.Six, PokerSuit.Clubs),
                PokerCard(PokerValue.Eight, PokerSuit.Diamonds)
        )

        val faceUpCard = listOf(PokerCard(PokerValue.Ten, PokerSuit.Spades))

        val score = CribbageScoringStrategy.scoreHand(heldCards, faceUpCard)

        assertEquals(0, score)
    }

    @Test
    fun `all evens suited no pairs with faceUpCard not matching - scoreHand - expecting 4`() {
        val heldCards = listOf(
                PokerCard(PokerValue.Two, PokerSuit.Spades),
                PokerCard(PokerValue.Four, PokerSuit.Spades),
                PokerCard(PokerValue.Six, PokerSuit.Spades),
                PokerCard(PokerValue.Eight, PokerSuit.Spades)
        )

        val faceUpCard = listOf(PokerCard(PokerValue.Ten, PokerSuit.Diamonds))

        val score = CribbageScoringStrategy.scoreHand(heldCards, faceUpCard)

        assertEquals(4, score)
    }

    @Test
    fun `all evens suited no pairs with faceUpCard matching - scoreHand - expecting 5`() {
        val heldCards = listOf(
                PokerCard(PokerValue.Two, PokerSuit.Spades),
                PokerCard(PokerValue.Four, PokerSuit.Spades),
                PokerCard(PokerValue.Six, PokerSuit.Spades),
                PokerCard(PokerValue.Eight, PokerSuit.Spades)
        )

        val faceUpCard = listOf(PokerCard(PokerValue.Ten, PokerSuit.Spades))

        val score = CribbageScoringStrategy.scoreHand(heldCards, faceUpCard)

        assertEquals(5, score)
    }

}

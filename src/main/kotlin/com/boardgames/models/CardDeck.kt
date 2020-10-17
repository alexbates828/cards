package com.boardgames.models

import com.boardgames.models.cards.AbstractCard
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*
import kotlin.math.min

open class CardDeck<TypedCard: AbstractCard>(private var cards: LinkedList<TypedCard>) {

    fun getSize(): Int {
        return cards.size
    }

    private var initialCards = cards.clone() as LinkedList<TypedCard>

    fun drawOne(): TypedCard = cards.pop()

    fun draw(numCardsToDraw: Int): List<TypedCard> = when {
            numCardsToDraw < 0 -> throw IllegalArgumentException("Cannot draw a negative number of cards!")
            numCardsToDraw == 0 -> listOf()
            numCardsToDraw > cards.size -> cards.also {
                logger.debug("Attempted to draw more cards than the number of cards in the deck!")
                cards.clear()
            }
            else -> (1..numCardsToDraw).map { drawOne() }
    }

    fun shuffle(): Unit = cards.shuffle()

    internal fun showOneAtIndex(position: Int): TypedCard = cards[position]

    internal fun drawOneAtIndex(position: Int): TypedCard = cards.removeAt(position)

    fun resetDeck() {
        cards = initialCards.clone() as LinkedList<TypedCard>
    }

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)
    }
}

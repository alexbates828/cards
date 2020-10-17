package com.boardgames.models.cards

import org.slf4j.LoggerFactory

class PokerCard(
        val value: PokerValue,
        val suit: PokerSuit?
) : AbstractCard {
    override var hasBeenPlayed: Boolean = false

    val shortName: String
        get() {
            var name = "${value.characterRepresentation}"
            if (suit != null) {
                name += "${suit.characterRepresentation}"
            }
            return name
        }

    val fullName: String
        get() = "$value of $suit"

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}

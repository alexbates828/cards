package com.boardgames.models

import com.boardgames.models.cards.AbstractCard
import com.boardgames.models.cards.PokerCard
import com.boardgames.models.games.CardGame
import com.boardgames.models.games.Game
import com.fasterxml.jackson.annotation.JsonIgnore

typealias PokerCardGamePlayer = CardGamePlayer<PokerCard>

class CardGamePlayer<TypedCard: AbstractCard>(displayName: String): GamePlayer(displayName) {
    @JsonIgnore
    lateinit var hand: List<TypedCard>

    fun resetHand() {
        hand = listOf()
    }
}
package com.boardgames.scoring

import com.boardgames.models.cards.AbstractCard

interface ICardGameScoringStrategy<TypedCard : AbstractCard> {
    fun scoreHand(heldCards: Collection<TypedCard>, communityCards: Collection<TypedCard>): Int
}

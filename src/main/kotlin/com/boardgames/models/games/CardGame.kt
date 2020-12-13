package com.boardgames.models.games

import com.boardgames.models.CardDeck
import com.boardgames.models.CardGamePlayer
import com.boardgames.models.cards.AbstractCard
import com.boardgames.models.cards.PokerCard
import com.boardgames.models.teams.Team

abstract class CardGame<TypedCard : AbstractCard, TypedPlayer : CardGamePlayer<TypedCard>>(teams: List<Team>) : GameWithTeams(teams) {
    lateinit var deck: CardDeck<PokerCard>
    lateinit var communityCards: Collection<TypedCard>
    lateinit var dealer: TypedPlayer
}

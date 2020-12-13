package com.boardgames.models.games

import com.boardgames.models.PokerCardGamePlayer
import com.boardgames.models.cards.PokerCard
import com.boardgames.models.teams.Team
import java.util.*

typealias CribbageGamePlayer = PokerCardGamePlayer

class CribbageGame(
        teams: List<Team<CribbageGamePlayer>>
) : CardGame<PokerCard, CribbageGamePlayer>(teams) {
    var playedCardStack: LinkedList<PokerCard> = LinkedList()
    var total = 0
    var crib: List<PokerCard> = listOf()
}

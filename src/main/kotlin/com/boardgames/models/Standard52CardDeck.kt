package com.boardgames.models

import com.boardgames.PokerUtils.get52CardDeck
import com.boardgames.models.cards.PokerCard
import java.util.*

class Standard52CardDeck: CardDeck<PokerCard>(LinkedList(get52CardDeck()))
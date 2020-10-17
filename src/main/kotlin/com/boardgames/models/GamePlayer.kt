package com.boardgames.models

import com.boardgames.models.games.Game
import java.util.*

open class GamePlayer(displayName: String? = null) {
    val uuid: UUID = UUID.randomUUID()
    var displayName = displayName ?: "Player $uuid"
}

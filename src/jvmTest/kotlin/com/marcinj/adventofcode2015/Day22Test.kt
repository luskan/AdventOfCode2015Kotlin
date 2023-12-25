package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day22Test {

    @Test
    fun `First test`() {
        // For example, suppose the player has 10 hit points and 250 mana, and that the boss has 13 hit points and 8 damage:
        assertEquals(226, calculateMinManaRequired(10, 250, 13, 8, false).mana)
    }
}
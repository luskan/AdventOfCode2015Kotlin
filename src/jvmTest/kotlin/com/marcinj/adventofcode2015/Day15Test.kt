package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day15Test {

    val data = """
            Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
            Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
        """.trimIndent()

    @Test
    fun `part 1`() {
        assertEquals(62842880u, calculateBestCookiesRecipePart1(data))
    }

    @Test
    fun `part 2`() {
        assertEquals(57600000u, calculateBestCookiesRecipePart2(data))
    }
}
package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day18Test {

    val data = """
        .#.#.#
        ...##.
        #....#
        ..#...
        #.#..#
        ####..
    """.trimIndent()

    @Test
    fun `Test part 1`() {
        assertEquals(4, calculateNumberOfLightsOn(data, 4, false))
    }

    @Test
    fun `Test part 2`() {
        assertEquals(17, calculateNumberOfLightsOn(data, 5, part2 = true))
    }
}
package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day14Test {

    val data = """
            Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
            Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
        """.trimIndent()

    @Test
    fun `part 1`() {
        assertEquals(1120, calculateWinningReindeerTravelDistancePart1(data, 1000))
    }

    @Test
    fun `part 2`() {
        assertEquals(689, calculateWinningReindeerTravelDistancePart2(data, 1000))
    }
}
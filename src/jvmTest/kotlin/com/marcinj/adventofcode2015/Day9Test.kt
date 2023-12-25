package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day9Test {

    @Test
    fun `basic tests`() {
        val data = """
            London to Dublin = 464
            London to Belfast = 518
            Dublin to Belfast = 141
        """.trimIndent()

        assertEquals(605, calculateSantasShortestPathPart1(data))
        assertEquals(982, calculateSantasShortestPathPart2(data))
    }
}
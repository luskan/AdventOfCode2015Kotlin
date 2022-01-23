package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day24Test {

    val data = """
            1
            2
            3
            4
            5
            7
            8
            9
            10
            11
        """.trimIndent()

    @Test
    fun `test 1`() {
        assertEquals(99UL, calculateQuantumEntanglement(data))
    }

    @Test
    fun `test 2`() {
        assertEquals(44UL, calculateQuantumEntanglement(data, true))
    }
}
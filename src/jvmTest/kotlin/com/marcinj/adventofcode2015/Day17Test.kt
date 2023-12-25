package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Test {

    val data = """
        20
        15
        10
        5
        5
    """.trimIndent()

    @Test
    fun `Test part 1`() {
        assertEquals(4, calculateNumberOfContainers(data, 25).fittingCount)
    }

    @Test
    fun `Test2 part 1`() {
        assertEquals(4, calculateNumberOfContainersUsingDynamicProgramming(data, 25).fittingCount)
    }

    @Test
    fun `Test part 1 using dynamic programming`() {
        val data = """
        2
        4
        6
    """.trimIndent()

        assertEquals(1, calculateNumberOfContainersUsingDynamicProgramming(data, 10).fittingCount)
    }

    @Test
    fun `Test part 2`() {
        assertEquals(3, calculateNumberOfContainers(data, 25).minCount)
    }
}
package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day25Test {

    @Test
    fun `test 1`() {
        assertEquals(33511524UL, calculateSnowMachineCode(1, 6))
        assertEquals(27995004UL, calculateSnowMachineCode(6, 6))
    }

    @Test
    fun `test calculateIterationNum`() {
        assertEquals(15, calculateIterationNum(1, 5))
        assertEquals(18, calculateIterationNum(4, 3))
        assertEquals(16, calculateIterationNum(6, 1))
        assertEquals(21, calculateIterationNum(1, 6))
        assertEquals(5, calculateIterationNum(2, 2))
        assertEquals(1, calculateIterationNum(1, 1))
    }

}
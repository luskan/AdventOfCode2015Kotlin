package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day2Test {

    @Test
    fun `test 1`() = assertEquals(58, calculateAreaOfWrappingPaper("2x3x4"))

    @Test
    fun `test 2`() = assertEquals(43, calculateAreaOfWrappingPaper("1x1x10"))

    @Test
    fun `ribbon test 1`() = assertEquals(34, calculateLengthOfRibbon("2x3x4"))

    @Test
    fun `ribbon test 2`() = assertEquals(14, calculateLengthOfRibbon("1x1x10"))
}
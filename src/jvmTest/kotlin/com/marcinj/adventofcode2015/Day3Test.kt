package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day3Test {

    @Test
    fun `test 1`() = assertEquals(2, calculateVisitedHomes(">"))

    @Test
    fun `test 2`() = assertEquals(4, calculateVisitedHomes("^>v<"))

    @Test
    fun `test 3`() = assertEquals(2, calculateVisitedHomes("^v^v^v^v^v"))

    @Test
    fun `test 4`() = assertEquals(3, calculateVisitedHomesPart2("^v"))

    @Test
    fun `test 5`() = assertEquals(3, calculateVisitedHomesPart2("^>v<"))

    @Test
    fun `test 6`() = assertEquals(11, calculateVisitedHomesPart2("^v^v^v^v^v"))
}
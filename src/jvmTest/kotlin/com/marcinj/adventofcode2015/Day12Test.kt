package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {

    @Test
    fun `part 1`() {
        assertEquals(6, calculateSumOfAllNumbersPart1("[1,2,3]"))
        assertEquals(6, calculateSumOfAllNumbersPart1("""{"a":2,"b":4}"""))
        assertEquals(3, calculateSumOfAllNumbersPart1("""[[[3]]]"""))
        assertEquals(3, calculateSumOfAllNumbersPart1("""{"a":{"b":4},"c":-1}"""))
        assertEquals(0, calculateSumOfAllNumbersPart1("""{"a":[-1,1]}"""))
        assertEquals(0, calculateSumOfAllNumbersPart1("""[-1,{"a":1}]"""))
        assertEquals(0, calculateSumOfAllNumbersPart1("""[]"""))
        assertEquals(0, calculateSumOfAllNumbersPart1("""{}"""))
    }

    @Test
    fun `part 2`() {
        assertEquals(6, calculateSumOfAllNumbersPart2("[1,2,3]"))
        assertEquals(4, calculateSumOfAllNumbersPart2("""[1,{"c":"red","b":2},3]"""))
        assertEquals(0, calculateSumOfAllNumbersPart2("""{"d":"red","e":[1,2,3,4],"f":5}"""))
        assertEquals(6, calculateSumOfAllNumbersPart2("""[1,"red",5]"""))
    }
}
package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day5Test {

    @Test
    fun `test 1`() = assertEquals(1, calculateNiceWords("ugknbfddgicrmopn"))

    @Test
    fun `test 2`() = assertEquals(1, calculateNiceWords("aaa"))

    @Test
    fun `test 3`() = assertEquals(0, calculateNiceWords("jchzalrnumimnmhp"))

    @Test
    fun `test 4`() = assertEquals(0, calculateNiceWords("haegwjzuvuyypxyu"))

    @Test
    fun `test 5`() = assertEquals(0, calculateNiceWords("dvszwmarrgswjxmb"))


    @Test
    fun `test 6`() = assertEquals(1, calculateNiceWordsPart2("qjhvhtzxzqqjkmpb"))

    @Test
    fun `test 7`() = assertEquals(1, calculateNiceWordsPart2("xxyxx"))

    @Test
    fun `test 8`() = assertEquals(0, calculateNiceWordsPart2("uurcxstgmygtbstg"))

    @Test
    fun `test 9`() = assertEquals(0, calculateNiceWordsPart2("ieodomkazucvgmuy"))

    @Test
    fun `test 10`() = assertEquals(0, calculateNiceWordsPart2("aaa"))
}
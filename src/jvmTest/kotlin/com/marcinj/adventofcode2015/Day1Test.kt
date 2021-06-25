package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day1Test {

    @Test
    fun `test 1`() = assertEquals(0, calculateFloor("(())"))

    @Test
    fun `test 2`() = assertEquals(0, calculateFloor("()()"))

    @Test
    fun `test 3`() = assertEquals(3, calculateFloor("((("))

    @Test
    fun `test 4`() = assertEquals(3, calculateFloor("(()(()("))

    @Test
    fun `test 5`() = assertEquals(3, calculateFloor("))((((("))

    @Test
    fun `test 6`() = assertEquals(-1, calculateFloor("())"))


    @Test
    fun `test 7`() = assertEquals(-1, calculateFloor("))("))


    @Test
    fun `test 8`() = assertEquals(-3, calculateFloor(")))"))


    @Test
    fun `test 9`() = assertEquals(-3, calculateFloor(")())())"))

    @Test
    fun `test 10 part 2`() = assertEquals(1, calculateEnterBasementIndex(")"))

    @Test
    fun `test 11 part 2`() = assertEquals(5, calculateEnterBasementIndex("()())"))
}
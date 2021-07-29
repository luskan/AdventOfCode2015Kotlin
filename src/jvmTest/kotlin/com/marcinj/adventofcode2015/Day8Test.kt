package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.calculateSantasMemoryListSize
import com.marcinj.adventofcode2015.santaEscape
import kotlin.test.Test
import kotlin.test.assertEquals

class Day8Test {

    @Test
    fun `basic tests`() {
        assertEquals(0, calculateSantasMemoryListSize(""""""""))

        assertEquals(3, calculateSantasMemoryListSize(""""abc""""))

        assertEquals(7, calculateSantasMemoryListSize(""""aaa\"aaa""""))

        assertEquals(1, calculateSantasMemoryListSize(""""\x27""""))

        assertEquals(18, calculateSantasMemoryListSize(""""njro\x68qgbx\xe4af\"\\suan""""))

        assertEquals(27, calculateSantasMemoryListSize(""""\x22unqdlsrvgzfaohoffgxzfpir\"s""""))

        assertEquals(4, calculateSantasMemoryListSize(""""hey\\""""))
    }

    @Test
    fun `basic tests part2`() {
        assertEquals(""""\"\""""", """""""".santaEscape())
        assertEquals(""""\"abc\""""", """"abc"""".santaEscape())
        assertEquals(""""\"aaa\\\"aaa\""""", """"aaa\"aaa"""".santaEscape())
        assertEquals(""""\"\\x27\""""", """"\x27"""".santaEscape())
    }
}
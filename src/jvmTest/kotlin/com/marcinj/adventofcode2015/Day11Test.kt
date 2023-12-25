package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day11Test {

    @Test
    fun `basic tests`() {
        assertEquals(false, validatePassword(StringBuilder("hijklmmn")))
        assertEquals(false, validatePassword(StringBuilder("abbceffg")))
        assertEquals(true, validatePassword(StringBuilder("abcdffaa")))
        assertEquals(true, validatePassword(StringBuilder("ghjaabcc")))
        assertEquals("abcdffaa", calculateSantaNewPassword("abcdefgh"))
        assertEquals("ghjaabcc", calculateSantaNewPassword("ghijklmn"))
    }
}
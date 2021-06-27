package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day4Test {

    @Test
    fun `test 1`() = assertEquals(609043, calculateSecretSalt("abcdef"))

    @Test
    fun `test 2`() = assertEquals(1048970, calculateSecretSalt("pqrstuv"))
}
package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day16Test {

    val data = """
        Sue 499: cats: 6, children: 3, perfumes: 0
        Sue 500: pomeranians: 10, cats: 3, vizslas: 5
    """.trimIndent()

    @Test
    fun `Test parseData`() {
        val auntData = parseAuntData(data)
        assertEquals(true, auntData.containsKey(499))
        assertEquals(true, auntData.containsKey(500))
        assertEquals(false, auntData.containsKey(0))
        assertEquals(10, auntData[500]!!["pomeranians"])
        assertEquals(3, auntData[499]!!["children"])
    }
}
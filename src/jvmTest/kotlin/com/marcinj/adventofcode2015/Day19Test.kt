package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day19Test {

    val data = """
        H => HO
        H => OH
        O => HH
        
        HOH
    """.trimIndent()

    val data2 = """
        H => HO
        H => OH
        O => HH
        
        HOHOHO
    """.trimIndent()

    val dataPart2 = """
        e => H
        e => O
        H => HO
        H => OH
        O => HH
        
        HOH
    """.trimIndent()

    val data2Part2 = """
        e => H
        e => O
        H => HO
        H => OH
        O => HH
        
        HOHOHO
    """.trimIndent()

    @Test
    fun `Test part 1 load data`() {
        val medData = parseMedData(data)
        assertEquals("H", medData.replaceRules[0].from)
        assertEquals("HO", medData.replaceRules[0].to)
        assertEquals(3, medData.replaceRules.size)
        assertEquals("HOH", medData.medMolecule)
    }

    @Test
    fun `Test part 1 calculate`() {
        assertEquals(4, calculateNumberOfMolecules(data))
        assertEquals(7, calculateNumberOfMolecules(data2))
    }

    @Test
    fun `Test part 2 calculate`() {
        assertEquals(3, calculateNumberOfStepsBruteForce(dataPart2))
        assertEquals(6, calculateNumberOfStepsBruteForce(data2Part2))

        assertEquals(3, calculateNumberOfStepsTopDown(dataPart2))
        assertEquals(6, calculateNumberOfStepsTopDown(data2Part2))
    }

    @Test
    fun `Test part 2 calculate seq`() {
        assertEquals(3, calculateNumberOfStepsTopDownSeq(dataPart2, sortMedDataDesc = true))
        // This one does not work with this algorithm
        //assertEquals(6, calculateNumberOfStepsTopDownSeq(data2Part2))
    }

}
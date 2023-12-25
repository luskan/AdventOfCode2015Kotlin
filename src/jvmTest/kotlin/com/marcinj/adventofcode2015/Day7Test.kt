package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day7Test {

    @Test
    fun `parse data`() {
        assertEquals(parseCircuitData("af AND ah -> ai")[0], CircuitData(GateType.and, "af", "ah", "ai"))
        assertEquals(parseCircuitData("NOT lk -> ll")[0], CircuitData(GateType.not, "", "lk", "ll"))
        assertEquals(parseCircuitData("123 -> x")[0], CircuitData(GateType.assign, "", "123", "x"))
    }

    @Test
    fun `check calculate part1`() {
        val data = """
            123 -> x
            456 -> y
            x AND y -> d
            x OR y -> e
            x LSHIFT 2 -> f
            y RSHIFT 2 -> g
            NOT x -> h
            NOT y -> i
        """.trimIndent()
        assertEquals(72u, calculateCircuit(data, "d"))
        assertEquals(507u, calculateCircuit(data, "e"))
        assertEquals(492u, calculateCircuit(data, "f"))
        assertEquals(114u, calculateCircuit(data, "g"))
        assertEquals(65412u, calculateCircuit(data, "h"))
        assertEquals(65079u, calculateCircuit(data, "i"))
        assertEquals(123u, calculateCircuit(data, "x"))
        assertEquals(456u, calculateCircuit(data, "y"))
    }
}
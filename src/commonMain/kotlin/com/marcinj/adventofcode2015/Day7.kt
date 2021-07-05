package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.readAllText

// https://adventofcode.com/2015/day/7

enum class GateType {
    assign, and, or, lshift, rshift, not
}
data class CircuitData (
    val gateType: GateType, val op1: String, val op2: String, val result: String
)

fun parseCircuitData(data: String) : List<CircuitData> {
    val circuitDataArray = mutableListOf<CircuitData>()

    /* Parses lines like:
    af AND ah -> ai
    NOT lk -> ll
    hz RSHIFT 1 -> is
    NOT go -> gp
    */
    val rg = """([0-1a-z]*\s)?([A-Z]*)\s?([\d\w]+) -> ([\d\w]+)""".toRegex()
    data.lines().forEach {
        val m = rg.matchEntire(it) ?: fail("Unknown input : $it")
        val circuitData = CircuitData(
            when(m.groups[2]?.value ?: "") {
                "AND" -> GateType.and
                "OR" -> GateType.or
                "NOT" -> GateType.not
                "RSHIFT" -> GateType.rshift
                "LSHIFT" -> GateType.lshift
                "" -> GateType.assign
                else -> fail("Unknown gate! : ${m.groups[2]?.value} ")
            },
            m.groups[1]?.value?.trim() ?: "",
            m.groups[3]?.value?.trim() ?: "",
            m.groups[4]?.value?.trim() ?: "",
        )
        if (circuitData.op1.isBlank() && (circuitData.gateType != GateType.not && circuitData.gateType != GateType.assign))
            fail("NOT with op1 non empty is invalid")
        else if (circuitData.op1.isBlank() && circuitData.gateType != GateType.not && circuitData.gateType != GateType.assign || circuitData.op2.isBlank())
            fail("Empty op1 or op2 is invalid for non NOT op")
        circuitDataArray.add(circuitData)
    }
    return circuitDataArray
}

fun calculateValueForCircuitData(
    resultToGateMap: HashMap<String, CircuitData>,
    cache: HashMap<String, UShort>,
    circuitData: CircuitData
) : UShort {
    var v1: UShort? = circuitData.op1.toUShortOrNull()
    if (v1 == null && !circuitData.op1.isBlank()) {
        check(resultToGateMap.containsKey(circuitData.op1))
        if (cache.containsKey(circuitData.op1)) {
            v1 = cache[circuitData.op1]
        }
        else {
            v1 = calculateValueForCircuitData(resultToGateMap, cache, resultToGateMap[circuitData.op1]!!)
            cache[circuitData.op1] = v1
        }
    }
    var v2: UShort? = circuitData.op2.toUShortOrNull()
    if (v2 == null && !circuitData.op2.isBlank()) {
        check(resultToGateMap.containsKey(circuitData.op2))
        if (cache.containsKey(circuitData.op2)) {
            v2 = cache[circuitData.op2]
        }
        else {
            v2 = calculateValueForCircuitData(resultToGateMap, cache, resultToGateMap[circuitData.op2]!!)
            cache[circuitData.op2] = v2
        }
    }

    return when(circuitData.gateType) {
        GateType.assign -> v2!!
        GateType.and -> v1!! and v2!!
        GateType.or -> v1!! or v2!!
        GateType.lshift -> (v1!!.toUInt() shl v2!!.toInt()).toUShort()
        GateType.rshift -> (v1!!.toUInt() shr v2!!.toInt()).toUShort()
        GateType.not -> v2!!.inv()
    }
}

fun calculateCircuit(data: String, wire: String) : UShort {
    val parsedData = parseCircuitData(data)

    val resultToGateMap = hashMapOf<String, CircuitData>()
    parsedData.forEach {
        check(!resultToGateMap.containsKey(it.result))
        resultToGateMap[it.result] = it
    }

    val cache = hashMapOf<String, UShort>()
    return calculateValueForCircuitData(resultToGateMap, cache, resultToGateMap[wire]!!)
}

fun calculateCircuitPart2(data: String) : UShort {
    val parsedData = parseCircuitData(data)

    val resultToGateMap = hashMapOf<String, CircuitData>()
    parsedData.forEach {
        check(!resultToGateMap.containsKey(it.result))
        resultToGateMap[it.result] = it
    }

    var cache = hashMapOf<String, UShort>()
    var wireA = calculateValueForCircuitData(resultToGateMap, cache, resultToGateMap["a"]!!)
    cache = hashMapOf()
    cache["b"] = wireA
    wireA = calculateValueForCircuitData(resultToGateMap, cache, resultToGateMap["a"]!!)

    return wireA
}

fun runday7() {
    val data = readAllText("/day7.txt")
    println(" --- Day7 ---")
    println("result for part 1: ${calculateCircuit(data, "a")}") //956
    println("result for part 2: ${calculateCircuitPart2(data)}") //40149
}


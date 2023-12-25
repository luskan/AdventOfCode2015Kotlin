package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText

// https://adventofcode.com/2015/day/7

enum class GateType {
    assign, and, or, lshift, rshift, not
}
data class CircuitData (
    val gateType: GateType, val op1: String, val op2: String, val result: String
) {
    fun op(ind: Int) = when (ind) {
        0 -> op1
        1 -> op2
        else -> fail("Only index 0 and 1 is allowed")
    }
}

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
    if (cache.containsKey(circuitData.result))
        return cache[circuitData.result]!!

    val v = arrayOf<UShort>(0u, 0u)
    for (i in 0..1) {
        var vop = circuitData.op(i).toUShortOrNull()
        if (vop == null && circuitData.op(i).isNotBlank()) {
           vop = calculateValueForCircuitData(resultToGateMap, cache, resultToGateMap[circuitData.op(i)]!!)
           cache[circuitData.op(i)] = vop
        }
        v[i] = vop ?: 0u
    }

    return when(circuitData.gateType) {
        GateType.assign -> v[1]
        GateType.and -> v[0] and v[1]
        GateType.or -> v[0] or v[1]
        GateType.lshift -> (v[0].toUInt() shl v[1].toInt()).toUShort()
        GateType.rshift -> (v[0].toUInt() shr v[1].toInt()).toUShort()
        GateType.not -> v[1].inv()
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
    println("result for part 1: ${verifyResult(getIntFromFile("/day7_result.txt", 0).toUShort(), calculateCircuit(data, "a"))}")
    println("result for part 2: ${verifyResult(getIntFromFile("/day7_result.txt", 1).toUShort(), calculateCircuitPart2(data))}")
}


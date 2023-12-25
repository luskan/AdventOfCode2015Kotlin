package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText

// https://adventofcode.com/2015/day/6

enum class LampOp {on, off, toggle}
data class Point(val x: Int, val y: Int)
data class Rect(val leftTop: Point, val rightBottom: Point) {
    fun isIn(point: Point): Boolean {
        return point.x >= leftTop.x && point.x <= rightBottom.x &&
                point.y >= leftTop.y && point.y <= rightBottom.y
    }
}

data class LampOpData(val op: LampOp, val rt: Rect)

fun parseLampData(data: String) : List<LampOpData> {
    // turn off 12,823 through 102,934
    val rg = """(\w+\s?\w+) (\d+),(\d+) through (\d+),(\d+)""".toRegex()
    val lampOps: MutableList<LampOpData> = mutableListOf()
    data.lines().forEach {
        val m = rg.matchEntire(it) ?: fail("bad data?")
        lampOps.add(
            LampOpData(
                when(m?.groups?.get(1)?.value?.toString() ?: ""){
                    "turn on" -> LampOp.on
                    "turn off" -> LampOp.off
                    "toggle" -> LampOp.toggle
                    else -> fail("Unknown lamp op")
                },
                Rect(Point(
                    m?.groups?.get(2)?.value?.toInt() ?: fail("Unknown lamp op"),
                    m?.groups?.get(3)?.value?.toInt() ?: fail("Unknown lamp op")),
                    Point(
                        m?.groups?.get(4)?.value?.toInt() ?: fail("Unknown lamp op"),
                        m?.groups?.get(5)?.value?.toInt() ?: fail("Unknown lamp op"))
                )
            )
        )
    }
    return lampOps
}

internal fun calculateLamps(data: String): Int {
    var count = 0
    val parsedData = parseLampData(data)
    for (x in 0..999) {
        for (y in 0..999) {
            var state = false
            parsedData.forEach {
                if (it.rt.isIn(Point(x, y))) {
                    state = when(it.op) {
                        LampOp.on -> true
                        LampOp.off -> false
                        LampOp.toggle -> !state
                    }
                }
            }
            if (state)
                count++
        }
    }
    return count
}

fun calculateLampsPart2(data: String): Int {
    var count = 0
    val parsedData = parseLampData(data)
    for (x in 0..999) {
        for (y in 0..999) {
            var state = 0
            parsedData.forEach {
                if (it.rt.isIn(Point(x, y))) {
                    state = when(it.op) {
                        LampOp.on -> state+1
                        LampOp.off -> state-1
                        LampOp.toggle -> state+2
                    }
                    if (state < 0)
                        state = 0
                }
            }
            count+=state
        }
    }
    return count
}

fun runday6() {
    val data = readAllText("/day6.txt")
    println(" --- Day6 ---")
    println("result for part 1: ${verifyResult(getIntFromFile("/day6_result.txt", 0), calculateLamps(data))}")
    println("result for part 2: ${verifyResult(getIntFromFile("/day6_result.txt", 1), calculateLampsPart2(data))}")
}


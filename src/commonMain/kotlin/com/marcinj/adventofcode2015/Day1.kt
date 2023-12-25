package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText

// https://adventofcode.com/2015/day/1

internal fun calculateFloor(data: String) : Int {
    var floor = 0
    data.forEach {
        floor +=
            when (it) {
                '(' -> 1
                ')' -> -1
                else -> fail("Expected ( and ) only")
            }
    }
    return floor
}

internal fun calculateEnterBasementIndex(data: String) : Int {
    var floor = 0
    data.forEachIndexed() lit@{ index, it ->
        floor +=
            when (it) {
                '(' -> 1
                ')' -> -1
                else -> fail("Expected ( and ) only")
            }
        if (floor == -1)
            return index + 1
    }
    return 0
}

fun runday1() {
    val data = readAllText("/day1.txt")
    println(" --- Day1 ---")
    println("result for part 1: ${verifyResult(getIntFromFile("/day1_result.txt", 0), calculateFloor(data))}")
    println("result for part 2: ${verifyResult(getIntFromFile("/day1_result.txt", 1), calculateEnterBasementIndex(data))}")
}
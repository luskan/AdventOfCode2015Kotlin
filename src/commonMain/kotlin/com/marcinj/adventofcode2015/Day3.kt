package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText

// https://adventofcode.com/2015/day/3

data class Pos(val x: Int, val y: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Pos

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}

internal fun calculateVisitedHomes(data: String): Int {
    val homes = hashMapOf<Pos, Int>()
    var pos = Pos(0,0)
    homes[pos] = 1
    data.forEach {
        pos = when(it) {
            '^' -> Pos(pos.x, pos.y+1)
            'v' -> Pos(pos.x, pos.y-1)
            '<' -> Pos(pos.x-1, pos.y)
            '>' -> Pos(pos.x+1, pos.y)
            else -> fail("")
        }
        homes[pos] = (homes.get(pos)?:0) + 1
    }
    return homes.size
}

internal fun calculateVisitedHomesPart2(data: String): Int {
    val homes = hashMapOf<Pos, Int>()
    var posSanta = Pos(0,0)
    var posRoboSanta = Pos(0,0)
    homes[posSanta] = 2
    data.forEachIndexed() { ind, it ->
        var pos = if (ind % 2 == 0) posSanta else posRoboSanta
        pos = when(it) {
            '^' -> Pos(pos.x, pos.y+1)
            'v' -> Pos(pos.x, pos.y-1)
            '<' -> Pos(pos.x-1, pos.y)
            '>' -> Pos(pos.x+1, pos.y)
            else -> fail("")
        }
        if (ind % 2 == 0)
            posSanta = pos
        else
            posRoboSanta = pos
        homes[pos] = (homes.get(pos)?:0) + 1
    }
    return homes.size
}

fun runday3() {
    val data = readAllText("/day3.txt")
    println(" --- Day3 ---")
    println("result for part 1: ${verifyResult(
        getIntFromFile("/day3_result.txt", 0), 
        calculateVisitedHomes(data))}")
    println("result for part 2: ${verifyResult(
        getIntFromFile("/day3_result.txt", 1), 
        calculateVisitedHomesPart2(data))}")
}


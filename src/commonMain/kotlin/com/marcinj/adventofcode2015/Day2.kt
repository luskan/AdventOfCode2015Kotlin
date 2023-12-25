package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText

// https://adventofcode.com/2015/day/2

data class Dim(val l: Int, val w: Int, val h: Int)

class ParseDim {
    private val rg = """(\d+)x(\d+)x(\d+)""".toRegex()
    fun parse(data: String) : Dim {
        val m = rg.matchEntire(data)
        val l = m?.groups?.get(1)?.value?.toInt() ?: 0
        val w = m?.groups?.get(2)?.value?.toInt() ?: 0
        val h = m?.groups?.get(3)?.value?.toInt() ?: 0
        return Dim(l, w, h)
    }
}

internal fun calculateAreaOfWrappingPaper(data: String): Int {
    val pd = ParseDim()
    return data.lines().fold(0) { acc, it ->
        val (l, w, h) = pd.parse(it)
        acc + 2 * l * w + 2 * w * h + 2 * h * l + minOf(l * w, w * h, h * l)
    }
}

internal fun calculateLengthOfRibbon(data: String): Int {
    val pd = ParseDim()
    return data.lines().fold(0) { acc, it ->
        val (l, w, h) = pd.parse(it)
        val dms = arrayOf(l, w, h).sortedArray()
        acc + dms[0]*2 + dms[1]*2 + l * w * h
    }
}

fun runday2() {
    val data = readAllText("/day2.txt")
    println(" --- Day2 ---")
    println("result for part 1: ${verifyResult(
        getIntFromFile("/day2_result.txt", 0)
        , calculateAreaOfWrappingPaper(data))}")
    println("result for part 2: ${verifyResult(
        getIntFromFile("/day2_result.txt", 1), 
        calculateLengthOfRibbon(data))}")
}


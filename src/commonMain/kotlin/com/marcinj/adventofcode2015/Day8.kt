package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText

// https://adventofcode.com/2015/day/8

fun String.santaUnescape() : String {
    if (length < 2) return this
    var s : String = this.subSequence(1, length - 1).toString()
    s = s.replace("\\\\", "\\")
    s = s.replace("\\\"", "\"")
    s = s.replace("\\\'", "\'")
    val rg = """\\[xX]([A-Fa-f0-9]{2})""".toRegex()
    s = rg.replace(s) { mr -> "" + (mr.groups[1]?.value?.toInt(16)?.toChar() ?: fail("how?")) }
    return s
}

fun String.santaEscape() : String {
    var s = this.replace("\\", "\\\\")
    s = s.replace("\"", "\\\"")
    return "\"" + s + "\""
}

fun calculateSantasMemoryListSize(data: String) = data.lines().fold(0) { acc, s -> acc + s.santaUnescape().length }

fun calculateSantasMemoryListSizePart1(data: String) =
    data.lines().fold(0) { acc, s -> s.length + acc} - calculateSantasMemoryListSize(data)

fun calculateSantasMemoryListSizePart2(data: String): Int {
    return data.lines().fold(0) { acc, s -> s.santaEscape().length - s.length + acc}
}

fun runday8() {
    val data = readAllText("/day8.txt")
    println(" --- Day8 ---")
    println("result for part 1: ${verifyResult(
        getIntFromFile("/day8_result.txt", 0)
        , calculateSantasMemoryListSizePart1(data))}")
    println("result for part 2: ${verifyResult(
        getIntFromFile("/day8_result.txt", 1), 
        calculateSantasMemoryListSizePart2(data))}")
}




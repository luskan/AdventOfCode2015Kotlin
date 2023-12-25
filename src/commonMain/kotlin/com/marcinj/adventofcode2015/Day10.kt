package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText

// https://adventofcode.com/2015/day/10

fun calculateElvesLookAndSayGamePart1Slow(data: String, iterations: Int) : String {
    var curData = data
    val rg = "(?<=(.))(?!\\1)".toRegex()
    for (iter in 1 until iterations) {
        val split = curData.split(rg)
        curData = ""
        split.forEach {
            if (it.isNotEmpty())
                curData += "${it.length}${it[0]}"
        }
    }

    return curData
}

fun calculateElvesLookAndSayGamePart1(data: String, iterations: Int) : String {
    var newData = StringBuilder()
    var curData = StringBuilder(data)
    for (iter in 1..iterations) {
        var i = 0
        newData.setLength(0)
        while(i < curData.length) {
            val start = i
            var end = i
            while (end < curData.length) {
                if (curData[start] != curData[end]) {
                    break
                }
                end++
            }
            newData.append((end-start).toString())
            newData.append(curData[start])
            i = end
        }
        curData = newData.also{newData = curData}
    }

    return curData.toString()
}

fun runday10() {
    println(" --- Day10 ---")
    val data = readAllText("/day10.txt")
    println("result for part 1: ${verifyResult(getIntFromFile("/day10_result.txt", 0), calculateElvesLookAndSayGamePart1(data, 40).length)}")
    println("result for part 2: ${verifyResult(getIntFromFile("/day10_result.txt", 1), calculateElvesLookAndSayGamePart1(data, 50).length)}")
}




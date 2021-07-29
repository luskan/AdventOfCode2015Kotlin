package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.readAllText

// https://adventofcode.com/2015/day/15

fun runday15() {
    val data = readAllText("/day15.txt")
    println(" --- Day15 ---")
    println("result for part 1: ${calculateWinningReindeerTravelDistancePart1(data, 2503)}")
    println("result for part 2: ${calculateWinningReindeerTravelDistancePart2(data, 2503)}")
}




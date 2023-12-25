package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText
import kotlin.time.ExperimentalTime

// https://adventofcode.com/2015/day/9

data class InData(val from: String, val to: String, val distance: Int)

fun parseSantasShortestPathsData(data: String) : MutableList<InData> {

    // Faerun to Tristram = 65
    val rg = """(\w+) to (\w+) = (\d+)""".toRegex()

    val result = mutableListOf<InData>()
    data.lines().forEach { line ->
        rg.matchEntire(line)?.let {
            result.add(InData(it.groups[1]?.value!!, it.groups[2]?.value!!, it.groups[3]?.value?.toInt()!!))
        }
    }
    return result
}

fun calculateSantasShortestPath(data: String, shortest: Boolean) : Int {
    val inData = parseSantasShortestPathsData(data)

    val cityList = mutableListOf<String>()
    val cityToDistances = HashMap<String, MutableList<InData>>()
    inData.forEach {
        cityToDistances.getOrPut(it.from) { mutableListOf() }.add(it)
        cityToDistances.getOrPut(it.to) { mutableListOf() }.add(it)
    }
    cityToDistances.forEach { cityList.add(it.key) }

    val perms = permutations(cityList)

    var bestDistance = if (shortest) Int.MAX_VALUE else 0
    perms.forEachIndexed { index, permList ->
        var prev: String? = null

        var distance = 0
        permList.forEach {
            if (prev == null) {
                prev = it
            }
            else {
                cityToDistances.get(prev)?.let{ coll ->
                    val toCity = coll.filter { ind -> ind.to == it || ind.from == it }
                    if (toCity.size != 1)
                        fail("Should be exactly 1!")
                    if (!(toCity[0].from == prev && toCity[0].to == it) &&
                        !(toCity[0].from == it && toCity[0].to == prev))
                        fail("Something wrong!")
                    distance += toCity[0].distance
                } ?: fail("Should exist!")
                prev = it
            }
        }
        if (distance < bestDistance && shortest || distance > bestDistance && !shortest) {
            bestDistance = distance
        }
    }

    return bestDistance
}

fun calculateSantasShortestPathPart1(data: String) : Int {
    return calculateSantasShortestPath(data, true)
}

fun calculateSantasShortestPathPart2(data: String) : Int {
    return calculateSantasShortestPath(data, false)
}

fun runday9() {
    val data = readAllText("/day9.txt")
    println(" --- Day9 ---")
    println("result for part 1: ${verifyResult(
        getIntFromFile("/day9_result.txt", 0)
        , calculateSantasShortestPathPart1(data))}")
    println("result for part 2: ${verifyResult(
        getIntFromFile("/day9_result.txt", 1)
        , calculateSantasShortestPathPart2(data))}")
}




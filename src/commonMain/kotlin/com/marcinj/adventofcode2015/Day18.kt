package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText

private data class LightElement(private var _isOn: Boolean, var isStuck: Boolean = false) {
    var isOn: Boolean
        get() = if(isStuck) true else _isOn
        set(value) { _isOn = value }
}
private typealias LightRow = ArrayList<LightElement>
private typealias LightArray = ArrayList<LightRow>

private fun parseLightsMapData(data: String, part2: Boolean) : LightArray {
    val lightArray = LightArray()
    for (row in data.lines()) {
        val lightRow = LightRow()
        row.forEach {  light -> lightRow.add(LightElement(when (light) { '#' -> true else -> false})) }
        lightArray.add(lightRow)
    }

    if (part2) {
        lightArray[0][0].isStuck = true
        lightArray[0].last().isStuck = true
        lightArray.last()[0].isStuck = true
        lightArray.last().last().isStuck = true
    }

    return lightArray
}

private data class NeighbourCounts(var on: Int = 0, var off: Int = 0)
private fun findNeighbours(lightArray: LightArray, x: Int, y: Int, counts: NeighbourCounts) {
    counts.on = 0
    counts.off = 0

    //upper row
    if (y-1>=0) {
        for (i in x-1..x+1) {
            if (i < 0 || i >= lightArray[0].size)
                counts.off++
            else if (lightArray[y-1][i].isOn)
                counts.on++
            else
                counts.off++
        }
    }
    else {
        counts.off+=3
    }

    //bottom row
    if (y+1<lightArray.size) {
        for (i in x-1..x+1) {
            if (i < 0 || i >= lightArray[0].size)
                counts.off++
            else if (lightArray[y+1][i].isOn)
                counts.on++
            else
                counts.off++
        }
    }
    else {
        counts.off+=3
    }

    // left
    if (x-1 >= 0) {
        if (lightArray[y][x-1].isOn)
            counts.on++
        else
            counts.off++
    }
    else
        counts.off++

    // right
    if (x+1 < lightArray[0].size) {
        if (lightArray[y][x+1].isOn)
            counts.on++
        else
            counts.off++
    }
    else
        counts.off++
}

fun calculateNumberOfLightsOn(data: String, steps: Int, part2: Boolean): Int {
    var oldLightsData = parseLightsMapData(data, part2)
    var currentLightsData = oldLightsData.map { it.map { it.copy() } as LightRow } as LightArray
    val neighbourCounts = NeighbourCounts()
    for (step in 0 until steps) {
        oldLightsData.forEachIndexed { y, row ->
            row.forEachIndexed { x, lightElement ->
                findNeighbours(oldLightsData, x, y, neighbourCounts)
                if (lightElement.isOn)
                    currentLightsData[y][x].isOn = neighbourCounts.on == 2 || neighbourCounts.on == 3
                else
                    currentLightsData[y][x].isOn = neighbourCounts.on == 3
            }
        }
        // ugly kotlin like swap
        currentLightsData = oldLightsData.also { oldLightsData = currentLightsData }
    }

    val res = oldLightsData.fold(0) { sum, row -> row.fold(sum) { subSum, light -> subSum + if (light.isOn) 1 else 0} }
    return res
}

fun runday18() {
    val data = readAllText("/day18.txt")
    println(" --- Day18 ---")
    println("result for part 1: ${verifyResult(getIntFromFile("/day18_result.txt", 0)
        , calculateNumberOfLightsOn(data, 100, false))}")
    println("result for part 2: ${verifyResult(getIntFromFile("/day18_result.txt", 1)
        , calculateNumberOfLightsOn(data, 100, true))}")
}


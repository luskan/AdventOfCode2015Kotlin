package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.readAllText

private data class LightElement(var isOn: Boolean)
private typealias LightRow = ArrayList<LightElement>
private typealias LightArray = ArrayList<LightRow>

private fun parseLightsMapData(data: String) : LightArray {
    val lightArray = LightArray()
    for (row in data.lines()) {
        val lightRow = LightRow()
        row.forEach {  light -> lightRow.add(LightElement(when (light) { '#' -> true else -> false})) }
        lightArray.add(lightRow)
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
    var oldLightsData = parseLightsMapData(data)
    var currentLightsData = oldLightsData.map { it.map { it.copy() } as LightRow } as LightArray
    val neighbourCounts = NeighbourCounts()
    for (step in 0 until steps) {
        oldLightsData.forEachIndexed { y, row ->
            row.forEachIndexed { x, lightElement ->
                if (part2 && (
                    x == 0 && y == 0 ||
                    x == oldLightsData[0].size - 1 && y == 0 ||
                    x == 0 && y == oldLightsData.size - 1 ||
                    x == oldLightsData[0].size - 1 && y == oldLightsData.size - 1)) {
                    currentLightsData[y][x].isOn = true
                }
                else {
                    findNeighbours(oldLightsData, x, y, neighbourCounts)
                    if (lightElement.isOn)
                        currentLightsData[y][x].isOn = neighbourCounts.on == 2 || neighbourCounts.on == 3
                    else
                        currentLightsData[y][x].isOn = neighbourCounts.on == 3
                }
            }
        }
        // ugly kotlin like swap
        currentLightsData = oldLightsData.also { oldLightsData = currentLightsData }
    }

    val res = oldLightsData.fold(0) { sum, row -> row.fold(sum) { sum, light -> sum + if (light.isOn) 1 else 0} }
    return res
}

fun runday18() {
    val data = readAllText("/day18.txt")
    println(" --- Day18 ---")
    println("result for part 1: ${calculateNumberOfLightsOn(data, 100, true)}")
    //println("result for part 2: ${verifyResult(18, calculateNumberOfContainers(data, 150).minCount)}")
}


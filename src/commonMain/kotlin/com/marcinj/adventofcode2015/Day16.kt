package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText

typealias PropertyMap = HashMap<String, Int>
typealias AuntsProperties = HashMap<Int, PropertyMap>

fun parseAuntData(data: String): AuntsProperties {
    val result = AuntsProperties()
    // Sue 1: cars: 9, akitas: 3, goldfish: 0
    val rg = """Sue (\d+): (\w+): (\d+), (\w+): (\d+), (\w+): (\d+)""".toRegex()
    data.lines().forEach {
        val (sueId, prop1, prop1Count, prop2, prop2Count, prop3, prop3Count) = rg.matchEntire(it)!!.destructured
        val props = PropertyMap()
        props[prop1] = prop1Count.toInt()
        props[prop2] = prop2Count.toInt()
        props[prop3] = prop3Count.toInt()
        result[sueId.toInt()] = props
    }
    return result
}

fun calculateWhichAuntSuePart(data: String, part2: Boolean): Int {
    val auntData = parseAuntData(data)

    val scanData =
        mapOf("children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1)

    data class AuntScore(val id: Int, var score: Int)
    val scoreList = mutableListOf<AuntScore>()

    auntData.forEach { (key, value) ->
        val auntScore = AuntScore(key, 0)
        scoreList.add(auntScore)
        value.forEach { (pk, pv) ->
            if (!part2) {
                if (scanData[pk] == pv)
                    auntScore.score++
            }
            else {
                when (pk) {
                    "trees", "cats" -> {
                        if (scanData[pk]!! < pv)
                            auntScore.score++
                    }
                    "pomeranians", "goldfish" -> {
                        if (scanData[pk]!! > pv)
                            auntScore.score++
                    }
                    else ->
                        if (scanData[pk] == pv)
                            auntScore.score++
                }
            }
        }
    }

    val best = scoreList.filter { it.score == 3 }
    check(best.size == 1)
    return best[0].id
}

fun runday16() {
    val data = readAllText("/day16.txt")
    println(" --- Day16 ---")
    println("result for part 1: ${verifyResult(
        getIntFromFile("/day16_result.txt", 0)
        , calculateWhichAuntSuePart(data, false))}")
    println("result for part 2: ${verifyResult(        
        getIntFromFile("/day16_result.txt", 1)
        , calculateWhichAuntSuePart(data, true))}")
}

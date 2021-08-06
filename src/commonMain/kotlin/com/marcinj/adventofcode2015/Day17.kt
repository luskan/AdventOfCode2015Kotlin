package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.readAllText
import kotlin.math.pow

data class EggNogContainersResult(val fittingCount: Int, val minCount: Int)
fun calculateNumberOfContainers(data: String, eggNogLiters: Int): EggNogContainersResult {
    val containerSizes = mutableListOf<Int>()
    data.lines().forEach { containerSizes.add(it.toInt()) }
    var foundCorrect = 0
    val maxSubSets = (2.0).pow(containerSizes.size).toUInt()
    val minimumContainerSizes = mutableListOf<Pair<Int,UInt>>()
    for (n in 0U until maxSubSets) {
        var total = 0
        var countOfOnes = 0
        for (k in 0 until containerSizes.size) {
            val mask: UInt = 1U shl k
            if ((mask and n) != 0U) {
                countOfOnes++
                total += containerSizes[k]
            }
        }
        if (total == eggNogLiters) {
            foundCorrect++

            if (minimumContainerSizes.isNotEmpty()) {
                if (countOfOnes < minimumContainerSizes[0].first) {
                    minimumContainerSizes.clear()
                    minimumContainerSizes.add(Pair(countOfOnes, n))
                }
                else if (countOfOnes == minimumContainerSizes[0].first)
                    minimumContainerSizes.add(Pair(countOfOnes, n))
            }
            else
                minimumContainerSizes.add(Pair(countOfOnes, n))
        }
    }
    return EggNogContainersResult(foundCorrect, minimumContainerSizes.size)
}

fun runday17() {
    val data = readAllText("/day17.txt")
    println(" --- Day17 ---")
    println("result for part 1: ${verifyResult(1304, calculateNumberOfContainers(data, 150).fittingCount)}")
    println("result for part 2: ${verifyResult(18, calculateNumberOfContainers(data, 150).minCount)}")
}

package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText
import kotlin.math.pow

data class EggNogContainersResult(val fittingCount: Int, val minCount: Int)

/**
 * Brute-force method, iterates oll subsets
 */
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

/**
 * Based on https://www.reddit.com/r/adventofcode/comments/3x6cyr/day_17_solutions/cy27nwr?utm_source=share&utm_medium=web2x&context=3
 *
 * For input: 2 4 6
 * Three containers of sized 2, 4 and 6. sums 2d array will contain:
 *
 *   i/j    0    1    2    3
 *   0:     1    0    0    0
 *   1:     0    0    0    0
 *   2:     0    1    0    0
 *   3:     0    0    0    0
 *   4:     0    1    0    0
 *   5:     0    0    0    0
 *   6:     0    1    1    0
 *   7:     0    0    0    0
 *   8:     0    0    1    0
 *   9:     0    0    0    0
 *  10:     0    0    1    0
 *
 * Each column indexes with j, is the number of containers. Column with j == 0, means no containers, and column with
 * j == 2 means two containers were used. Its not known which actuall containers.
 * Each row indexed with i, is the number of liters of eggnog. Rows represents actually the containers sizes, so at
 * sums[i == 2][j == 1] we have 1, because there is exactly one way to fill 2 liters of eggnog using above three
 * containers - with use of container 2. sums[i == 6][j == 2] is 1 because there are exactly one way to fill 6 liters
 * of eggnog using above containers - with use of 2 and 4 containers.
 *
 * The results will be visible at the last row, they will be summed for the first part answer. For second part answer,
 * we will need to find first non zero element of last row, this will represent minimal number of containers allowing
 * to contain all the eggnog.
 */
fun calculateNumberOfContainersUsingDynamicProgramming(data: String, eggNogLiters: Int): EggNogContainersResult {
    val containerSizes = mutableListOf<Int>()
    data.lines().forEach { containerSizes.add(it.toInt()) }




    val sums = mutableListOf<ArrayList<Int>>()

    for (i in 0 until eggNogLiters+1) {
        val innerList: ArrayList<Int> = ArrayList<Int>()
        for (k in 0..containerSizes.size) {
            innerList.add(0)
        }
        sums.add(innerList)
    }

    sums[0][0] = 1

    for (c in containerSizes) {
        for (i in eggNogLiters - c downTo 0 ) {
            for ( j in 0 until containerSizes.size) {
                sums[i + c][j + 1] += sums[i][j]
            }
        }

        //printMemArray(c, sums)
    }

    var foundCorrect = 0
    for (c in sums.last())
        foundCorrect+=c

    var minCount = sums.last().find find@{ return@find it != 0 }!!

    return EggNogContainersResult(foundCorrect, minCount)
}

private fun printMemArray(
    c: Int,
    sums: MutableList<ArrayList<Int>>
) {
    println("$c :------------+------------+------------+------------+------------+------------+------------+")
    print("     ")
    for (i in sums[0].indices)
        print(i.toString().padStart(5, ' '))
    println()
    for (i in sums.indices) {
        print(i.toString().padStart(3, ' ') + ": ")
        for (j in sums[i].indices) {
            print(sums[i][j].toString().padStart(5, ' '))
        }
        println()
    }
}


fun runday17() {
    val data = readAllText("/day17.txt")
    println(" --- Day17 ---")
    println("result for part 1: ${verifyResult(
        getIntFromFile("/day17_result.txt", 0)
        , calculateNumberOfContainers(data, 150).fittingCount)}")
    println("result for part 1: ${verifyResult(getIntFromFile("/day17_result.txt", 0), calculateNumberOfContainersUsingDynamicProgramming(data, 150).fittingCount)}")
    println("result for part 2: ${verifyResult(getIntFromFile("/day17_result.txt", 1), calculateNumberOfContainers(data, 150).minCount)}")
    println("result for part 2: ${verifyResult(getIntFromFile("/day17_result.txt", 1), calculateNumberOfContainersUsingDynamicProgramming(data, 150).minCount)}")
}


package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.getStringFromFile
import com.marcinj.adventofcode2015.tools.readAllText

data class BestData(var gr1QE: ULong, var gr1Size: Int )
data class GroupResult(var stats: BestData, var groups:MutableList<MutableSet<Int>>)

fun calculateBestSolution(packagesList: MutableList<Int>,
                          groups: MutableList<MutableSet<Int>>,
                          groupWeight: Int,
                          correctGroups: MutableList<GroupResult>,
                          depth: Int,
                          bestData: BestData,
                          cache: HashSet<MutableList<Int>>): Int
{
    if (packagesList in cache) {
        val sum = groups[0].sum()
        if (sum == groupWeight && groups[1].size == 0)
            return -1
    }
    else {
        cache.add(packagesList.toMutableList())
    }

    for (value in packagesList) {
        for (k in 0 until (groups.size)) {
            val grSum = groups[k].sum()
            var doBreak = false
            if (grSum < groupWeight) {
                if (grSum + value <= groupWeight) {

                    // If 0 group is not yet filled, but it has more items than the best choosen
                    // set of items for this groupd, then we dont need to check this combination of items
                    // as it will be worse than the best one.
                    if (grSum + value != groupWeight && k == 0 && groups[0].size == bestData.gr1Size)
                        return 0

                    groups[k].add(value)

                    // We dont fill all the groups
                    //if (k == groups.size-2)
                    if (packagesList.size == 1)
                    {
                        val finalGroups = mutableListOf<MutableSet<Int>>()
                        groups.forEach { finalGroups.add(it.toMutableSet()) }
                        val grBestData = BestData(groups[0].fold(1UL){ acc, v -> acc * v.toULong()}, groups[0].size)
                        val gr = GroupResult(grBestData, finalGroups)
                        var allowCalculateAllGroups = false
                        if (!correctGroups.contains(gr)) {
                            correctGroups.add(gr)

                            bestData.gr1Size = grBestData.gr1Size
                            bestData.gr1QE = grBestData.gr1QE
                            allowCalculateAllGroups = true
                        }
                        if (!allowCalculateAllGroups) {
                            groups[k].remove(value)
                            return 0
                        }
                    }

                    val newPackagesList = packagesList.toMutableList()
                    newPackagesList.remove(value)
                    val res = calculateBestSolution(newPackagesList, groups, groupWeight, correctGroups, depth + 1, bestData, cache)
                    if (res == 0) {
                        if (k > 0) {
                            groups[k].remove(value)
                            return 0
                        }
                    }
                    if (res == -1 && depth > 0) {
                        groups[k].remove(value)
                        return -1
                    }

                    groups[k].remove(value)
                }
                doBreak = true
            }

            // Fast abort if group 1 is worse than any previous ones.
            if (k == 0) {
                if (groups[k].size > bestData.gr1Size) {
                    return 0
                }
                else {
                    val qe = groups[k].fold(1UL){ acc, v -> acc * v.toULong()}
                    if (qe > bestData.gr1QE)
                        return 0
                }
            }
            if (doBreak)
                break

        }
    }
    return 1
}

fun calculateQuantumEntanglement(data: String, part2: Boolean = false): ULong {
    val packagesList = data.lines().map { it.toInt() }.sortedByDescending { it }
    val weightsSum = packagesList.fold(0){ acc, i -> acc + i }
    val groupWeight = weightsSum / (if (part2) 4 else 3)

    val groups = MutableList((if (part2) 4 else 3)) { index -> mutableSetOf<Int>() }
    val finalGroups = mutableListOf<GroupResult>()
    val cache = HashSet<MutableList<Int>>()
    calculateBestSolution(packagesList.toMutableList(), groups, groupWeight,
        finalGroups, 0, BestData(ULong.MAX_VALUE, Int.MAX_VALUE), cache)

    finalGroups.sortWith(compareBy<GroupResult> { it.groups[0].size }.thenBy { it.stats.gr1QE })

    return finalGroups[0].stats.gr1QE
}

fun runday24() {
    println(" --- Day24 ---")

    val data = readAllText("/day24.txt")
   println("result for part 1: ${verifyResult(
       getStringFromFile("/day24_result.txt", 0, 1).toULong()
       , calculateQuantumEntanglement(data))}")
   println("result for part 2: ${verifyResult(
       getStringFromFile("/day24_result.txt", 1, 2).toULong()
       , calculateQuantumEntanglement(data, true))}")
}

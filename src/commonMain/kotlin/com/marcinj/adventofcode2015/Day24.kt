package com.marcinj.adventofcode2015

data class BestData(var gr1QE: ULong, var gr1Size: Int )
data class GroupResult(var stats: BestData, var groups:MutableList<MutableSet<Int>>)
fun calculateBestSolution(packagesList: MutableList<Int>, groups: MutableList<MutableSet<Int>>, groupWeight: Int, correctGroups: MutableList<GroupResult>, depth: Int, bestData: BestData): Boolean {
    for (i in packagesList.indices) {
        val value = packagesList[i]
        if (value < 0)
            continue

        for (k in 0 until groups.size) {
            val grSum = groups[k].sum()
            var doBreak = false
            if (grSum < groupWeight) {
                if (grSum + value <= groupWeight) {

                    if (grSum + value != groupWeight && k == 0 && groups[0].size == bestData.gr1Size) {
                        return false
                    }

                    groups[k].add(value)
                    packagesList[i] = -packagesList[i]

                    if ((packagesList.firstOrNull { it >= 0 } ?: 0) == 0) {
                        val finalGroups = mutableListOf<MutableSet<Int>>()
                        groups.forEach { finalGroups.add(it.toMutableSet()) }
                        val grBestData = BestData(groups[0].fold(1UL){ acc, v -> acc * v.toULong()}, groups[0].size)
                        val gr = GroupResult(grBestData, finalGroups)
                        if (!correctGroups.contains(gr)) {
                            correctGroups.add(gr)

                            bestData.gr1Size = grBestData.gr1Size
                            bestData.gr1QE = grBestData.gr1QE

                            // It appears that first result is the correct one. This is because the input
                            // packages list is sorted descending - this way promoting bigger packages in santa place
                            // giving this way the best QE.
                            //return false
                        }
                        groups[k].remove(value)
                        packagesList[i] = -packagesList[i]
                        return false
                    }
                    else {
                        if (!calculateBestSolution(packagesList, groups, groupWeight, correctGroups, depth + 1, bestData)) {
                            if (k > 0) {
                                groups[k].remove(value)
                                packagesList[i] = -packagesList[i]
                                return false
                            }
                        }
                    }
                    packagesList[i] = -packagesList[i]
                    groups[k].remove(value)
                }
                doBreak = true
            }
            // Fast abort if group 1 is worse than any previous ones.
            if (k == 0 /* && grSum == groupWeight*/) {
                if (groups[k].size > bestData.gr1Size) {
                    return false
                }
                else {
                    val qe = groups[k].fold(1UL){ acc, v -> acc * v.toULong()}
                    if (qe > bestData.gr1QE)
                        return false
                }
            }
            if (doBreak)
                break

        }
    }
    return true
}

fun calculateQuantumEntanglement(data: String, part2: Boolean = false): ULong {
    val packagesList = data.lines().map { it.toInt() }.sortedByDescending { it }
    val weightsSum = packagesList.fold(0){ acc, i -> acc + i }
    val groupWeight = weightsSum / (if (part2) 4 else 3)

    val groups = MutableList((if (part2) 4 else 3)) { index -> mutableSetOf<Int>() }
    val finalGroups = mutableListOf<GroupResult>()
    calculateBestSolution(packagesList.toMutableList(), groups, groupWeight, finalGroups, 0, BestData(ULong.MAX_VALUE, Int.MAX_VALUE))

    finalGroups.sortWith(compareBy<GroupResult> { it.groups[0].size }.thenBy { it.stats.gr1QE })

    return finalGroups[0].stats.gr1QE
}

fun runday24() {
    println(" --- Day24 ---")

    val data = """
        1
        3
        5
        11
        13
        17
        19
        23
        29
        31
        41
        43
        47
        53
        59
        61
        67
        71
        73
        79
        83
        89
        97
        101
        103
        107
        109
        113
    """.trimIndent()
   println("result for part 1: ${verifyResult(11266889531UL, calculateQuantumEntanglement(data))}")
   println("result for part 2: ${verifyResult(77387711UL, calculateQuantumEntanglement(data, true))}")
}

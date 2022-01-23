package com.marcinj.adventofcode2015

data class BestData(var gr1QE: ULong, var gr1Size: Int )
data class GroupResult(var stats: BestData, var groups:MutableList<MutableSet<Int>>)

fun calculateBestSolution(packagesList: MutableSet<Int>,
                          groups: MutableList<MutableSet<Int>>,
                          groupWeight: Int,
                          correctGroups: MutableList<GroupResult>,
                          depth: Int,
                          bestData: BestData,
                          cache: HashSet<MutableSet<Int>>): Int
{
    if (packagesList in cache)
        return -1
    else
        cache.add(packagesList.toMutableSet())

    for (value in packagesList) {
        for (k in 0 until (groups.size-1)) {
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

                    // We dont fill all the groups, we leave the last one as it shoulr
                    if (k == groups.size-2)
                    //if (packagesList.size == 1)
                    {
                        val finalGroups = mutableListOf<MutableSet<Int>>()
                        groups.forEach { finalGroups.add(it.toMutableSet()) }
                        val grBestData = BestData(groups[0].fold(1UL){ acc, v -> acc * v.toULong()}, groups[0].size)
                        val gr = GroupResult(grBestData, finalGroups)
                        if (!correctGroups.contains(gr)) {
                            correctGroups.add(gr)

                            bestData.gr1Size = grBestData.gr1Size
                            bestData.gr1QE = grBestData.gr1QE
                        }
                        groups[k].remove(value)
                        return 0
                    }
                    else {
                        val newPackagesList = packagesList.toMutableSet()
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
                    }
                    groups[k].remove(value)
                }
                doBreak = true
            }
            // Fast abort if group 1 is worse than any previous ones.
            if (k == 0 /* && grSum == groupWeight*/) {
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
    calculateBestSolution(packagesList.toMutableSet(), groups, groupWeight,
        finalGroups, 0, BestData(ULong.MAX_VALUE, Int.MAX_VALUE), HashSet())

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

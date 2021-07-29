package com.marcinj.adventofcode2015

// Slow version, too many allocations
fun <T> permutationsSlow(list: List<T>): Set<List<T>> {
    if (list.isEmpty()) return setOf(emptyList())

    val result: MutableSet<List<T>> = mutableSetOf()
    for (i in list.indices) {
        permutations(list - list[i]).forEach{
                item -> result.add(item + list[i])
        }
    }
    return result
}

private fun <T> permutationsHeapRecursiveInternal(k: Int, list: MutableList<T>, outList: MutableList<List<T>>) {
    if (k == 1) {
        outList.add(List<T>(list.size) {list[it]})
    }
    else {
        permutationsHeapRecursiveInternal(k - 1, list, outList)
        for (i in 0 until k-1) {
            if (k % 2 == 0)
                list[i] = list[k-1].also{ list[k-1] = list[i] }
            else
                list[0] = list[k-1].also{ list[k-1] = list[0] }
            permutationsHeapRecursiveInternal(k - 1, list, outList)
        }
    }
}

fun <T> permutationsHeapRecursive(list: List<T>): List<List<T>> {
    val result = mutableListOf<List<T>>()
    if (list.isNotEmpty()) {
        val tempList = MutableList<T>(list.size) { i -> list[i] }
        permutationsHeapRecursiveInternal(tempList.size, tempList, result)
    }
    return result
}

/**
 * Heap algorithm to find permutation, non-recursive version
 */
fun <T> permutations(list: List<T>): List<List<T>> {
    val result = mutableListOf<List<T>>()
    val c = Array(list.size) {0}
    result.add(list.toList())

    val tempList = list.toMutableList()
    var i = 1
    while (i < list.size) {
        if (c[i] < i) {
            if (i % 2 == 0)
                tempList[0] = tempList[i].also { tempList[i] = tempList[0] }
            else
                tempList[c[i]] = tempList[i].also { tempList[i] = tempList[c[i]] }
            result.add(tempList.toList())
            c[i] += 1
            i = 1
        }
        else {
            c[i] = 0
            i += 1
        }
    }
    return result
}

fun <T> verifyResult(expected: T, actual: T) : T {
    check( expected == actual, {"Expected: $expected but got $actual"})
    return actual
}
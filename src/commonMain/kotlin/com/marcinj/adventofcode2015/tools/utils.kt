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

/***
 * @param values and @maxValues must be of same length
 */
data class SetPermute(val values:MutableList<Int>, val minValues:List<Int>, val maxValues:List<Int>) {
    init {
        if (minValues.size != maxValues.size)
            throw IllegalArgumentException()
    }
}

/***
 * Given @param setPermute, function computes next value.
 * Values are being incremented in each field according to maxValues.
 * So setPermute.values[0] will be incremented in each call untill setPermute.maxValues[0] will be reached.
 * Then setPermute.values[0] will be set to setPermute.minValues[0] and setPermute.values[1] will be incremented,
 * etc...
 *
 * setPermute.values can be non initialized, nextPermutation will add new values as needed - in such case first call
 * will set initial setPermute.minvalues to first field, or it can contain
 * values initialized to setPermute.minValues[0] ...
 *
 * @return true if more permutations are possible, otherwise false is returned
 */
fun nextPermutation(setPermute: SetPermute): Boolean {
    if (setPermute.values.size == 0) {
        setPermute.values.addAll(setPermute.minValues)
        return true
    }

    if (setPermute.values.equals(setPermute.maxValues))
        return false

    for (n in setPermute.values.size-1 downTo 0) {
        if (setPermute.values[n] == setPermute.maxValues[n]) {
            setPermute.values[n] = setPermute.minValues[n]
            if (n == 0) {
                val minValIndex = setPermute.values.size
                setPermute.values.add(0, setPermute.minValues[minValIndex] + 1)
            }
            //else {
            //    setPermute.values[n - 1]++
            //}
            //break
        }
        else {
            setPermute.values[n]++
            break
        }
    }

    return true
}

fun <T> verifyResult(expected: T, actual: T) : T {
    check( expected == actual, {"Expected: $expected but got $actual"})
    return actual
}
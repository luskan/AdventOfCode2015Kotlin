package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText

// https://adventofcode.com/2015/day/5

private fun isNiceWord(word: String) : Boolean {
    //It contains at least three vowels (aeiou only)
    if (word.filter { it in "aeiou" }.count() < 3)
        return false;

    //It contains at least one letter that appears twice in a row
    var found = false
    for (i in 1 until word.length) {
        if (word[i] == word[i-1]) {
            found = true
            break
        }
    }
    if (!found)
        return false

    //It does not contain the strings ab, cd, pq, or xy, even if they are part of one of the other requirements.
    arrayOf("ab", "cd", "pq", "xy").forEach { if (it in word) return false; }

    return true
}

private fun isNiceWordPart2(word: String) : Boolean {
    //It contains a pair of any two letters that appears at least twice in the string without overlapping
    var found = false
    p2@for (i in 0 until word.length - 1) {
        val c1 = word[i]
        val c2 = word[i+1]
        for (n in i + 2 until word.length-1 ) {
            if (word[n] == c1 && word[n+1] == c2) {
                found = true
                break@p2
            }
        }
    }
    if (!found)
        return false

    //It contains at least one letter which repeats with exactly one letter between them,
    // like xyx, abcdefeghi (efe), or even aaa.
    for (i in 1 until word.length - 1) {
        if (word[i - 1] == word[i + 1]) {
            return true
        }
    }

    return false
}

internal fun calculateNiceWords(data: String): Int {
    var count = 0
    data.lines().forEach {
        if (isNiceWord(it))
            count++
    }
    return count
}

internal fun calculateNiceWordsPart2(data: String): Int {
    var count = 0
    data.lines().forEach {
        if (isNiceWordPart2(it))
            count++
    }
    return count
}

fun runday5() {
    val data = readAllText("/day5.txt")
    println(" --- Day5 ---")
    println("result for part 1: ${verifyResult(getIntFromFile("/day5_result.txt", 0), calculateNiceWords(data))}")
    println("result for part 2: ${verifyResult(getIntFromFile("/day5_result.txt", 1), calculateNiceWordsPart2(data))}")
}


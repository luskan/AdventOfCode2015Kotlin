package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.*
import com.marcinj.adventofcode2015.tools.md5HashInit

// https://adventofcode.com/2015/day/4

internal fun calculateSecretSalt(data: String): Int {
    val md5Data = md5HashInit()
    nextNum@for (n in 0..Int.MAX_VALUE) {
        val md5Bytes = md5HashInBytes(md5Data, data + n)
        //val md5Str = md5HashInBytesToStr(md5Bytes)
        if (md5Bytes[0].toInt() != 0 || md5Bytes[1].toInt() != 0 || md5Bytes[2].toUInt() > 15.toUInt()) {
            continue@nextNum
        }
        return n
    }
    return 0
}

internal fun calculateSecretSaltPart2(data: String): Int {
    val md5Data = md5HashInit()
    nextNum@for (n in 0..Int.MAX_VALUE) {
        val md5Bytes = md5HashInBytes(md5Data, data + n)
        if (md5Bytes[0].toInt() != 0 || md5Bytes[1].toInt() != 0 || md5Bytes[2].toInt() != 0) {
            continue@nextNum
        }
        return n
    }
    return 0
}

fun runday4() {
    println(" --- Day4 ---")
    val data = readAllText("/day4.txt")
    println("result for part 1: ${verifyResult(
        getIntFromFile("/day4_result.txt", 0)
        , calculateSecretSalt(data))}")
    println("result for part 2: ${verifyResult(
        getIntFromFile("/day4_result.txt", 1)
        , calculateSecretSaltPart2(data))}")
}


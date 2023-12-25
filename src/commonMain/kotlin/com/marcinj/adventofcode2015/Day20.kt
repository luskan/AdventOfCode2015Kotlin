package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.getIntFromFile

private fun getDivisors(number: Int, divisors: MutableList<Int>) {
    for (i in (kotlin.math.sqrt(number.toFloat())).toInt() downTo  1) {
        if (number % i == 0) {
            if (number / i == i) {
                divisors.add(i)
            }
            else {
                divisors.add(i)
                divisors.add(number/i)
            }
        }
    }
}

fun calculateLowestHouseNumber(maxPresents: Int, part2: Boolean): Int {

    val foldLambda: (Int,Int,Int)->Int  =
        if (part2)
            {acc, n, num -> acc + if (num/n <= 50) n*11 else 0 }
        else
            {acc, n, _ -> acc + n*10 }

    var num = 1
    val divisors = mutableListOf<Int>()

    while(true) {
        divisors.clear()
        getDivisors(num, divisors)
        val presents = divisors.fold(0) {acc, n -> foldLambda(acc, n, num) }
        if (presents >= maxPresents)
            return num
        num++
    }
}

fun runday20() {
    println(" --- Day20 ---")
    val data = getIntFromFile("/day20.txt", 0)
    println("result for part 1: ${verifyResult(getIntFromFile("/day20_result.txt", 0), calculateLowestHouseNumber(data, false))}")
    println("result for part 2: ${verifyResult(getIntFromFile("/day20_result.txt", 1), calculateLowestHouseNumber(data, true))}")
}





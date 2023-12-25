package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getStringFromFile
import com.marcinj.adventofcode2015.tools.readAllText

/*
Calculate a number at the given row;col:

   | 1   2   3   4   5   6
---+---+---+---+---+---+---+
 1 |  1   3   6  10  15  21
 2 |  2   5   9  14  20
 3 |  4   8  13  19
 4 |  7  12  18
 5 | 11  17
 6 | 16

1. first we will calculate which diagonal the row;col is at. This is a diag1=row+col-1.
2. then to calculate last number at the and of last diagonal, we step back to previous diagonal and calculate its
value at the row;col=(1, diag1-1). This will be equal to sum of arithmetic sequence (actuall of columns): num1=(1 + (diag1-1))/2*(diag1-1)
3. We will add the additional number of sequence cells in the diag1 diagonal, which is equal to value of col

so the formula is:

 diag1=row+col-1
 num1=(1 + (diag1-1))/2*(diag1-1)
 result=num1+col

 ex: for r;c = 4;3
 diag1=4+3-1=6
 num1=(1+(6-1))/2*(6-1)=(1+5)/2*5=15
 result=15+3=18
*/
fun calculateIterationNum(row: Int, col: Int): Int {
    val diag1 = row + col - 1
    val num1 = ((1 + (diag1-1)) * (diag1 - 1)) / 2
    val result = num1 + col
    return result
}

fun calculateSnowMachineCode(row: Int, col: Int): ULong {
    val seq = calculateIterationNum(row, col)
    val codeOne = 20151125UL
    val mulVal = 252533UL
    val divVal = 33554393UL
    var lastVal = codeOne
    repeat(seq - 1) {
        val newVal = (lastVal * mulVal) % divVal
        lastVal = newVal
    }
    return lastVal
}

fun runday25() {
    println(" --- Day25 ---")

    val data = readAllText("/day25.txt")
    val rg = """^.+?(\d+).+?(\d+).+$""".toRegex()
    val m = rg.matchEntire(data)
    val (row, column) = m?.destructured ?: fail("Wrong input!")
    println("result for part 1: ${verifyResult(
        getStringFromFile("/day25_result.txt", 0, 1).toULong()
        , calculateSnowMachineCode(row.toInt(), column.toInt()))}")
}

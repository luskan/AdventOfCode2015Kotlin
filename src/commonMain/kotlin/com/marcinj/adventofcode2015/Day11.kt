package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.getStringFromFile
import com.marcinj.adventofcode2015.tools.readAllText

// https://adventofcode.com/2015/day/11

private fun incrementPassword(password: StringBuilder) {
    for (i in password.length - 1 downTo 0) {
        val c = password[i]
        if (c == 'z') {
            password[i] = 'a'
            if (i == 0) {
                password.insert(0, 'a')
                break
            }
        }
        else {
            password[i]++
            break
        }
    }
}

fun validatePassword(password: StringBuilder) : Boolean {
    var valid = false
    for (k in 0 until password.length - 3) {
        if (password[k]+1 == password[k+1] && password[k+1]+1 == password[k+2]) {
            valid = true
            break
        }
    }
    if (!valid)
        return false

    if (password.contains('i', ignoreCase = true))
        return false
    if (password.contains('o', ignoreCase = true))
        return false
    if (password.contains('l', ignoreCase = true))
        return false

    // check of non overlapping double letters like aa bb cc etc., at least two such
    valid = false
    var doublesCount = 0
    var k = 0
    var letterUsed = '?'
    whileLoop@while (k < password.length - 1) {
        if (password[k] == password[k+1]) {
            if (letterUsed == '?') {
                letterUsed = password[k]
            }
            else {
                if (letterUsed == password[k]) {
                    k++
                    continue@whileLoop
                }
            }
            doublesCount++
            k+=2
            if (doublesCount == 2) {
                valid = true
                break
            }
        }
        else {
            k++
        }
    }
    if (!valid)
        return false
    return true
}

fun calculateSantaNewPassword(data: String) : String {
    val password = StringBuilder(data)

    do {
        incrementPassword(password)
    } while(!validatePassword(password))

    return password.toString()
}

fun runday11() {
    println(" --- Day11 ---")
    var data = getStringFromFile("/day11.txt", 0, 1)
    println("result for part 1: ${verifyResult(getStringFromFile("/day11_result.txt", 0, 1), calculateSantaNewPassword(data))}") //cqjxxyzz
    data = getStringFromFile("/day11.txt", 1, 2)
    println("result for part 2: ${verifyResult(getStringFromFile("/day11_result.txt", 1, 2), calculateSantaNewPassword(data))}") //cqkaabcc
}




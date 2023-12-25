package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText

// https://adventofcode.com/2015/day/13

enum class GainOrLose {gain, loose}
data class GuestData(val name: String, val gainOrLose: GainOrLose, val amount: Int, val name2: String) {
    fun score() =
        when(gainOrLose) {
            GainOrLose.gain -> amount
            GainOrLose.loose -> -amount
        }
}

fun parseGuestData(data: String) : MutableList<GuestData> {
    val parsedData = mutableListOf<GuestData>()
    // Alice would gain 54 happiness units by sitting next to Bob.
    val rg = """
        (?<name>\w+) would (?<gainloose>gain|lose) (?<amount>\d+) happiness units by sitting next to (?<name2>\w+).
    """.trimIndent().toRegex()

    data.lines().forEach {
        val matchEntire = rg.matchEntire(it)
        if (matchEntire == null)
            fail("parse error!")
        val (name, gainloose, amount, name2) = matchEntire!!.destructured
        parsedData.add(GuestData(
            name,
            when (gainloose) {
                "gain" -> GainOrLose.gain
                "lose" -> GainOrLose.loose
                else -> fail("error!")
            },
            amount.toInt(),
            name2))
    }

    return parsedData
}

fun calculateOptimalGuestArrangementTotalHappines(guestData: List<GuestData>) : Int {
    val guestNamesToData = calculateGuestDataMap(guestData)
    val guestNames = calculateGuestNamesList(guestNamesToData)
    val permutationList = permutations(guestNames)

    var bestHappiness : Int = 0
    var bestArrangement : List<String>
    permutationList.forEach {
        var total = 0
        for (i in 0..it.size-1) {
            val leftGuest = it[if (i-1 < 0) it.size-1 else i-1]
            val rightGuest = it[if (i+1 >= it.size) 0 else i+1]
            total += guestNamesToData[it[i]]!!.get(leftGuest)!!.score()
            total += guestNamesToData[it[i]]!!.get(rightGuest)!!.score()
        }
        if (total > bestHappiness) {
            bestHappiness = total
            bestArrangement = it
        }
    }

    return bestHappiness
}

private fun calculateGuestNamesList(guestNamesToData: HashMap<String, HashMap<String, GuestData>>): MutableList<String> {
    val guestNames = mutableListOf<String>()
    guestNamesToData.forEach { guestNames.add(it.key) }
    return guestNames
}

private fun calculateGuestDataMap(guestData: List<GuestData>): HashMap<String, HashMap<String, GuestData>> {
    val guestNamesToData = hashMapOf<String, HashMap<String, GuestData>>()
    guestData.forEach {
        check(!guestNamesToData.containsKey(it.name) || !guestNamesToData[it.name]!!.containsKey(it.name2))
        guestNamesToData.getOrPut(it.name) { hashMapOf() }.getOrPut(it.name2) { it }
    }
    return guestNamesToData
}

fun calculateOptimalGuestArrangementTotalHappinesPart1(data: String) =
    calculateOptimalGuestArrangementTotalHappines(parseGuestData(data))

fun calculateOptimalGuestArrangementTotalHappinesPart2(data: String) : Int {
    val parsedData = parseGuestData(data)

    val guestNamesToData = calculateGuestDataMap(parsedData)
    val guestNames = calculateGuestNamesList(guestNamesToData)

    guestNames.forEach { it1 ->
        parsedData.add(GuestData(it1, GainOrLose.gain, 0, "Martin"))
    }
    guestNames.forEach { it1 ->
        parsedData.add(GuestData("Martin", GainOrLose.gain, 0, it1))
    }

    return calculateOptimalGuestArrangementTotalHappines(parsedData)
}

fun runday13() {
    val data = readAllText("/day13.txt")
    println(" --- Day13 ---")
    println("result for part 1: ${verifyResult(
        getIntFromFile("/day13_result.txt", 0)
        , calculateOptimalGuestArrangementTotalHappinesPart1(data))}")
    println("result for part 2: ${verifyResult(
        getIntFromFile("/day13_result.txt", 1)
        , calculateOptimalGuestArrangementTotalHappinesPart2(data))}")
}




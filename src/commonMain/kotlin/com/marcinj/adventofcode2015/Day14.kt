package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText

// https://adventofcode.com/2015/day/14

data class ReindeerRacerData(val name: String, val speed: Int, val travelTime: Int, val restTime: Int)

fun parseReindeerRaceData(data: String) : List<ReindeerRacerData> {
    val res = mutableListOf<ReindeerRacerData>()
    //          Comet can fly 14    km/s for 10    seconds, but then must rest for 127 seconds.
    val rg = """(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds.""".toRegex()
    data.lines().forEach {
        val m = rg.matchEntire(it) ?: fail("parse error! : $it")
        val (name, speed, travelTime, restTime) = m.destructured
        res.add(ReindeerRacerData(name, speed.toInt(), travelTime.toInt(), restTime.toInt()))
    }
    return res
}

private enum class RaceState { running, resting }

private data class ReindeerRacer(val reindeer: ReindeerRacerData,
                         var currentDistanceInKm: Int = 0,
                         var currentTravelTimeInSeconds: Int = 0,
                         var currentRestTimeInSeconds: Int = 0,
                         var state: RaceState = RaceState.running,
                         var score: Int = 0
)

private fun calculateWinningReindeer(data: String, raceDurationInSeconds: Int, byScore: Boolean = false): ReindeerRacer {
    val parsedData = parseReindeerRaceData(data)

    val raceData = MutableList(parsedData.size) {i -> ReindeerRacer(parsedData[i])}

    for (sec in 0 until raceDurationInSeconds) {
        raceData.forEach { racer ->
            if (racer.state == RaceState.running) {
                racer.currentTravelTimeInSeconds++
                racer.currentDistanceInKm += racer.reindeer.speed
            }
            else if (racer.state == RaceState.resting) {
                racer.currentRestTimeInSeconds++
            }

            if (racer.state == RaceState.running) {
                if (racer.currentTravelTimeInSeconds == racer.reindeer.travelTime) {
                    racer.state = RaceState.resting
                    racer.currentTravelTimeInSeconds = 0
                    racer.currentRestTimeInSeconds = 0
                }
            }
            else if (racer.state == RaceState.resting) {
                if (racer.currentRestTimeInSeconds == racer.reindeer.restTime) {
                    racer.state = RaceState.running
                    racer.currentRestTimeInSeconds = 0
                }
            }
        }

        val winnerForSecond = raceData.maxByOrNull { it.currentDistanceInKm } ?: fail("error!")
        raceData
            .forEach {
                if (it.currentDistanceInKm == winnerForSecond.currentDistanceInKm)
                    it.score++
            }
    }

    val winner = raceData.maxByOrNull { if (byScore) it.score else it.currentDistanceInKm }
    return winner ?: fail("error!")
}

fun calculateWinningReindeerTravelDistancePart1(data: String, raceDurationInSeconds: Int): Int {
    return calculateWinningReindeer(data, raceDurationInSeconds).currentDistanceInKm
}

fun calculateWinningReindeerTravelDistancePart2(data: String, raceDurationInSeconds: Int): Int {
    return calculateWinningReindeer(data, raceDurationInSeconds, true).score
}

fun runday14() {
    val data = readAllText("/day14.txt")
    println(" --- Day14 ---")
    println("result for part 1: ${verifyResult(
        getIntFromFile("/day14_result.txt", 0)
        , calculateWinningReindeerTravelDistancePart1(data, 2503))}")
    println("result for part 2: ${verifyResult(
        getIntFromFile("/day14_result.txt", 1)
        , calculateWinningReindeerTravelDistancePart2(data, 2503))}")
}




package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText

// https://adventofcode.com/2015/day/15

data class Ingridients(val name: String, val capacity: Long, val durability: Long, val flavor: Long, val texture: Long, val calories: Long)
{
    fun countingIngridient(index: Int) =
        when(index) {
            0 -> capacity
            1 -> durability
            2 -> flavor
            3 -> texture
            else -> fail("error!")
        }

    companion object {
        fun countingIngridientSize() = 4
    }
}
//Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
//Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
private fun parseCookiesIngridientsData(data: String) : List<Ingridients> {
    val result = mutableListOf<Ingridients>()
    val rg = """(\w+): capacity ([-\d]+), durability ([-\d]+), flavor ([-\d]+), texture ([-\d]+), calories ([-\d]+)""".toRegex()
    data.lines().forEach {
        rg.matchEntire(it)!!.let { mr ->
            val (name, capacity, durability, flavor, texture, calories) = mr.destructured
            val ing = Ingridients(
                name, capacity.toLong(), durability.toLong(),
                flavor.toLong(), texture.toLong(), calories.toLong())
            result.add(ing)
        }
    }
    return result
}

data class BestScore(var value: ULong)

/**
 * Bruteforce recursive solution, it iterates each possible ingridient count for each ingridient. It makes sure all counts always
 * adds up to 100.
 */
private fun getNextPacking(ingridients: List<Ingridients>, counts: Array<Long>, index: Int, maxValue: Long,
                                   bestScore: BestScore, expectedCalories: Long = -1) {
    if (index == counts.size) {
        var total = 1UL
        check(100L == counts.fold(0L) { sum,it->sum + it})

        if (expectedCalories != -1L) {
            var totalCalories = 0L
            for (ing in 0 until ingridients.size) {
                totalCalories += counts[ing] * ingridients[ing].calories
            }
            if (totalCalories != expectedCalories)
                return
        }

        for (k in 0 until Ingridients.countingIngridientSize()) {
            var sum = 0L
            for (ing in 0 until ingridients.size) {
                sum += counts[ing] * ingridients[ing].countingIngridient(k)
            }
            if (sum < 0)
                sum = 0
            total *= sum.toULong()
        }
        if (total > bestScore.value)
            bestScore.value = total
        return
    }
    val minValue =
        if(index==counts.size-1)
            maxValue
        else
            1
    for (k in minValue .. maxValue) {
        counts[index] = k
        getNextPacking(ingridients, counts, index + 1,  maxValue - k, bestScore, expectedCalories)
    }
}

fun calculateBestCookiesRecipePart1(data: String) : ULong {
    val ingredients = parseCookiesIngridientsData(data)
    val counts = Array<Long>(ingredients.size) { if (it == 0) 100L else 1L }
    val bestScore = BestScore(0UL)
    getNextPacking(ingredients, counts, 0,  100L, bestScore)
    return bestScore.value
}

fun calculateBestCookiesRecipePart2(data: String) : ULong {
    val ingredients = parseCookiesIngridientsData(data)
    val counts = Array<Long>(ingredients.size) { if (it == 0) 100L else 1L }
    val bestScore = BestScore(0UL)
    getNextPacking(ingredients, counts, 0,  100L, bestScore, 500)
    return bestScore.value
}

fun runday15() {
    val data = readAllText("/day15.txt")
    println(" --- Day15 ---")
    println("result for part 1: ${verifyResult(
        getIntFromFile("/day15_result.txt", 0).toULong()
        , calculateBestCookiesRecipePart1(data))}")
    println("result for part 2: ${verifyResult(
        getIntFromFile("/day15_result.txt", 1).toULong()
        , calculateBestCookiesRecipePart2(data))}")
}

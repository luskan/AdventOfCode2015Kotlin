package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText
import kotlinx.serialization.json.*

// https://adventofcode.com/2015/day/12

fun calculateSumOfAllNumbersPart1(data: String) : Int {
    var sum = 0
    val rg = """([-\d.]+)""".toRegex()
    rg.findAll(data).forEach {
        val num = it.groups[1]?.value ?: fail("error!")
        sum += num.toInt()
    }
    return sum
}

fun calculateSumUsingJsonParser(jsonElement: JsonElement) : Int {
    var sum = 0
    if (jsonElement is JsonArray) {
        val jsonArray = jsonElement.jsonArray
        for (elem in jsonArray) {
            sum += calculateSumUsingJsonParser(elem)
        }
    }
    else if (jsonElement is JsonObject) {
        val jsonObject = jsonElement.jsonObject
        if (!jsonObject.containsValue(JsonPrimitive("red"))) {
            for ((_, value) in jsonObject) {
                sum += calculateSumUsingJsonParser(value)
            }
        }
    }
    else if (jsonElement is JsonPrimitive) {
        try {
            sum += jsonElement.toString().toInt()
        } catch (e: Exception) {}
    }

    return sum
}

fun calculateSumOfAllNumbersPart2(data: String) : Int {
    val jsElem = Json.parseToJsonElement(data)
    return calculateSumUsingJsonParser(jsElem)
}

fun runday12() {
    val data = readAllText("/day12.txt")
    println(" --- Day12 ---")
    println("result for part 1: ${verifyResult(
        getIntFromFile("/day12_result.txt", 0)
        , calculateSumOfAllNumbersPart1(data))}")
    println("result for part 2: ${verifyResult(
        getIntFromFile("/day12_result.txt", 1)
        , calculateSumOfAllNumbersPart2(data))}")
}




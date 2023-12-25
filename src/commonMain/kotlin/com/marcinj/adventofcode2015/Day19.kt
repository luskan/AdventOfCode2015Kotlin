package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText

data class ReplaceRule(val from: String, val to: String)
data class MedData(var replaceRules: List<ReplaceRule>, val medMolecule: String)

fun parseMedData(data: String): MedData  {
    val rules = mutableListOf<ReplaceRule>()
    var molecule = ""

    val rg = """(\w+) => (\w+)""".toRegex()
    data.lines().forEach lin@{
        if (it.isEmpty())
            return@lin
        val matchEntire = rg.matchEntire(it)
        if (matchEntire != null) {
            val (from, to) = matchEntire.destructured
            rules.add(ReplaceRule(from, to))
        } else {
            molecule = it
        }
    }

    return MedData(rules, molecule)
}

fun calculateNumberOfMolecules(data: String): Int {
    val medData = parseMedData(data)

    val newMolecule = StringBuilder()
    val molecules = hashSetOf<String>()
    medData.replaceRules.forEach { (from, to) ->
        var ind = 0
        while(true) {
            ind = medData.medMolecule.indexOf(from, ind)
            if (ind == -1)
                break
            newMolecule.clear()
            newMolecule.appendRange(medData.medMolecule, 0, ind)
            newMolecule.append(to)
            newMolecule.appendRange(medData.medMolecule, ind + from.length, medData.medMolecule.length)
            molecules.add(newMolecule.toString())
            ind+=from.length
        }
    }

    return molecules.size
}

val newMolecule = StringBuilder()
private fun calculateStepsRecurse(medData: MedData, cache: HashSet<String>, path: String, molecule: String, depth: Int) : Int {
    //if (depth == 9)
    //    return Result(0, false)

    if (molecule == medData.medMolecule) {
        // print("Found !\n")
        return 0
    }
    if (molecule.length >= medData.medMolecule.length)
        return -1
    //if (molecule != "e" && medData.medMolecule.indexOf(molecule) == -1 && molecule.length > medData.medMolecule.length/10)
    //    return Result(0, false)

    val replacementResults = mutableListOf<Int>()
    val candidates = hashSetOf<Pair<String, String>>()
    var index = 0
    medData.replaceRules.forEach { (from, to) ->
        var ind = 0
        while(true) {
            ind = molecule.indexOf(from, ind)
            if (ind == -1)
                break
            newMolecule.clear()
            newMolecule.appendRange(molecule, 0, ind)
            newMolecule.append(to)
            newMolecule.appendRange(molecule, ind + from.length, molecule.length)
            val newMoleculeStr = newMolecule.toString()

            var allowAdd = true
            val pth = "$path,$index:$ind"
            if (cache.contains(pth))
               allowAdd = false
            if (allowAdd)
                candidates.add(Pair(newMoleculeStr, pth))
            ind+=from.length
        }
        index++
    }

    candidates.forEach {
        var res = calculateStepsRecurse(medData, cache, it.second, it.first, depth + 1)
        if (res == -1)
            cache.add(it.second)
        else
            res++
        if (res != -1)
            replacementResults.add(res)
    }

    val bestResult = replacementResults.minByOrNull { it }
    val result = bestResult ?: -1
    return result
}

fun calculateNumberOfStepsBruteForce(data: String): Int {
    val medData = parseMedData(data)
    medData.replaceRules = medData.replaceRules.sortedByDescending{ it.to.length }
    return calculateStepsRecurse(medData, hashSetOf(), "", "e", 0)
}


fun calculateNumberOfStepsRecurse(molecule: String, medData: MedData, cache: HashSet<String>, depth: Int, steps: Int): Int {
    if(molecule == "e") {
        //print("Found! $steps\n")
        return 0
    }

    val candidateCount = hashMapOf<String, Int>()
    val candidates = hashSetOf<String>()
    medData.replaceRules.forEach { (from, to) ->
        if (molecule.contains(to)) {

            val newMolecule = molecule.replace(to, from)
            if(!cache.contains(newMolecule))
            {

                // Count
                var count = 0
                var ind = 0
                while (true) {
                    ind = molecule.indexOf(to, ind)
                    if (ind == -1)
                        break
                    count++
                    ind += to.length
                }

                candidates.add(newMolecule)
                candidateCount[newMolecule] = count
            }
        }
    }

    val result = arrayListOf<Int>()
    run ext@{
        candidates.forEach {
            if (depth == 0)
                cache.clear()
            val res = calculateNumberOfStepsRecurse(it, medData, cache, depth + 1, steps + (candidateCount[it] ?: 0))
            cache.add(it)
            if (res >= 0) {
                //if (depth == 0)
                //    print("Result: ${res + (candidateCount[it] ?: 0)}")
                result.add(res + (candidateCount[it] ?: 0))
                return@ext // this is not quite right, but produces correct result...
            }
        }
    }

    val res = result.minOrNull() ?: -1
    return if (res == -1) -1 else res
}

fun calculateNumberOfStepsTopDown(data: String): Int {
    val medData = parseMedData(data)
    medData.replaceRules = medData.replaceRules.sortedBy{ it.to.length }
    return calculateNumberOfStepsRecurse(medData.medMolecule, medData, hashSetOf(), 0, 0)
}

fun calculateNumberOfStepsTopDownSeq(data: String, sortMedDataDesc:Boolean = false): Int {
    val medData = parseMedData(data)
    if (sortMedDataDesc)
        medData.replaceRules = medData.replaceRules.sortedByDescending{ it.to.length }
    var steps = 0
    var molecule = medData.medMolecule
    do {
        medData.replaceRules.forEach {
            if (molecule.contains(it.to)) {
                molecule = molecule.replaceFirst(it.to, it.from)
                steps++
            }
        }
        //if (molecule.contains("e") && molecule.length > 1 ) {
        //    medData.replaceRules = medData.replaceRules.shuffled()
        //    molecule = medData.medMolecule
        //    steps = 0
        //}
    } while(molecule != "e")

    return steps
}

fun runday19() {
    val data = readAllText("/day19.txt")
    println(" --- Day19 ---")
    println("result for part 1: ${verifyResult(calculateNumberOfMolecules(data),
        getIntFromFile("/day19_result.txt", 0)
        )}")

    // Way too timeconsuming
    //println("result for part 2: ${calculateNumberOfStepsBruteForce(data)}")

    // This one works but it does not go all the possibilities which renders it useless for some inputs - I suppose.
    //println("result for part 2: ${verifyResult(calculateNumberOfStepsTopDown(data), getIntFromFile("/day19_result.txt", 1))}")
    println("result for part 2: ${verifyResult(calculateNumberOfStepsTopDownSeq(data), getIntFromFile("/day19_result.txt", 1)
    )}")
}

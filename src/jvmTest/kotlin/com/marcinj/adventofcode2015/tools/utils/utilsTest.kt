package com.marcinj.adventofcode2015.tools.utils

import com.marcinj.adventofcode2015.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

class utilsTest {
    //@OptIn(ExperimentalTime::class)
    @Test
    fun `test 1`()
    {

        /*
        // Time measurements of various permutation methods, results are: time1 >>> time2 > time3
        val cityList = listOf("Vixen", "Rudolph", "Donner", "Blitzen", "Comet", "Cupid", "Dasher", "Dancer", "Prancer")
        println("time1: " + measureTime {
            var perms1 = permutations(cityList)
        })
        println("time2: " + measureTime {
            var perms = permutationsHeapRecursive(cityList)
        })
        println("time3: " + measureTime {
            var perms = permutationsHeapNonRecursive(cityList)
        })
        */

        for (k in 1..8) {
            val inlist = if (k == 0) listOf() else (0..k).map{ it }
            val perms1 = permutationsSlow(inlist)
            val perms2 = permutationsHeapRecursive(inlist)
            val perms3 = permutations(inlist)
            assertEquals(perms1.size, perms2.size)
            assertEquals(perms2.size, perms3.size)
            val permsSet = perms2.toSet()
            assertEquals(permsSet, perms1)
            val permsSet2 = perms3.toSet()
            assertEquals(permsSet2, perms1)
        }
    }

    @Test
    fun `setPermute test`() {
        val sp = SetPermute(mutableListOf(), mutableListOf(0, 0, 0), mutableListOf(9,9,9))
        for (i in  0..999) {
            nextPermutation(sp)
            val num = sp.values.withIndex().reversed().fold(0) { acc,
                                                                 num ->
                return@fold acc + Math.pow(10.0, (sp.values.size - 1 - num.index).toDouble()).toInt() * num.value
            }
            assertEquals(num, i)
        }
    }

    @Test
    fun `setPermute test 2`() {
        val sp = SetPermute(mutableListOf(0, 0, 0, 0, 0), mutableListOf(0, 0, 0, 0, 0), mutableListOf(9,9,9,9,9))
        for (i in  1..99999) {
            nextPermutation(sp)
            val num = sp.values.withIndex().reversed().fold(0) { acc,
                                                                 num ->
                return@fold acc + Math.pow(10.0, (sp.values.size - 1 - num.index).toDouble()).toInt() * num.value
            }
            assertEquals(num, i)
        }
    }

    @Test
    fun `setPermute test 3`() {
        val sp = SetPermute(mutableListOf(), mutableListOf(0, 0, 0, 0, 0), mutableListOf(1,2,1,2,1))
        val res = mutableListOf<List<Int>>()
        while(nextPermutation(sp)) {
            res.add(sp.values.toList())
        }
        assertEquals(sp.maxValues.fold(1) { acc,v -> acc * (v+1)}, res.size)
    }
}

package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.readAllText
import kotlin.test.Test
import kotlin.test.assertEquals

class Day21Test {

    @Test
    fun `Test loading data`() {
        val data = readAllText("/day21.txt")
        val pd = parseDay21Data(data)
        assertEquals(5, pd.shopItems.weapons.size)
        assertEquals(5, pd.shopItems.armors.size)
        assertEquals(6, pd.shopItems.rings.size)

        assertEquals(103, pd.bossData.hp)
        assertEquals(9, pd.bossData.damage)
        assertEquals(2, pd.bossData.armor)

        assertEquals(8, pd.shopItems.weapons[0].cost)
        assertEquals(4, pd.shopItems.weapons[0].damage)
        assertEquals(0, pd.shopItems.weapons[0].armor)

        assertEquals(74, pd.shopItems.weapons[4].cost)
        assertEquals(8, pd.shopItems.weapons[4].damage)
        assertEquals(0, pd.shopItems.weapons[4].armor)

        assertEquals(80, pd.shopItems.rings[5].cost)
        assertEquals(0, pd.shopItems.rings[5].damage)
        assertEquals(3, pd.shopItems.rings[5].armor)

    }

}
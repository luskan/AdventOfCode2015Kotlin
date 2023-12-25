package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText

data class BossData(val hp: Int, val damage: Int, val armor: Int)
data class ShopItem(val name: String, val cost: Int, val damage: Int, val armor: Int)
data class ShopItems(val weapons: List<ShopItem>, val armors: List<ShopItem>, val rings: List<ShopItem>)
data class GameData(val shopItems: ShopItems, val bossData: BossData)
enum class ItemType { Weapons, Armors, Rings, None }

fun parseDay21Data(data: String) : GameData {
    val weapons = mutableListOf<ShopItem>()
    val armors = mutableListOf<ShopItem>()
    val rings = mutableListOf<ShopItem>()

    var curItemType = ItemType.None
    var isBossData = false
    //Damage +1    25     1       0
    val shopItemRg = """([\w\s\\+0-9]+)\s+(\d+)\s+(\d+)\s+(\d+)""".toRegex()
    val bossDataRg = """([\w\s]+):\s+(\d+)""".toRegex()

    var bossHP = 0
    var bossDamage = 0
    var bossArmor = 0

    data.lines().forEach forEachLoop@{ line ->
        if (line.isEmpty())
            return@forEachLoop
        if (!isBossData && line.contains("Weapons:"))
            curItemType = ItemType.Weapons
        else if (!isBossData && line.contains("Armor:"))
            curItemType = ItemType.Armors
        else if (!isBossData && line.contains("Rings:"))
            curItemType = ItemType.Rings
        else if (line.contains("Boss Data:")) {
            curItemType = ItemType.None
            isBossData = true
        }
        else {
            if (isBossData) {
                val m = bossDataRg.matchEntire(line)
                m?.let {
                    val (name, value) = it.destructured
                    when (name) {
                        "Hit Points" -> bossHP = value.toInt()
                        "Damage" -> bossDamage = value.toInt()
                        "Armor" -> bossArmor = value.toInt()
                        else -> fail("")
                    }
                } ?:
                fail("")
            }
            else {
                val m = shopItemRg.matchEntire(line)
                m?.let {
                    val (name, cost, damage, armor) = it.destructured
                    val sp = ShopItem(name.trim(), cost.toInt(), damage.toInt(), armor.toInt())
                    when (curItemType) {
                        ItemType.Weapons -> weapons.add(sp)
                        ItemType.Armors -> armors.add(sp)
                        ItemType.Rings -> rings.add(sp)
                        else -> fail("Added new type?")
                    }
                } ?: fail("")
            }
        }
    }

    val bossData = BossData(bossHP, bossDamage, bossArmor)
    return GameData(ShopItems(weapons, armors, rings), bossData)
}

fun calculateGoldRequired(data: String, part2: Boolean): Int {
    val gd = parseDay21Data(data)

    val shopMaxCountsList = mutableListOf<Int>(
        gd.shopItems.rings.size,
        gd.shopItems.rings.size,
        gd.shopItems.armors.size,
        gd.shopItems.weapons.size
    )

    var bestSpend = if (part2) 0 else 999
    val sp = SetPermute(mutableListOf(), mutableListOf(0, 0, 0, 1), shopMaxCountsList)
    while(nextPermutation(sp)) {
        if (sp.values[0] != 0 && sp.values[1] != 0 && sp.values[0] == sp.values[1]) {
            // We cant but the same rings twice.
            continue
        }
        val boughtItems = mutableListOf<ShopItem>()
        sp.values.withIndex().forEach {
            if (it.value != 0) {
                boughtItems.add(
                    when(it.index) {
                        0,1 -> gd.shopItems.rings[it.value-1]
                        2 -> gd.shopItems.armors[it.value-1]
                        3 -> gd.shopItems.weapons[it.value-1]
                        else -> fail("")
                    }
                )
            }
        }

        val playerWins = fightWithBoss(100, boughtItems, gd.bossData)
        val spentGold = boughtItems.fold(0){ acc,bi -> acc + bi.cost}

        if (part2) {
            if (!playerWins && spentGold > bestSpend) {
                bestSpend = spentGold
            }
        }
        else {
            if (playerWins && spentGold < bestSpend) {
                bestSpend = spentGold
            }
        }
    }

    return bestSpend
}

private fun fightWithBoss(playerHpVal: Int, playerItems: MutableList<ShopItem>, bossData: BossData): Boolean {
    var playerHp = playerHpVal
    var bossHp = bossData.hp

    val totalPlayerArmor = playerItems.fold(0) { acc,op -> acc + op.armor }
    val totalPlayerDamage = kotlin.math.max(playerItems.fold(0) { acc,op -> acc + op.damage } - bossData.armor, 1)
    val totalBossDamage = kotlin.math.max(bossData.damage - totalPlayerArmor, 1)

    var playerTurn = true
    while (playerHp > 0 && bossHp > 0) {
        if (playerTurn)
            bossHp -= totalPlayerDamage
        else
            playerHp -= totalBossDamage
        playerTurn = !playerTurn
    }

    return playerHp > 0
}

fun runday21() {
    println(" --- Day21 ---")
    val data = readAllText("/day21.txt")
    println("result for part 1: ${verifyResult(getIntFromFile("/day21_result.txt", 0), calculateGoldRequired(data, false))}")
    println("result for part 1: ${verifyResult(getIntFromFile("/day21_result.txt", 1), calculateGoldRequired(data, true))}")

}






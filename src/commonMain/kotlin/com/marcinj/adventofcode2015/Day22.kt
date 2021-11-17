package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail

enum class SpellType(val mana: Int) {
    MagicMissle(53), Drain(73), Shield(113), Poison(173), Recharge(229)
}

private interface SpellBase {
    fun update(gd: WizardGameData)
    fun isComplete() : Boolean
    fun clone() : SpellBase

    val type: SpellType
}

private data class WizardGameData(
    var playerHp: Int = 0,
    var playerMana: Int = 0,
    var playerArmor: Int = 0,
    var bossHp: Int = 0,
    var bossDamage: Int = 0,
    var activeSpells: MutableList<SpellBase> = mutableListOf(),
    var initialMana: Int = playerMana,
    var spentMana: Int = 0) {

    fun deepClone() : WizardGameData {
        val wg = copy()
        wg.activeSpells = activeSpells.map { it.clone() }.toMutableList()
        return wg
    }
}

// Magic Missile costs 53 mana. It instantly does 4 damage.
private class MagicMissleSpell(var totalApplied: Int = -1) : SpellBase {
    override fun update(gd: WizardGameData) {
        gd.playerMana -= type.mana
        gd.spentMana += type.mana
        gd.bossHp -= 4
        totalApplied = 1
    }

    override fun isComplete(): Boolean = totalApplied == 1
    override fun clone() = MagicMissleSpell(totalApplied)

    override val type = SpellType.MagicMissle
}

// Drain costs 73 mana. It instantly does 2 damage and heals you for 2 hit points.
private class DrainSpell(var totalApplied: Int = -1) : SpellBase {
    override fun update(gd: WizardGameData) {
        gd.playerMana -= type.mana
        gd.spentMana += type.mana
        gd.bossHp -= 2
        gd.playerHp += 2
        totalApplied = 0
    }

    override fun isComplete(): Boolean = totalApplied == 0
    override fun clone() = DrainSpell()

    override val type = SpellType.Drain
}

//Shield costs 113 mana. It starts an effect that lasts for 6 turns. While it is active, your armor is increased by 7.
private class ShieldSpell(var totalApplied: Int = -1) : SpellBase {
    override fun update(gd: WizardGameData) {
        if (totalApplied == -1) {
            totalApplied = 0
            return
        }
        if (totalApplied == 0) {
            gd.playerMana -= type.mana
            gd.spentMana += type.mana
            gd.playerArmor += 7
        }

        totalApplied++
        if (totalApplied == 7) {
            gd.playerArmor -= 7
        }
    }

    override fun isComplete(): Boolean = totalApplied == 7
    override fun clone() = ShieldSpell(totalApplied)

    override val type = SpellType.Shield
}

//Poison costs 173 mana. It starts an effect that lasts for 6 turns. At the start of each turn while it is active, it deals the boss 3 damage.
private class PoisonSpell(var totalApplied: Int = -1) : SpellBase {

    override fun update(gd: WizardGameData) {
        if (totalApplied == -1) {
            totalApplied = 0
            return
        }

        if (totalApplied == 0) {
            gd.playerMana -= type.mana
            gd.spentMana += type.mana
        }

        gd.bossHp -= 3
        totalApplied++

        if (totalApplied > 6)
           fail("")
    }

    override fun isComplete(): Boolean = totalApplied == 6
    override fun clone() = PoisonSpell(totalApplied)

    override val type = SpellType.Poison
}

//Recharge costs 229 mana. It starts an effect that lasts for 5 turns. At the start of each turn while it is active, it gives you 101 new mana.
private class RechargeMissle(var totalApplied: Int = -1) : SpellBase {

    override fun update(gd: WizardGameData) {
        if (totalApplied == -1) {
            totalApplied = 0
            return
        }

        if (totalApplied == 0) {
            gd.playerMana -= type.mana
            gd.spentMana += type.mana
        }

        gd.playerMana += 101
        totalApplied++

        if (totalApplied > 5)
            fail("")
    }

    override fun isComplete(): Boolean = totalApplied == 5
    override fun clone() = RechargeMissle(totalApplied)

    override val type = SpellType.Recharge
}

private fun createSpellFromType(type: SpellType) = when(type) {
    SpellType.MagicMissle -> MagicMissleSpell()
    SpellType.Drain -> DrainSpell()
    SpellType.Shield -> ShieldSpell()
    SpellType.Poison -> PoisonSpell()
    SpellType.Recharge -> RechargeMissle()
}

fun calculateMinManaRequired(playerHp: Int, playerMana: Int, bossHp: Int, bossDamage: Int, part2: Boolean): Int {
    val gameData = WizardGameData(playerHp, playerMana, 0, bossHp, bossDamage)
    return fightWithBoss(gameData, part2)
}

private fun fightWithBoss(gameData: WizardGameData, part2: Boolean): Int {

    val manaResults = mutableListOf<Int>()
    SpellType.values().forEachIndexed spellFor@{index, spellType ->
        // Check if spell can be casted (its not used, and player has enough mana)

        // Make a deep copy of gameData for a new duel branch
        val newGameData = gameData.deepClone()

        //
        // Player turn, cast a spell and apply damage from spells to boss
        if (part2) {
            newGameData.playerHp--
            if (newGameData.playerHp <= 0) {
                return@spellFor
            }
        }
        newGameData.activeSpells.forEach { spell -> spell.update(newGameData) }
        newGameData.activeSpells.removeAll { it.isComplete() }

        if (newGameData.bossHp <= 0) {
            manaResults.add(newGameData.spentMana)
            return@spellFor
        }

        if (newGameData.activeSpells.find { it.type == spellType } != null)
            return@spellFor
        if (newGameData.playerMana < spellType.mana)
            return@spellFor

        val spellFromType = createSpellFromType(spellType)
        newGameData.activeSpells.add(spellFromType)
        spellFromType.update(newGameData)
        newGameData.activeSpells.removeAll { it.isComplete() }

        if (newGameData.bossHp <= 0) {
            manaResults.add(newGameData.spentMana)
            return@spellFor
        }

        //
        // Boss turn, apply damage from boss to player

        newGameData.activeSpells.forEach { spell -> spell.update(newGameData) }
        if (newGameData.bossHp <= 0) {
            manaResults.add(newGameData.spentMana)
            return@spellFor
        }

        newGameData.playerHp -= kotlin.math.max(1, newGameData.bossDamage - newGameData.playerArmor)

        if (newGameData.playerHp <= 0) {
            return@spellFor
        }

        // Remove completed spells
        newGameData.activeSpells.removeAll { it.isComplete() }

        // Start next round
        val manaRes = fightWithBoss(newGameData, part2)
        if (manaRes != -1)
            manaResults.add(manaRes)
    }
    return manaResults.minOfOrNull{ it } ?: -1
}

fun runday22() {
    println(" --- Day22 ---")
    val playerHp = 50
    val playerMana = 500

    //Hit Points: 55
    //Damage: 8
    val bossHp = 55
    val bossDamage = 8
    println("result for part 1: ${verifyResult(953,
        calculateMinManaRequired(playerHp, playerMana,  bossHp, bossDamage, false))}")
    println("result for part 2: ${verifyResult(1289,
        calculateMinManaRequired(playerHp, playerMana,  bossHp, bossDamage, true))}")
}







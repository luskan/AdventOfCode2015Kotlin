package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getIntFromFile

enum class SpellType(val mana: Int) {
    MagicMissle(53), Drain(73), Shield(113), Poison(173), Recharge(229);

    override fun toString(): String {
        return "$name($mana)"
    }
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
    var spentMana: Int = 0,
    var spendManaSpells: MutableList<SpellType> = mutableListOf()
    ) {

    fun deepClone(baseGameData: WizardGameData) : WizardGameData {
        baseGameData.playerHp = playerHp
        baseGameData.playerMana = playerMana
        baseGameData.playerArmor = playerArmor
        baseGameData.bossHp = bossHp
        baseGameData.bossDamage = bossDamage
        baseGameData.activeSpells.clear()
        baseGameData.initialMana = initialMana
        baseGameData.spentMana = spentMana

        baseGameData.activeSpells.clear()
        activeSpells.forEach { baseGameData.activeSpells.add(it.clone()) }
        baseGameData.spendManaSpells.clear()
        spendManaSpells.forEach { baseGameData.spendManaSpells.add(it) }

        return baseGameData
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

fun calculateMinManaRequired(playerHp: Int, playerMana: Int, bossHp: Int, bossDamage: Int, part2: Boolean): ManaResult {
    val gameData = WizardGameData(playerHp, playerMana, 0, bossHp, bossDamage)
    return fightWithBoss(gameData, part2)
}
data class ManaResult(var mana: Int = 9999, var spells: List<SpellType> = listOf(), var failed: Boolean = false)
private fun fightWithBoss(gameData: WizardGameData, part2: Boolean,
                          totalBestMinMana: ManaResult = ManaResult()): ManaResult {
    if (gameData.spentMana >= totalBestMinMana.mana)
        return ManaResult(failed = true)

    val baseGameData = WizardGameData()

    val manaResults = mutableListOf<ManaResult>()
    SpellType.values().forEachIndexed spellFor@{_, spellType ->
        // Check if spell can be casted (its not used, and player has enough mana)

        // Make a deep copy of gameData for a new duel branch - as a storage reuse baseGameData to limit allocations
        val newGameData = gameData.deepClone(baseGameData)

        //
        // Player turn, cast a spell and apply damage from spells to boss
        if (part2) {
            newGameData.playerHp--
            if (newGameData.playerHp <= 0)
                return@spellFor
        }

        newGameData.activeSpells.forEach { spell -> spell.update(newGameData) }
        newGameData.activeSpells.removeAll { it.isComplete() }

        if (newGameData.bossHp <= 0)
            manaResults.add(ManaResult(newGameData.spentMana, newGameData.spendManaSpells.toList()))

        if (newGameData.activeSpells.find { it.type == spellType } != null)
            return@spellFor
        if (newGameData.playerMana < spellType.mana)
            return@spellFor

        val spellFromType = createSpellFromType(spellType)
        newGameData.activeSpells.add(spellFromType)
        spellFromType.update(newGameData)
        newGameData.spendManaSpells.add(spellFromType.type)
        newGameData.activeSpells.removeAll { it.isComplete() }

        if (newGameData.bossHp <= 0) {
            manaResults.add(ManaResult(newGameData.spentMana, newGameData.spendManaSpells.toList()))
            return@spellFor
        }

        //
        // Boss turn, apply damage from boss to player

        newGameData.activeSpells.forEach { spell -> spell.update(newGameData) }
        if (newGameData.bossHp <= 0) {
            manaResults.add(ManaResult(newGameData.spentMana, newGameData.spendManaSpells.toList()))
            return@spellFor
        }

        newGameData.playerHp -= kotlin.math.max(1, newGameData.bossDamage - newGameData.playerArmor)

        if (newGameData.playerHp <= 0) {
            return@spellFor
        }

        // Remove completed spells
        newGameData.activeSpells.removeAll { it.isComplete() }

        // Start next round
        val manaRes = fightWithBoss(newGameData, part2, totalBestMinMana)
        if (!manaRes.failed) {
            manaResults.add(manaRes)
        }
    }

    val bestMinMana = manaResults.minOfWithOrNull({a,b -> a.mana - b.mana}, {a -> a} )
    bestMinMana?.let { best ->
        if (best.mana < totalBestMinMana.mana) {
            totalBestMinMana.mana = best.mana
            totalBestMinMana.spells = best.spells.toList()
        }
    }
    return bestMinMana ?: ManaResult(failed = true)
}

fun runday22() {
    println(" --- Day22 ---")
    val playerHp = getIntFromFile("/day22.txt", 0)
    val playerMana = getIntFromFile("/day22.txt", 1)
    val bossHp = getIntFromFile("/day22.txt", 2)
    val bossDamage = getIntFromFile("/day22.txt", 3)
    var manaResult: ManaResult
    manaResult = calculateMinManaRequired(playerHp, playerMana,  bossHp, bossDamage, false)
    println("result for part 1: ${verifyResult(getIntFromFile("/day22_result.txt", 0),
        manaResult.mana)} spells [sum=${manaResult.spells.fold(0){acc,st -> acc+st.mana}}]: ${manaResult.spells}")
    manaResult = calculateMinManaRequired(playerHp, playerMana,  bossHp, bossDamage, true)
    println("result for part 2: ${verifyResult(getIntFromFile("/day22_result.txt", 1),
        manaResult.mana)} spells [sum=${manaResult.spells.fold(0){acc,st -> acc+st.mana}}]: ${manaResult.spells}")
}







package com.marcinj.adventofcode2015

import com.marcinj.adventofcode2015.tools.fail
import com.marcinj.adventofcode2015.tools.getIntFromFile
import com.marcinj.adventofcode2015.tools.readAllText

data class Registers(var a: Int, var b: Int, var ip: Int) {
    fun doOpOnReg(reg: Char, op: (Int) -> Int) {
        if (reg == 'a')
            a = op(a)
        else if (reg == 'b')
            b = op(b)
        else
            fail("Unknown reg!")
    }
}
enum class Instruction(val logic: (Registers, CodeLine) -> Unit) {
    HLF({ reg, code -> reg.doOpOnReg(code.register){r -> r / 2}; reg.ip += 1 }),
    TPL({ reg, code -> reg.doOpOnReg(code.register){r -> r * 3}; reg.ip += 1 }),
    INC({ reg, code -> reg.doOpOnReg(code.register){r -> r + 1}; reg.ip += 1 }),
    JMP({ reg, code -> reg.ip += code.offset }),
    JIE({ reg, code -> reg.doOpOnReg(code.register){r -> if (r%2==0) reg.ip += code.offset else reg.ip += 1; r; }}),
    JIO({ reg, code -> reg.doOpOnReg(code.register){r -> if (r==1) reg.ip += code.offset else reg.ip += 1; r; }}),
}
data class CodeLine(val instruction: Instruction, val register: Char, val offset: Int)

fun parseSourceCode(data: String): List<CodeLine> {
    val code = mutableListOf<CodeLine>()
    data.lines().forEach { line ->
        val split = line.split(", ", " ", ",")
        val instruction = Instruction.values().find { it.name.equals(split[0], ignoreCase = true) } ?: fail("Unknown instruction! - ${split[0]}")
        val reg = if (split.size >= 2 && instruction != Instruction.JMP) split[1][0] else ' '
        val offset = if (split.size == 3 || split.size == 2 && instruction == Instruction.JMP ) {
            val ind = if (instruction != Instruction.JMP) 2 else 1
            val sign = if (split[ind][0] == '+') +1 else -1
            val off = split[ind].substring(1).toInt()
            sign * off
        } else 0
        code.add(CodeLine(instruction, reg, offset))
    }
    return code
}

fun calculateProgramResult(data: String, part2: Boolean = false): Registers {
    val code = parseSourceCode(data)
    val regs = Registers(if(part2) 1 else 0, 0, 0)
    while(regs.ip >= 0 && regs.ip < code.size) {
        val codeLine = code[regs.ip]
        codeLine.instruction.logic(regs, codeLine)
    }
    return regs
}

fun runday23() {
    println(" --- Day23 ---")

    val data = readAllText("/day23.txt")
    println("result for part 1: ${verifyResult(getIntFromFile("/day23_result.txt", 0), calculateProgramResult(data).b)}")
    println("result for part 1: ${verifyResult(getIntFromFile("/day23_result.txt", 1), calculateProgramResult(data, true).b)}")
}








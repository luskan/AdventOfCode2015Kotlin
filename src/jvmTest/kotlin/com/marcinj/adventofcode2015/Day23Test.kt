package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day23Test {

    @Test
    fun `Instruction hlf`() {
        val cl = CodeLine(Instruction.HLF, 'a', 0)
        val regs = Registers(20, 20, 0)
        cl.instruction.logic(regs, cl)
        assertEquals(10, regs.a)
        assertEquals(1, regs.ip)

        val cl2 = CodeLine(Instruction.HLF, 'b', 0)
        cl2.instruction.logic(regs, cl2)
        assertEquals(10, regs.b)
        assertEquals(2, regs.ip)
    }

    @Test
    fun `Instruction tpl`() {
        val cl = CodeLine(Instruction.TPL, 'a', 0)
        val regs = Registers(20, 20, 0)
        cl.instruction.logic(regs, cl)
        assertEquals(60, regs.a)
        assertEquals(1, regs.ip)
    }

    @Test
    fun `Instruction inc`() {
        val cl = CodeLine(Instruction.INC, 'a', 0)
        val regs = Registers(20, 20, 0)
        cl.instruction.logic(regs, cl)
        assertEquals(21, regs.a)
        assertEquals(1, regs.ip)
    }

    @Test
    fun `Instruction jmp`() {
        val cl = CodeLine(Instruction.JMP, 'a', 10)
        val regs = Registers(20, 20, 0)
        cl.instruction.logic(regs, cl)
        assertEquals(20, regs.a)
        assertEquals(20, regs.b)
        assertEquals(10, regs.ip)

        val cl2 = CodeLine(Instruction.JMP, 'a', -10)
        val regs2 = Registers(20, 20, 11)
        cl2.instruction.logic(regs2, cl2)
        assertEquals(20, regs2.a)
        assertEquals(20, regs2.b)
        assertEquals(1, regs2.ip)
    }

    @Test
    fun `Instruction jie`() {
        val cl = CodeLine(Instruction.JIE, 'a', 10)
        val regs = Registers(20, 20, 0)
        cl.instruction.logic(regs, cl)
        assertEquals(20, regs.a)
        assertEquals(20, regs.b)
        assertEquals(10, regs.ip)

        val cl2 = CodeLine(Instruction.JIE, 'a', 10)
        val regs2 = Registers(21, 20, 0)
        cl2.instruction.logic(regs2, cl2)
        assertEquals(21, regs2.a)
        assertEquals(20, regs2.b)
        assertEquals(1, regs2.ip)
    }


    @Test
    fun `Instruction jio`() {
        val cl = CodeLine(Instruction.JIO, 'a', 10)
        val regs = Registers(1, 20, 0)
        cl.instruction.logic(regs, cl)
        assertEquals(1, regs.a)
        assertEquals(20, regs.b)
        assertEquals(10, regs.ip)

        val cl2 = CodeLine(Instruction.JIO, 'a', 10)
        val regs2 = Registers(21, 20, 0)
        cl2.instruction.logic(regs2, cl2)
        assertEquals(21, regs2.a)
        assertEquals(20, regs2.b)
        assertEquals(1, regs2.ip)
    }

    val data = """
            inc a
            jio a, +2
            tpl a
            inc a
        """.trimIndent()

    val data2 = """
            inc a
            jio a, +2
            jio a, -22
            jmp +22
            inc a
        """.trimIndent()

    @Test
    fun `Parse test`() {
        val code = parseSourceCode(data2)

        assertEquals(22, code[3].offset)

        assertEquals(Instruction.INC, code[0].instruction)
        assertEquals('a', code[0].register)

        assertEquals(2, code[1].offset)
        assertEquals(Instruction.JIO, code[1].instruction)

        assertEquals(-22, code[2].offset)
    }

    @Test
    fun `First test`() {

        assertEquals(2, calculateProgramResult(data).a)
    }
}
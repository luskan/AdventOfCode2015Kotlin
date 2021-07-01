package com.marcinj.adventofcode2015

import kotlin.test.Test
import kotlin.test.assertEquals

class Day6Test {

    @Test
    fun `parse data`() {
        assertEquals(
            LampOpData(LampOp.off, Rect(Point(12,823), Point(102,934))),
            parseLampData("turn off 12,823 through 102,934")[0]
        )
        assertEquals(
            LampOpData(LampOp.on, Rect(Point(1,2), Point(3,4))),
            parseLampData("turn on 1,2 through 3,4")[0]
        )
        assertEquals(
            LampOpData(LampOp.toggle, Rect(Point(111,222), Point(333,444))),
            parseLampData("toggle 111,222 through 333,444")[0]
        )
    }

    @Test
    fun `Test part 2`() {
        assertEquals(
            1,
            calculateLampsPart2("turn on 0,0 through 0,0")
        )
        assertEquals(
            2000000,
            calculateLampsPart2("toggle 0,0 through 999,999")
        )
    }
}
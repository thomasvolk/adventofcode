package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals

class Day10Test {
    private val inputFile: URL = PuzzleInput.loadFile("/Day10-input.txt")

    @Test
    fun testDay10Part1() {
        val commands = Day10.parse(inputFile)
        var result = 0
        val processor = Day10.Processor()
        processor.dataBuss.registerDevice { cycles, x ->
            val amount = when (cycles) {
                20 -> 20 * x
                60 -> 60 * x
                100 -> 100 * x
                140 -> 140 * x
                180 -> 180 * x
                220 -> 220 * x
                else -> 0
            }
            result += amount
        }
        commands.forEach { c -> c.execute(processor) }
        assertEquals(14320, result)
    }
}
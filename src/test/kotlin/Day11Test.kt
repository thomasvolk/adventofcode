package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals

class Day11Test {
    private val inputFile: URL = PuzzleInput.loadFile("/Day11-input.txt")
    @Test
    fun testDay11Part1() {
        val monkeys = Day11.Monkey.parse(inputFile.readText())
        assertEquals(8, monkeys.count())
    }
}
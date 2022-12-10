package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals

class Day09Test {
    private val inputFile: URL = PuzzleInput.loadFile("/Day09-input.txt")

    private val testData = """
        R 4
        U 4
        L 3
        D 1
        R 4
        D 1
        L 5
        R 2
    """.trimIndent()

    @Test
    fun testLogic() {
        val moves = Day09.parse(testData.split("\n"))
        val rope = Day09.rope(0,0)
        rope.execute(moves)
        assertEquals(13, rope.tailHistory().count())
    }

    @Test
    fun testDay09Part1() {
        val moves = Day09.parse(inputFile)
        val rope = Day09.rope(0,0)
        rope.execute(moves)
        assertEquals(6642, rope.tailHistory().count())
    }

    @Test
    fun testDay09Part2() {
        val moves = Day09.parse(inputFile)
        val rope = Day09.rope(0,0, 9)
        rope.execute(moves)
        assertEquals(2765, rope.tailHistory().count())
    }
}
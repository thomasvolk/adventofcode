package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals

class Day13Test {
    private val inputFile: URL = PuzzleInput.loadFile("/Day13-input.txt")

    @Test
    fun testExample() {
        val testData = """
            [1,1,3,1,1]
            [1,1,5,1,1]

            [[1],[2,3,4]]
            [[1],4]

            [9]
            [[8,7,6]]

            [[4,4],4,4]
            [[4,4],4,4,4]

            [7,7,7,7]
            [7,7,7]

            []
            [3]

            [[[]]]
            [[]]

            [1,[2,[3,[4,[5,6,7]]]],8,9]
            [1,[2,[3,[4,[5,6,0]]]],8,9]
        """.trimIndent()
        val packetPairs = Day13.parse(testData)
        val result = packetPairs.filter { it.correctOrder() }.sumOf { it.index }
        assertEquals(13, result)
    }

    @Test
    fun testDay13Part1() {
        val packetPairs = Day13.parse(inputFile)
        val result = packetPairs.filter { it.correctOrder() }.sumOf { it.index }
        assertEquals(3394, result)
    }
}
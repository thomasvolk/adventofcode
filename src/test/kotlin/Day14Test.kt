package net.t53k

import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals

class Day14Test {
    private val inputFile: URL = PuzzleInput.loadFile("/Day14-input.txt")

    @Test
    fun testExample() {
        val testData = """
            498,4 -> 498,6 -> 496,6
            503,4 -> 502,4 -> 502,9 -> 494,9
        """.trimIndent()
        val cave = Day14.parse(testData.split("\n"))
        assertEquals("""
..........
..........
..........
..........
....#...##
....#...#.
..###...#.
........#.
........#.
#########.
        """.trimIndent(), cave.toString())
    }
}
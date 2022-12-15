package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals

class Day12Test {
    private val inputFile: URL = PuzzleInput.loadFile("/Day12-input.txt")
    @Test
    fun testSimpleMap() {
        val inputData = """
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi
        """.trimIndent()
        assertEquals(31, Day12.stepsForShortestPath(inputData))
    }
    @Test
    fun testDay12Part1() {
        // brute force is not working :-(
        assertEquals(1240, Day12.stepsForShortestPath(inputFile))
    }
}
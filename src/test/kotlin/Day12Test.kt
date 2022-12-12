package net.t53k

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12Test {
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
}
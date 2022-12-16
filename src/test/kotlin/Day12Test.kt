package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals

class Day12Test {
    private val inputFile: URL = PuzzleInput.loadFile("/Day12-input.txt")
    @Test
    fun testExample() {
        val inputData = """
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi
        """.trimIndent()
        val map = Day12.parseMap(inputData)
        println(map)
        val count = map.findPath()
        println("")
        println(map)
        assertEquals(31, count)
    }
    @Test
    fun testDay12Part1() {
        val map = Day12.parseMap(inputFile)
        assertEquals(-1, map.findPath())
    }
}
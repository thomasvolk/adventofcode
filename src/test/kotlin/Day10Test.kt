package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL

class Day10Test {
    private val inputFile: URL = PuzzleInput.loadFile("/Day10-input.txt")

    @Test
    fun testDay10Part1() {
        Day10.parse(inputFile)
    }
}
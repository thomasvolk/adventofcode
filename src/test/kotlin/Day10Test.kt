package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL

class Day10Test {
    private val inputFile: URL = PuzzleInput.loadFile("/Day10-input.txt")

    @Test
    fun testDay10Part1() {
        val commands = Day10.parse(inputFile)
        val processor = Day10.Processor()
        commands.forEach { c -> c.execute(processor) }
        println(processor.cycles())
        println(processor.x)
    }
}
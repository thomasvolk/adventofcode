package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL

class Day05Test {
    private val inputFile: URL = Day01::class.java.getResource("/Day05-input.txt")!!

    @Test
    fun testParse() {
        val (cargoStacks, commands) = Day05.parse(inputFile)
        commands.forEach { it.execute(cargoStacks) }
        println(cargoStacks.map { (i, s) -> s.peek() }.joinToString(""))
    }
}
package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL
import java.util.*
import kotlin.test.assertEquals

class Day05Test {
    private val inputFile: URL = Day01::class.java.getResource("/Day05-input.txt")!!

    @Test
    fun testDay05part1() {
        val (cargoStacks, commands) = Day05.parse(inputFile)
        commands.forEach { it.execute(cargoStacks) }
        assertEquals("CFFHVVHNC", peek(cargoStacks))
    }

    private fun peek(cargoStacks: Map<Int, Stack<Char>>) =
        cargoStacks.map { (i, s) -> s.peek() }.joinToString("")
}
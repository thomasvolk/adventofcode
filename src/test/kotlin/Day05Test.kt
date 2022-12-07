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
    @Test
    fun testDay05part2() {
        val (cargoStacks, commands) = Day05.parse(inputFile, Day05.CrateMover9001())
        commands.forEach { it.execute(cargoStacks) }
        assertEquals("FSZWBPTBG", peek(cargoStacks))
    }

    private fun peek(cargoStacks: Map<Int, Stack<Char>>) =
        cargoStacks.map { (i, s) -> s.peek() }.joinToString("")
}
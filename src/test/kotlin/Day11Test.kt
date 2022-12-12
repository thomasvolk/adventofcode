package net.t53k

import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.net.URL
import kotlin.test.assertEquals

class Day11Test {
    private val inputFile: URL = PuzzleInput.loadFile("/Day11-input.txt")
    @Test
    fun testDay11Part1() {
        val monkeys = Day11.Monkey.parse(inputFile.readText(), 3L.toBigInteger())
        assertEquals(8, monkeys.count())
        val game = Day11.KeepAwayGame(monkeys)
        (1..20).forEach{ _ -> game.round() }
        val monkeyBusiness = monkeys.map { it.itemsInspected() }.sortedDescending().subList(0, 2).reduce { a, b -> a * b}
        assertEquals(117640L.toBigInteger(), monkeyBusiness)
    }

    @Test
    fun testDay11Part2() {
        val monkeys = Day11.Monkey.parse(inputFile.readText(), BigInteger.ONE)
        assertEquals(8, monkeys.count())
        val game = Day11.KeepAwayGame(monkeys)
        (1..10000).forEach{ _ -> game.round() }
        val monkeyBusiness = monkeys.map { it.itemsInspected() }.sortedDescending().subList(0, 2).reduce { a, b -> a * b}
        assertEquals("30616425600".toBigInteger(), monkeyBusiness)
    }
}
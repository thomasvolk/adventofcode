package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals

class Day02Test {
    private val inputFile: URL = Day01::class.java.getResource("/Day02-input.txt")!!

    @Test
    fun testPair() {
        val pairs = listOf(
            Pair(Choice.ROCK, Choice.PAPER),
            Pair(Choice.PAPER, Choice.ROCK),
            Pair(Choice.SCISSORS, Choice.SCISSORS)
        )
        assertEquals(15, pairs.map { Choice.score(it) }.sum())
    }

    @Test
    fun testDay02Part1() {
        val totalScope = Day02.totalScopeSimpleMapping(inputFile)
        assertEquals(13565, totalScope)
    }

    @Test
    fun testDay02Part2() {
        val totalScope = Day02.totalScopeAdvancedMapping(inputFile)
        assertEquals(12424, totalScope)
    }
}
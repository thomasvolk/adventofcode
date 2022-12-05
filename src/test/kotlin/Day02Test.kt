package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals

class Day02Test {
    private val inputFile: URL = Day01::class.java.getResource("/Day02-input.txt")!!

    @Test
    fun testPair() {
        val pairs = listOf(
            Pair(Guess.ROCK, Guess.PAPER),
            Pair(Guess.PAPER, Guess.ROCK),
            Pair(Guess.SCISSORS, Guess.SCISSORS)
        )
        assertEquals(15, pairs.map { Guess.round(it) }.sum())
    }

    @Test
    fun testDay02Part1() {
        val totalScope = Day02.totalScopeSimpleMapping(inputFile)
        assertEquals(13565, totalScope)
    }
}
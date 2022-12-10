package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals


class Day01Test {
    private val inputFile: URL = PuzzleInput.loadFile("/Day01-input.txt")
    @Test
    fun testDay01Part1() {
        val elfWithMostCalories = Day01.elfsFromURL(inputFile)
            .maxByOrNull { e -> e.calories() }!!
        println("$elfWithMostCalories calories: ${elfWithMostCalories.calories()}")
        assertEquals(74711, elfWithMostCalories.calories())
    }

    @Test
    fun testDay01Part2() {
        val firstThreeElfsWithMostCalories = Day01.elfsFromURL(inputFile)
            .sortedByDescending { e -> e.calories() }
            .subList(0, 3)
        println(firstThreeElfsWithMostCalories)
        assertEquals(209481, firstThreeElfsWithMostCalories.sumOf { it.calories() })
    }
}
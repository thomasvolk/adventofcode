package net.t53k

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day04Test {
    private val inputFile: URL = Day01::class.java.getResource("/Day04-input.txt")!!

    @Test
    fun testParse() {
        assertEquals(Pair((23..34), (1..9)), Day04.parseLine("23-34,1-9"))
    }
    @Test
    fun testContaining() {
        assertTrue(Day04.ElfPair((23..34), (23..34)).sectionContaining())
        assertTrue(Day04.ElfPair((4..5), (2..6)).sectionContaining())
        assertTrue(Day04.ElfPair((2..6), (4..5)).sectionContaining())
        assertFalse(Day04.ElfPair((23..34), (22..30)).sectionContaining())
        assertFalse(Day04.ElfPair((10..40), (400..3800)).sectionContaining())
    }
    @Test
    fun testOverlapping() {
        assertTrue(Day04.ElfPair((23..34), (23..34)).sectionOverlapping())
        assertTrue(Day04.ElfPair((4..5), (2..6)).sectionOverlapping())
        assertTrue(Day04.ElfPair((2..6), (4..5)).sectionOverlapping())
        assertTrue(Day04.ElfPair((23..34), (22..30)).sectionOverlapping())
        assertTrue(Day04.ElfPair((22..30), (23..34)).sectionOverlapping())
        assertFalse(Day04.ElfPair((10..40), (400..3800)).sectionOverlapping())
        assertFalse(Day04.ElfPair((10..40), (4..5)).sectionOverlapping())
    }
    @Test
    fun testDay04Part1() {
        assertEquals(580, Day04.parse(inputFile).count { it.sectionContaining() })
    }
    @Test
    fun testDay04Part2() {
        assertEquals(895, Day04.parse(inputFile).count { it.sectionOverlapping() })
    }
}
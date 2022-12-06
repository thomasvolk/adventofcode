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
    fun testElfPair() {
        assertTrue(Day04.ElfPair((23..34), (23..34)).sectionContained())
        assertTrue(Day04.ElfPair((4..5), (2..6)).sectionContained())
        assertTrue(Day04.ElfPair((2..6), (4..5)).sectionContained())
        assertFalse(Day04.ElfPair((23..34), (22..30)).sectionContained())
        assertFalse(Day04.ElfPair((10..40), (400..3800)).sectionContained())
    }
    @Test
    fun testDay04() {
        assertEquals(580, Day04.parse(inputFile).count { it.sectionContained() })
    }
}
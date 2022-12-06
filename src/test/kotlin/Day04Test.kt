package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals

class Day04Test {
    private val inputFile: URL = Day01::class.java.getResource("/Day04-input.txt")!!

    @Test
    fun testParse() {
        assertEquals(Pair((23..34), (1..9)), Day04.parseLine("23-34,1-9"))
    }
}
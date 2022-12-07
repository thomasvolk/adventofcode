package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL

class Day05Test {
    private val inputFile: URL = Day01::class.java.getResource("/Day05-input.txt")!!

    @Test
    fun testParse() {
        Day05.parse(inputFile)
    }
}
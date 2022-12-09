package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals

class Day08Test {
    private val inputFile: URL = Day01::class.java.getResource("/Day08-input.txt")!!

    val testData = """
        30373
        25512
        65332
        33549
        35390
    """.trimIndent()


    @Test
    fun testParser() {
        val forest = Day08.parse(testData.split("\n"))
        assertEquals(5, forest.rowCount)
        assertEquals(5, forest.colCount)
    }
}
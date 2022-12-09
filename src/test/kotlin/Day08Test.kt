package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
        assertEquals(5, forest.rowCount())
        assertEquals(5, forest.colCount())
        assertEquals(25, forest.count())
        val tree = forest.findTree(3, 4)!!
        assertEquals(9, tree.height)
        assertEquals(4, tree.neighbour(Day08.Direction.EAST)!!.height)
        assertEquals(0, tree.neighbour(Day08.Direction.SOUTH)!!.height)

        val invisibleTree = forest.findTree(1, 3)!!
        assertTrue(invisibleTree.isInvisible())
        val invisibleTree2 = forest.findTree(3, 3)!!
        assertTrue(invisibleTree2.isInvisible())
        val visibleTree = forest.findTree(2, 1)!!
        assertFalse(visibleTree.isInvisible())
        assertEquals(21, forest.allTrees().count { !it.isInvisible() })
    }
    @Test
    fun testDay08Part1() {
        val forest = Day08.parse(inputFile)
        assertEquals(1763, forest.allTrees().count { !it.isInvisible() })
    }
}
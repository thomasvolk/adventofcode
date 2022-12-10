package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals

class Day07Test {
    private val inputFile: URL = PuzzleInput.loadFile("/Day07-input.txt")

    private val testData = """
        ${'$'} cd /
        ${'$'} ls
        dir a
        14848514 b.txt
        8504156 c.dat
        dir d
        ${'$'} cd a
        ${'$'} ls
        dir e
        29116 f
        2557 g
        62596 h.lst
        ${'$'} cd e
        ${'$'} ls
        584 i
        ${'$'} cd ..
        ${'$'} cd ..
        ${'$'} cd d
        ${'$'} ls
        4060174 j
        8033020 d.log
        5626152 d.ext
        7214296 k
    """.trimIndent()

    @Test
    fun testParser() {
        val root = Day07.parse(testData)
        assertEquals("/, a, e, d", root.findAllDirectories().joinToString { it.name })
        assertEquals(95437, root.findAllDirectories().map { it.size() }.filter { it <= 100000 }.sum())
    }

    @Test
    fun testDay07part1() {
        val root = Day07.parse(inputFile)
        assertEquals(1491614, root.findAllDirectories().map { it.size() }.filter { it <= 100000 }.sum())
    }
    @Test
    fun testDay07part2() {
        val root = Day07.parse(inputFile)
        val totalDiskSpace = 70000000
        val usedSpace = root.size()
        val freeSpace = totalDiskSpace - usedSpace
        val updateSize = 30000000
        val neededSpaceForUpdate = updateSize - freeSpace
        assertEquals(6090134, neededSpaceForUpdate)
        val candidates = root.findAllDirectories()
            .map { it.size() }
            .filter { it >= neededSpaceForUpdate }
            .sorted()
        assertEquals(6400111, candidates.first())
    }
}
package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL

class Day07Test {
    private val inputFile: URL = Day01::class.java.getResource("/Day07-input.txt")!!

    val testData = """
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
        val node = Day07.parse(testData)
    }
}
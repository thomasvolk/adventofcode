package net.t53k

import java.lang.RuntimeException

object Day07 {
    interface Node {
        fun size(): Long
        fun name(): String
    }

    data class Dir(val name: String) : Node {
        val children = MutableList<Node>()
        var parent: Dir? = null
        override fun size(): Long {
            return children.sumOf { it.size() }
        }

        override fun name(): String = name
        fun moveUp(): Dir? = parent

        fun moveDown(name: String): Dir {
            return children.filterIsInstance<Dir>().find { it.name() == name }!!
        }
    }

    data class File(val name: String, val size: Long): Node {
        override fun size(): Long = size
        override fun name(): String = name
    }

    class Parser(
        private val onCd: (Dir) -> Unit,
        private val onDir: (Dir) -> Unit,
        private val onFile: (File) -> Unit,
        private val onLs: (() -> Unit)
    ) {
        private val regexCd = "\\$ cd (.+$)".toRegex()
        private val regexDir = "dir (.+$)".toRegex()
        private val regexFile = "([0-9]+) (.+$)".toRegex()
        private val regexLs = "\\$ ls".toRegex()

        private fun parse(line: String) {
            val matchCd =  regexCd.find(line)
            val matchDir = regexDir.find(line)
            val matchFile = regexFile.find(line)
            val matchLs = regexLs.find(line)
            if(matchCd != null) {
                onCd(Dir(matchCd.groupValues[0]))
            }
            else if(matchDir != null) {
                onDir(Dir(matchDir.groupValues[0]))
            }
            else if(matchFile != null) {
                onFile(File(matchFile.groupValues[0], matchFile.groupValues[0].toLong()))
            }
            else if(matchLs != null) {
                onLs()
            }
            else {
                throw RuntimeException("can not parse line $line")
            }
        }
    }

    private fun parse(input: List<String>): Node {
        TODO("Not yet implemented")
    }
    fun parse(input: String): Node {
        return parse(input.split("\n"))
    }
}
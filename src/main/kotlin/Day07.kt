package net.t53k

object Day07 {
    interface Node {
        fun size(): Long
        fun name(): String
    }
    private val regexCd = "\\$ cd (.+$)".toRegex()
    private val regexDir = "dir (.+$)".toRegex()
    private val regexFile = "([0-9]+) (.+$)".toRegex()

    data class Dir(val name: String, val children: MutableList<Node>, val parent: Dir): Node {
        override fun size(): Long {
            return children.sumOf { it.size() }
        }

        override fun name(): String = name
        fun moveUp(): Dir = parent

        fun moveDown(name: String): Dir {
            return children.filterIsInstance<Dir>().find { it.name() == name }!!
        }
    }

    data class File(val name: String, val size: Long): Node {
        override fun size(): Long = size
        override fun name(): String = name
    }

    private fun parse(input: List<String>): Node {
        input.mapNotNull { regexCd.find(it) }.forEach { println(it.groupValues[1]) }
        println("---------")
        input.mapNotNull { regexDir.find(it) }.forEach { println(it.groupValues[1]) }
        println("---------")
        input.mapNotNull { regexFile.find(it) }.forEach { println(it.groupValues[2]) }
        TODO("implement")
    }

    fun parse(input: String): Node {
        return parse(input.split("\n"))
    }
}
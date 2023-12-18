package net.t53k

import java.io.File
import java.lang.RuntimeException
import java.net.URL

object Day07 {
    interface Node {
        fun size(): Long
        fun name(): String
    }

    data class Dir(val name: String) : Node {
        private val files = mutableMapOf<String, File>()
        private val dirs = mutableMapOf<String, Dir>()
        var parent: Dir? = null
        override fun size(): Long {
            return files.values.sumOf { it.size() } + dirs.values.sumOf { it.size() }
        }

        fun addFile(f: File): File {
            return files.getOrPut(f.name) { f }
        }

        fun addDir(d: Dir): Dir {
            return dirs.getOrPut(d.name) { d }
        }

        override fun name(): String = name

        fun findAllDirectories(): List<Dir> {
            return listOf(this) + dirs.values.flatMap { it.findAllDirectories() }
        }
    }

    data class File(val name: String, val size: Long): Node {
        override fun size(): Long = size
        override fun name(): String = name
    }

    class Parser(
        private val onCdDown: (Dir) -> Unit,
        private val onCdUp: () -> Unit,
        private val onDir: (Dir) -> Unit,
        private val onFile: (File) -> Unit,
        private val onLs: (() -> Unit)
    ) {
        private val regexCdDown = "\\$ cd (.+$)".toRegex()
        private val regexCdUp = "\\$ cd \\.\\.".toRegex()
        private val regexDir = "dir (.+$)".toRegex()
        private val regexFile = "([0-9]+) (.+$)".toRegex()
        private val regexLs = "\\$ ls".toRegex()

        fun parse(line: String) {
            val matchCdDown =  regexCdDown.find(line)
            val matchCdUp =  regexCdUp.find(line)
            val matchDir = regexDir.find(line)
            val matchFile = regexFile.find(line)
            val matchLs = regexLs.find(line)
            if(matchCdUp != null) {
                onCdUp()
            }
            else if(matchCdDown != null) {
                onCdDown(Dir(matchCdDown.groupValues[1]))
            }
            else if(matchDir != null) {
                onDir(Dir(matchDir.groupValues[1]))
            }
            else if(matchFile != null) {
                onFile(File(matchFile.groupValues[2], matchFile.groupValues[1].toLong()))
            }
            else if(matchLs != null) {
                onLs()
            }
            else {
                throw RuntimeException("can not parse line $line")
            }
        }
    }

    private const val rootDirName = "/"

    private fun parse(input: List<String>): Dir {
        val root = Dir(rootDirName)
        var wd = root
        val parser = Parser(
            { changeDirDown ->
                if(changeDirDown.name != rootDirName) {
                    val current  = wd.addDir(changeDirDown)
                    current.parent = wd
                    wd = current
                }
            },
            {
                wd = wd.parent!!
            },
            { dir -> wd.addDir(dir) },
            { file -> wd.addFile(file) },
            {}
        )
        input.forEach(parser::parse)
        return root
    }
    fun parse(input: String): Dir {
        return parse(input.split("\n"))
    }

    fun parse(url: URL): Dir {
        File(url.toURI()).useLines { lines ->
            return parse(lines.toList())
        }
    }
}
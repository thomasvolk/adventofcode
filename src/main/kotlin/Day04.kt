package net.t53k

object Day04 {
    fun parseLine(line: String): Pair<IntRange, IntRange> {
        val regex = "([0-9]+)\\-([0-9]+),([0-9]+)\\-([0-9]+)".toRegex()
        val elfAstart = regex.find(line)!!.groupValues[1].toInt()
        val elfAend = regex.find(line)!!.groupValues[2].toInt()
        val elfBstart = regex.find(line)!!.groupValues[3].toInt()
        val elfBend = regex.find(line)!!.groupValues[4].toInt()
        return Pair((elfAstart..elfAend), (elfBstart..elfBend))
    }
}
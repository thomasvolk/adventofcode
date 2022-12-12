package net.t53k

import java.io.File
import java.net.URL

object Day04 {
    fun parseLine(line: String): Pair<IntRange, IntRange> {
        val regex = "([0-9]+)-([0-9]+),([0-9]+)-([0-9]+)".toRegex()
        val elfAstart = regex.find(line)!!.groupValues[1].toInt()
        val elfAend = regex.find(line)!!.groupValues[2].toInt()
        val elfBstart = regex.find(line)!!.groupValues[3].toInt()
        val elfBend = regex.find(line)!!.groupValues[4].toInt()
        return Pair((elfAstart..elfAend), (elfBstart..elfBend))
    }

    private fun sectionContaining(a: IntRange, b: IntRange): Boolean {
        return a.first >= b.first  && a.last <= b.last
    }

    private fun sectionOverlapping(a: IntRange, b: IntRange): Boolean {
        return sectionContaining(a, b)
                || (a.first < b.first && a.last <= b.last && a.last >= b.first)
                || (a.last > b.last && a.first >= b.first && a.first <= b.last)
    }


    data class ElfPair(val sectionA: IntRange, val sectionB: IntRange) {

        fun sectionContaining(): Boolean {
           return sectionContaining(sectionA, sectionB) || sectionContaining(sectionB, sectionA)
        }
        fun sectionOverlapping(): Boolean {
            return sectionOverlapping(sectionA, sectionB) || sectionOverlapping(sectionB, sectionA)
        }
    }
    fun parse(url: URL): List<ElfPair> {
        File(url.toURI()).useLines { lines ->
            return lines.toList()
                .map { parseLine(it) }
                .map { ElfPair(it.first, it.second) }
        }
    }
}
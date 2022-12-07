package net.t53k

import java.io.File
import java.net.URL

object Day05 {
    data class CargoMap(val cargoStacks: Map<Int, ArrayDeque<String>>)
    fun parse(url: URL) {
        val text = File(url.toURI()).readText()
        val (map, commands) = text.split("\n\n", limit = 2)
        println(map.split("\n").last())
        println("---------------------------")
        println(commands)
    }
}
package net.t53k

import java.io.File
import java.net.URL

object Day05 {
    data class CargoMap(val cargoStacks: Map<Int, ArrayDeque<String>>)
    fun parse(url: URL) {
        val text = File(url.toURI()).readText()
        val (map, commands) = text.split("\n\n", limit = 2)

        println(map)
        val mapLines = map.split("\n")
        val stackRow = mapLines.last()
        val stackPositions = stackRow.toList()
            .mapIndexed { i, c -> i to c }
            .filter { it.second != ' ' }
            .associate { it }
        println(stackPositions)
        val regexCargo = "[A-Z]".toRegex()
        val stacks = stackPositions
            .map { (pos, name) ->
                name to mapLines
                    .filter { it.length > pos }
                    .map { it[pos] }
                    .filter { regexCargo.matches(it.toString()) }
            }
            .associate { it }

        println(stacks)
        println("---------------------------")
        println(commands)
    }
}
package net.t53k

import java.io.File
import java.net.URL
import java.util.*

object Day05 {
    interface Command {
        fun execute(cargoStacks:  Map<Int, Stack<Char>>)
    }
    data class MoveCommand(val amount: Int, val from: Int, val to: Int) : Command {
        override fun execute(cargoStacks: Map<Int, Stack<Char>>) {
            for(i in 1..amount) {
                val c = cargoStacks[from]!!.pop()
                cargoStacks[to]!!.push(c)
            }
        }
    }

    fun parse(url: URL): Pair<Map<Int, Stack<Char>>, List<Command>> {
        val text = File(url.toURI()).readText()
        val (stackInput, commandsInput) = text.split("\n\n", limit = 2)

        val stacks = parseStacks(stackInput)
        val commands = parseCommands(commandsInput)
        return Pair(stacks, commands)
    }

    private fun parseCommands(commands: String): List<Command> {
        val regexCmd = "move ([0-9]+) from ([0-9]+) to ([0-9]+)".toRegex()
        return commands.trim()
            .split("\n")
            .mapNotNull { regexCmd.find(it) }
            .map { it.groupValues }
            .map { MoveCommand(it[1].toInt(), it[2].toInt(), it[3].toInt()) }
    }

    private fun parseStacks(map: String): Map<Int, Stack<Char>> {
        val mapLines = map.split("\n")
        val stackRow = mapLines.last()
        val stackPositions = stackRow.toList()
            .mapIndexed { i, c -> i to c }
            .filter { it.second != ' ' }
            .associate { it }
        val regexCargo = "[A-Z]".toRegex()
        val stacks = stackPositions
            .map { (pos, name) ->
                val stack = Stack<Char>()
                stack.addAll(mapLines
                    .filter { it.length > pos }
                    .map { it[pos] }
                    .filter { regexCargo.matches(it.toString()) }
                    .reversed()
                )
                name.toString().toInt() to stack
            }
            .associate { it }
        return stacks
    }
}
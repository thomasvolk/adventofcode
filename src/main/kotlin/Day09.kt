package net.t53k

import java.io.File
import java.lang.IllegalArgumentException
import java.net.URL

object Day09 {
    data class Position(val x: Int, val y: Int)

    enum class Direction {
        UP, DOWN, LEFT, RIGHT;
        companion object {
            private val mapping = mapOf(
                "U" to UP,
                "D" to DOWN,
                "L" to LEFT,
                "R" to RIGHT
                )
            fun get(shortName: String): Direction {
                mapping[shortName]?.let { d ->
                    return d
                }
                throw IllegalArgumentException("no direction found for $shortName")
            }
        }
    }
    class Rope {
        private var head: Position
        private var tail: Position
        private var tailHistory: Set<Position> = setOf()
        constructor(start: Position) {
            head = start
            tail = start
        }

        fun moveHead(direction: Direction, amount: Int) {
            println("$direction, $amount")
            //TODO("implement")
        }

        fun execute(moves: List<Pair<Direction, Int>>) {
            moves.forEach { (d, a) -> moveHead(d, a) }
        }

        fun tailHistory() = tailHistory
    }

    fun parse(lines: List<String>): List<Pair<Direction, Int>> {
        return lines
            .map { it.trim() }
            .map { it.split(" ") }
            .map { Pair(Direction.get(it.first()), it.last().toInt()) }
    }
    fun parse(url: URL): List<Pair<Direction, Int>> {
        File(url.toURI()).useLines { lines ->
            return parse(lines.toList())
        }
    }

    fun rope(x: Int, y: Int): Rope = Rope(Position(x, y))
}
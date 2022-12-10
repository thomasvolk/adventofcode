package net.t53k

import java.io.File
import java.lang.IllegalArgumentException
import java.net.URL
import kotlin.math.absoluteValue

object Day09 {

    data class Distance(val deltaX: Int, val deltaY: Int) {
        fun isNextToEachOther(): Boolean {
            return if(isDiagonally()) {
                (deltaX.absoluteValue + deltaY.absoluteValue) == 2
            }
            else {
                (deltaX.absoluteValue + deltaY.absoluteValue) <= 1
            }
        }

        fun isDiagonally(): Boolean = deltaX != 0 && deltaY != 0

        fun isNotNextToEachOther(): Boolean {
           return !isNextToEachOther()
        }
    }
    data class Position(val x: Int, val y: Int) {
        fun distanceTo(other: Position): Distance {
            return Distance(other.x - x, other.y - y)
        }

        fun move(direction: Direction): Position {
            return when(direction) {
                Direction.UP ->  this.copy(y = y + 1)
                Direction.DOWN ->  this.copy(y = y - 1)
                Direction.RIGHT ->  this.copy(x = x + 1)
                Direction.LEFT ->  this.copy(x = x - 1)
            }
        }
    }

    class Knot(var position: Position) {
        fun move(direction: Direction) {
            position = position.move(direction)
        }

        fun pullTowardTo(knot: Knot) {
            val distance = position.distanceTo(knot.position)
            if(distance.isNotNextToEachOther()) {
                pullTowardTo(distance)
            }
        }

        private fun pullTowardTo(distance: Distance) {
            val minYXDistance =
                if(distance.isDiagonally()) { 1 } else { 2 }
            if(distance.deltaX.absoluteValue >= minYXDistance) {
                position = if(distance.deltaX > 0) {
                    position.move(Direction.RIGHT)
                } else {
                    position.move(Direction.LEFT)
                }
            }
            if(distance.deltaY.absoluteValue >= minYXDistance) {
                position = if(distance.deltaY > 0) {
                    position.move(Direction.UP)
                } else {
                    position.move(Direction.DOWN)
                }
            }
        }
    }

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
    class Rope(start: Position) {
        private var head: Knot = Knot(start)
        private var tail: Knot = Knot(start)
        private var tailHistory: Set<Position> = setOf(start)

        fun moveHead(direction: Direction) {
            head.move(direction)
            tail.pullTowardTo(head)
            tailHistory = tailHistory + tail.position
        }

        fun moveHead(direction: Direction, amount: Int) {
            (1..amount).forEach { moveHead(direction) }
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
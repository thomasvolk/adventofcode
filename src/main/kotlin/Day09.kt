package net.t53k

import java.io.File
import java.lang.IllegalArgumentException
import java.lang.Math.abs
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
        private var head: Position = start
        private var tail: Position = start
        private var tailHistory: Set<Position> = setOf(start)

        fun moveHead(direction: Direction) {
            head = head.move(direction)
            val distance = tail.distanceTo(head)
            if(distance.isNotNextToEachOther()) {
                moveTail(distance)
                tailHistory = tailHistory + tail
            }
        }

        private fun moveTail(distance: Distance) {
            val minYXDistance =
                if(distance.isDiagonally()) { 1 } else { 2 }
            if(distance.deltaX.absoluteValue >= minYXDistance) {
                tail = if(distance.deltaX > 0) {
                    tail.move(Direction.RIGHT)
                } else {
                    tail.move(Direction.LEFT)
                }
            }
            if(distance.deltaY.absoluteValue >= minYXDistance) {
                tail = if(distance.deltaY > 0) {
                    tail.move(Direction.UP)
                } else {
                    tail.move(Direction.DOWN)
                }
            }
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
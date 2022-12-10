package net.t53k

import java.io.File
import java.lang.IllegalArgumentException
import java.net.URL
import kotlin.math.absoluteValue

object Day09 {

    data class Distance(val deltaX: Int, val deltaY: Int) {
        private fun isNextToEachOther(): Boolean {
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

    abstract class Knot(var position: Position) {
        fun position(): Position = position
    }

    class Head(position: Position, val next: Tail): Knot(position) {
        fun move(direction: Direction) {
            position = position.move(direction)
            next.pullTowardTo(this)
        }
    }

    class Tail(position: Position, val next: Tail? = null): Knot(position) {
        private fun move(direction: Direction) {
            position = position.move(direction)
        }
        fun pullTowardTo(knot: Knot) {
            val distance = position.distanceTo(knot.position())
            if(distance.isNotNextToEachOther()) {
                pullToward(distance)
            }
            next?.let {it.pullTowardTo(this) }
        }

        private fun pullToward(distance: Distance) {
            val minYXDistance =
                if(distance.isDiagonally()) { 1 } else { 2 }
            if(distance.deltaX.absoluteValue >= minYXDistance) {
                if(distance.deltaX > 0) {
                    move(Direction.RIGHT)
                } else {
                    move(Direction.LEFT)
                }
            }
            if(distance.deltaY.absoluteValue >= minYXDistance) {
                if(distance.deltaY > 0) {
                    move(Direction.UP)
                } else {
                    move(Direction.DOWN)
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
    class Rope(start: Position, tailLength: Int = 1) {
        private var tail: Tail
        private var head: Head
        private var tailHistory: Set<Position> = setOf(start)

        init {
            if(tailLength < 1) throw IllegalArgumentException("tailLength must be >= 1")
            tail = Tail(start)
            var current = tail
            for(i in 1 until tailLength) {
              current = Tail(start, current)
            }
            head = Head(start, current)
        }

        private fun moveHead(direction: Direction, amount: Int) {
            (1..amount).forEach { _ ->
                head.move(direction)
                tailHistory = tailHistory + tail.position
            }
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
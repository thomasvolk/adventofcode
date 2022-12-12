package net.t53k

import java.lang.IllegalArgumentException
import java.net.URL

object Day12 {

    enum class Direction {
        EAST, WEST, NORTH, SOUTH
    }

    data class Position(val height: Char) {
        private var neighbours = mapOf<Direction, Position>()
        fun addNeighbour(position: Position, direction: Direction) {
            neighbours = neighbours + (direction to position)
        }
        fun neighours() = neighbours.values
        override fun toString(): String {
            return "Position(height=$height, neighbours=${neighbours.keys})"
        }
    }

    data class Map(val start: Position, val end: Position, val positions: List<Position>){
        companion object {
            fun parse(input: String): Map {
                var start: Position? = null
                var end: Position? = null
                val positions = input.split("\n").map { line ->
                    line.toList().map { c ->
                        when (c) {
                            'S' -> {
                                val s = Position('a')
                                start = s
                                s
                            }

                            'E' -> {
                                val e = Position('z')
                                end = e
                                e
                            }
                            else -> Position(c)
                        }
                    }
                }
                // TODO: assign neighbours
                start?.let { s ->
                    end?.let { e ->
                        return Map(s, e, positions.flatten())
                    }
                    throw IllegalArgumentException("input map has no end point 'E' - $input")
                }
                throw IllegalArgumentException("input map has no start point 'S' - $input")
            }
        }
    }

    fun stepsForShortestPath(input: String): Long {
        val map = Map.parse(input)

        println(map)
        TODO("implement path algorithm")
    }

    fun stepsForShortestPath(input: URL): Long {
        return stepsForShortestPath(input.readText())
    }
}
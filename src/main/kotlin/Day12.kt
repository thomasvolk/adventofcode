package net.t53k

import java.lang.IllegalArgumentException
import java.net.URL

object Day12 {

    enum class Direction {
        EAST {
            override fun transformCoordinates(x: Int, y: Int): Pair<Int, Int> = Pair(x + 1, y)
        },
        WEST {
            override fun transformCoordinates(x: Int, y: Int): Pair<Int, Int> = Pair(x - 1, y)
        },
        NORTH {
            override fun transformCoordinates(x: Int, y: Int): Pair<Int, Int> = Pair(x, y - 1)
        },
        SOUTH {
            override fun transformCoordinates(x: Int, y: Int): Pair<Int, Int> = Pair(x, y + 1)
        };

        abstract fun transformCoordinates(x: Int, y: Int): Pair<Int, Int>

        fun findNeighbour(x: Int, y: Int, positions: List<List<Position>>): Pair<Direction, Position>? {
            val (nx, ny) = transformCoordinates(x, y)
            if(nx>= 0 && ny >= 0 && ny < positions.count()) {
                val row = positions[ny]
                if(nx < row.count()) {
                    return Pair(this, row[nx])
                }
            }
            return null
        }
    }

    data class Coordinates(val x:Int, val y:Int, val height: Char)

    class Position(val coordinates: Coordinates, val end: Boolean = false) {
        var neighbours = mapOf<Direction, Position>()

        fun findPaths(path: List<Position> = listOf()): List<List<Position>> {
            val newPath = path + this
            if(end) {
                return listOf(newPath)
            }

            val candidates = neighbours.values
                .filter { n -> (coordinates.height + 1) >= n.coordinates.height   }
                .filter { n -> !path.contains(n) }

            return candidates.map { n -> n.findPaths(newPath) }
                .filter { p -> p.isNotEmpty() }
                .flatten()
        }
        override fun toString(): String {
            return "Position($coordinates)"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Position

            if (coordinates != other.coordinates) return false

            return true
        }

        override fun hashCode(): Int {
            return coordinates.hashCode()
        }
    }

    data class Map(val start: Position, val end: Position, val positions: List<Position>){
        companion object {
            fun parse(input: String): Map {
                var start: Position? = null
                var end: Position? = null
                val positions = input.split("\n").mapIndexed{ y, line ->
                    line.toList().mapIndexed { x, c ->
                        when (c) {
                            'S' -> {
                                val s = Position(Coordinates(x, y, 'a'))
                                start = s
                                s
                            }

                            'E' -> {
                                val e = Position(Coordinates(x, y, 'z'), end = true)
                                end = e
                                e
                            }
                            else -> Position(Coordinates(x, y, c))
                        }
                    }
                }
                positions.forEachIndexed { row, cols ->
                    cols.forEachIndexed { col, position ->
                        position.neighbours =
                            Direction.values()
                            .mapNotNull { it.findNeighbour(col, row, positions) }
                            .associate { it }
                    }
                }
                start?.let { s ->
                    end?.let { e ->
                        return Map(s, e, positions.flatten())
                    }
                    throw IllegalArgumentException("input map has no end point 'E' - $input")
                }
                throw IllegalArgumentException("input map has no start point 'S' - $input")
            }
        }

        fun findPaths(): List<List<Position>> {
            return start.findPaths().filter { it.contains(end) }
        }
    }

    fun stepsForShortestPath(input: String): Int {
        val map = Map.parse(input)
        val pathsFound = map.findPaths().map { it.count() - 1 }.sorted()
        return pathsFound.first()
    }

    fun stepsForShortestPath(input: URL): Int {
        return stepsForShortestPath(input.readText())
    }
}
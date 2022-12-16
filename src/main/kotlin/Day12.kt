package net.t53k

import java.lang.IllegalArgumentException
import java.net.URL

object Day12 {

    enum class Direction {
        EAST {
            override fun transformCoordinates(coordinates: Coordinates): Coordinates = Coordinates(coordinates.x + 1, coordinates.y)
        },
        WEST {
            override fun transformCoordinates(coordinates: Coordinates): Coordinates = Coordinates(coordinates.x - 1, coordinates.y)
        },
        NORTH {
            override fun transformCoordinates(coordinates: Coordinates): Coordinates = Coordinates(coordinates.x, coordinates.y - 1)
        },
        SOUTH {
            override fun transformCoordinates(coordinates: Coordinates): Coordinates = Coordinates(coordinates.x, coordinates.y + 1)
        };

        abstract fun transformCoordinates(coordinates: Coordinates): Coordinates

        fun findNeighbour(coordinates: Coordinates, positions: List<List<Position>>): Pair<Direction, Position>? {
            val (nx, ny) = transformCoordinates(coordinates)
            if(nx>= 0 && ny >= 0 && ny < positions.count()) {
                val row = positions[ny]
                if(nx < row.count()) {
                    return Pair(this, row[nx])
                }
            }
            return null
        }
    }

    data class Coordinates(val x: Int, val y: Int)

    open class Position(val coordinates: Coordinates, private val height: Char) {
        private var neighbours = mapOf<Direction, Position>()

        fun neighbours(n: Map<Direction, Position>) {
            neighbours = n
        }
        fun neighbours() = neighbours.values
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

        override fun toString(): String {
            return "${this.javaClass.simpleName}(coordinates=$coordinates, height=$height)"
        }

    }

    class End(coordinates: Coordinates, height: Char): Position(coordinates, height)

    data class HeightMap(val start: Position, val end: Position, val positions: List<List<Position>>) {

        fun findPath(): Int {
            val explored = mutableSetOf<Position>()
            explored.add(start)
            val queue = ArrayDeque<Position>()
            queue.addFirst(start)
            var count = 1
            while(queue.isNotEmpty()) {
                count++
                val v = queue.removeFirst()
                if(v is End) {
                    println("found the end")
                    return count
                }
                for(n in v.neighbours()) {
                    if(!explored.contains(n)) {
                        explored.add(n)
                        queue.addFirst(n)
                    }
                }

            }
            return count
        }

        companion object {
            fun create(input: String): HeightMap {
                var start: Position? = null
                var end: Position? = null
                val positions = input.split("\n").mapIndexed { y, line ->
                    line.toList().mapIndexed { x, c ->
                        when (c) {
                            'S' -> {
                                val s = Position(Coordinates(x, y), 'a')
                                start = s
                                s
                            }

                            'E' -> {
                                val e = End(Coordinates(x, y), 'z')
                                end = e
                                e
                            }

                            else -> Position(Coordinates(x, y), c)
                        }
                    }
                }
                positions.forEachIndexed { row, cols ->
                    cols.forEachIndexed { col, position ->
                        position.neighbours(
                            Direction.values()
                                .mapNotNull { it.findNeighbour(Coordinates(col, row), positions) }
                                .associate { it }
                        )
                    }
                }
                start?.let { s ->
                    end?.let { e ->
                        return HeightMap(s, e, positions)
                    }
                    throw IllegalArgumentException("input map has no end point 'E' - $input")
                }
                throw IllegalArgumentException("input map has no start point 'S' - $input")
            }
        }
    }

    fun stepsForShortestPath(input: String): Int {
        val map = HeightMap.create(input)
        return map.findPath()
    }

    fun stepsForShortestPath(input: URL): Int {
        return stepsForShortestPath(input.readText())
    }
}
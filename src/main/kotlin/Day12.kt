package net.t53k

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

        fun findNeighbour(coordinates: Coordinates, positions: List<List<Position>>): Position? {
            val (nx, ny) = transformCoordinates(coordinates)
            if(nx>= 0 && ny >= 0 && ny < positions.count()) {
                val row = positions[ny]
                if(nx < row.count()) {
                    return row[nx]
                }
            }
            return null
        }
    }

    data class Coordinates(val x: Int, val y: Int)

    open class Position(val coordinates: Coordinates, val height: Char) {
        var neighbours = listOf<Position>()
        var explored = false
        var parent: Position? = null

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

    class HeightMap(input: String) {
        private lateinit var start: Position
        private lateinit var end: Position
        private var positions: List<List<Position>>

        init {
            positions = input.split("\n").mapIndexed { y, line ->
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
                    position.neighbours =
                        Direction.values()
                            .mapNotNull { it.findNeighbour(Coordinates(col, row), positions) }
                            .filter { it.height <= (position.height + 1) }
                            .sortedBy { it.height }
                }
            }
        }

        private fun reset() {
            positions.forEach { row ->
                row.forEach { position ->
                    position.explored = false
                    position.parent = null
                }
            }
        }

        fun findPath(): Int {
            reset()
            start.explored = true
            val queue = ArrayDeque<Position>()
            queue.addFirst(start)
            while(queue.isNotEmpty()) {
                val v = queue.removeFirst()
                if(v is End) {
                    return path(end).count()
                }
                for(n in v.neighbours) {
                    if(!n.explored) {
                        n.explored = true
                        n.parent  = v
                        queue.addFirst(n)
                    }
                }
            }
            throw RuntimeException("end position not found")
        }

        private fun path(position: Position): List<Position> {
            var result = listOf<Position>()
            var current: Position? = position
            while(current != null) {
                current = current.parent
                current?.let {
                    result = result + current
                }
            }
            return result
        }

        override fun toString(): String {
            val path = path(end)
            return positions.map { cols ->
                cols.map {  position ->
                    when(position) {
                        start -> 'S'
                        end -> 'E'
                        else -> if(path.contains(position)) { '.' } else { position.height }
                    }
                }.joinToString("")
            }.joinToString("\n")
        }
    }

    fun parseMap(input: String): HeightMap {
        return HeightMap(input)
    }

    fun parseMap(input: URL): HeightMap {
        return parseMap(input.readText())
    }
}
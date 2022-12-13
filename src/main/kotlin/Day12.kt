package net.t53k

import java.lang.IllegalArgumentException
import java.net.URL

object Day12 {

    enum class Direction {
        EAST {
            override fun transformCoordinates(c: Coordinates): Coordinates = Coordinates(c.x + 1, c.y)
        },
        WEST {
            override fun transformCoordinates(c: Coordinates): Coordinates = Coordinates(c.x - 1, c.y)
        },
        NORTH {
            override fun transformCoordinates(c: Coordinates): Coordinates = Coordinates(c.x, c.y - 1)
        },
        SOUTH {
            override fun transformCoordinates(c: Coordinates): Coordinates = Coordinates(c.x, c.y + 1)
        };

        abstract fun transformCoordinates(coordinates: Coordinates): Coordinates

    }

    data class Coordinates(val x: Int, val y: Int)

    data class Position(val coordinates: Coordinates, val height: Char, val end: Boolean = false)

    data class HeightMap(val start: Position, val end: Position, val positions: List<List<Position>>) {
        fun findNeighbours(p: Position): Map<Direction, Position> {
            return Direction.values()
                .map { d -> d to d.transformCoordinates(p.coordinates) }
                .filter { (d, c) -> c.x >= 0 && c.y >= 0 && c.y < positions.count() }
                .mapNotNull { (d, c) ->
                    val row = positions[c.y]
                    if (row.count() > c.x) d to row[c.x] else null
                }
                .associate { it }
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
                                val e = Position(Coordinates(x, y), 'z', end = true)
                                end = e
                                e
                            }

                            else -> Position(Coordinates(x, y), c)
                        }
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

    class PathStep(val position: Position, var choices: Set<Position> = setOf()) {
        override fun toString(): String {
            return "PathStep(position=$position)"
        }
    }

    class PathFinder(val map: HeightMap) {

        fun nextCandidates(path: List<Position>): List<Position> {
            val current = path.last()
            val neighbours = map.findNeighbours(current)
            return neighbours.values
                .filter { n -> (current.height + 1) >= n.height }
                .filter { n -> !path.contains(n) }
        }

        fun findPath(): Int {
            var path = listOf<PathStep>(PathStep(map.start))
            var currentStep = PathStep(map.start)
            var minResultPathLength = -1
            while (true) {
                val nextCandidates = nextCandidates(path.map { it.position })
                    .filter { it != map.start }
                    .filter { it != currentStep.position }
                    .filter { !currentStep.choices.contains(it) }
                if (nextCandidates.isNotEmpty()) {
                    val next = PathStep(nextCandidates.first())
                    currentStep.choices = currentStep.choices + next.position
                    currentStep = next
                    path = path + next
                } else {
                    if(currentStep.position == map.start) break
                    path = path.dropLast(1)
                    currentStep = path.last()
                }
                if(currentStep.position == map.end) {
                    val pathLength = path.count() - 1
                    if(minResultPathLength == -1 || minResultPathLength > pathLength) {
                        minResultPathLength = pathLength
                    }
                }
                //val str = pathString(map, path.map { it.position.coordinates })
                //println("               ")
                //println(str)
            }
            return minResultPathLength
        }

        private fun pathString(map: HeightMap, path: List<Coordinates>): String {
            val heigth = map.positions.count()
            val width = map.positions.first().count()
            return (0 until heigth).map { y ->
                (0 until width)
                    .map { x -> Coordinates(x, y) }
                    .map { c -> if (path.contains(c)) '#' else '.' }
                    .joinToString("")
            }.joinToString("\n")

        }
    }

    fun stepsForShortestPath(input: String): Int {
        val map = HeightMap.create(input)
        val pf = PathFinder(map)
        return pf.findPath()
    }

    fun stepsForShortestPath(input: URL): Int {
        return stepsForShortestPath(input.readText())
    }
}
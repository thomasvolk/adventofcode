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

    }

    data class Coordinates(val x: Int, val y: Int)

    data class Position(val coordinates: Coordinates, val height: Char, val end: Boolean = false)

    data class HeightMap(val start: Position, val end: Position, val positions: List<List<Position>>) {
        fun findNeighbours(p: Position): Map<Direction, Position> {
            return Direction.values()
                .map { d -> d to d.transformCoordinates(p.coordinates) }
                .filter { (_, c) -> c.x >= 0 && c.y >= 0 && c.y < positions.count() }
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

        private fun nextCandidates(path: List<Position>): Map<Direction, Position> {
            val current = path.last()
            val neighbours = map.findNeighbours(current)
            return neighbours
                .filter { (d, n) -> (current.height + 1) >= n.height }
                .filterNot { (d, n) -> path.contains(n) }
        }

        fun findPath(): Int {
            var path = listOf(PathStep(map.start))
            var currentStep = PathStep(map.start)
            var minResultPathLength = -1
            while (true) {
                val nextCandidates = nextCandidates(path.map { it.position })
                    .filter { it.value != map.start }
                    .filter { it.value != currentStep.position }
                    .filter { !currentStep.choices.contains(it.value) }
                if (nextCandidates.isNotEmpty()) {
                    val next = PathStep(pickCandidate(currentStep, nextCandidates))
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
                    println("found path: $pathLength")
                    println(pathString(map, path))
                    if(minResultPathLength == -1 || minResultPathLength > pathLength) {
                        minResultPathLength = pathLength
                    }
                }
            }
            return minResultPathLength
        }

        private fun pickCandidate(currentStep: PathStep, nextCandidates: Map<Direction, Position>): Position {
            val currentX = currentStep.position.coordinates.x
            val currentY = currentStep.position.coordinates.y
            nextCandidates[Direction.EAST]?.let { p ->
                if(currentX < map.end.coordinates.x) return p
            }
            nextCandidates[Direction.WEST]?.let { p ->
                if(currentX > map.end.coordinates.x) return p
            }
            nextCandidates[Direction.NORTH]?.let { p ->
                if(currentY > map.end.coordinates.y) return p
            }
            nextCandidates[Direction.SOUTH]?.let { p ->
                if(currentY < map.end.coordinates.y) return p
            }
            return nextCandidates.values.first()
        }

        private fun pathString(map: HeightMap, path: List<PathStep>): String {
            val cPath = path.map { it.position.coordinates }
            val heigth = map.positions.count()
            val width = map.positions.first().count()
            return (0 until heigth).joinToString("\n") { y ->
                (0 until width)
                    .map { x -> Coordinates(x, y) }
                    .map { c ->
                        val idx = cPath.indexOf(c)
                        if (idx >= 0) '#' else ' '
                    }
                    .joinToString("")
            }

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
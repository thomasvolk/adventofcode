package net.t53k

import java.io.File
import java.net.URL

object Day08 {
    enum class Direction {
        EAST, WEST, NORTH, SOUTH
    }
    class Tree(val height:Int, private val neighbours: MutableMap<Direction, Tree> = mutableMapOf()) {

        fun neighbour(direction: Direction): Tree? = neighbours[direction]

        fun neighbour(direction: Direction, tree: Tree?) {
            tree?.let { t ->
                neighbours[direction] = t
            }
        }

        private fun coordinates(): Pair<Int, Int> {
            return Pair(
                allTreesInDirection(this, Direction.NORTH).count(),
                allTreesInDirection(this, Direction.EAST).count()
            )
        }

        fun viewingDistanceScore(): Int {
            return viewingDistanceScore(this, Direction.EAST, this.height) *
                    viewingDistanceScore(this, Direction.WEST, this.height) *
                    viewingDistanceScore(this, Direction.NORTH, this.height) *
                    viewingDistanceScore(this, Direction.SOUTH, this.height)
        }

        fun isInvisible(): Boolean {
            return isInvisible(Direction.EAST)
                    && isInvisible(Direction.NORTH)
                    && isInvisible(Direction.WEST)
                    && isInvisible(Direction.SOUTH)
        }

        private fun isInvisible(direction: Direction): Boolean {
            return allTreesInDirection(this, direction).count { other -> other.height >= this.height } > 0
        }

        override fun toString(): String {
            return "Tree(height=$height, coordinates=${coordinates()})"
        }

        companion object {
            fun viewingDistanceScore(tree: Tree, direction: Direction, height: Int, result:Int = 0): Int {
                tree.neighbour(direction)?.let {
                    if(it.height >= height) {
                        return result + 1
                    }
                    return viewingDistanceScore(it, direction, height, result + 1)
                }
                return result
            }
            fun allTreesInDirection(tree: Tree, direction: Direction, result: List<Tree> = listOf()): List<Tree> {
                tree.neighbour(direction)?.let {
                    return allTreesInDirection(it, direction, result + it)
                }
                return result
            }
        }
    }
    class Forest(rows: List<String>) {
        private val trees: List<List<Tree>>
        private val colCount: Int
        init {
            trees = rows.map { it.toList().map(Character::getNumericValue) .map{ h -> Tree(h) }}
            colCount = trees.first().count()
            trees.forEach { r ->
                if(r.count() != colCount) {
                    throw IllegalStateException("inconsistent column count: $colCount != ${r.count()}")
                }
            }
            trees.forEachIndexed { row, cols ->
                cols.forEachIndexed { col, tree ->
                    tree.neighbour(Direction.EAST, findTree(row, col - 1))
                    tree.neighbour(Direction.WEST, findTree(row, col + 1))
                    tree.neighbour(Direction.NORTH, findTree(row - 1, col))
                    tree.neighbour(Direction.SOUTH, findTree(row + 1, col))
                }
            }
        }
        fun colCount() = colCount
        fun rowCount() = trees.count()

        fun count() = rowCount() * colCount()

        fun allTrees(): List<Tree> = trees.flatten()

        fun findTree(row: Int, col: Int): Tree? {
            if(row >= 0 && row < rowCount() && col >= 0 && col < colCount()) {
                return trees[row][col]
            }
            return null
        }
    }
    fun parse(rows: List<String>): Forest {
        return Forest(rows)
    }

    fun parse(url: URL): Forest {
        File(url.toURI()).useLines { lines ->
            return parse(lines.toList())
        }
    }
}
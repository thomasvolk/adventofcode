package net.t53k

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

        companion object {
            fun allNeighboursOf(tree: Tree, direction: Direction, result: List<Tree> = listOf<Tree>()): List<Tree> {
                tree.neighbour(direction)?.let {
                    return allNeighboursOf(it, direction, result + it)
                }
                return result
            }
        }
    }
    class Forest {
        private lateinit var trees: List<List<Tree>>
        constructor(rows: List<String>) {
            trees = rows.map { it.toList().map(Character::getNumericValue) .map{ h -> Tree(h) }}
            val colCount = colCount()
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
        fun colCount() = trees.first().count()
        fun rowCount() = trees.count()

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
}
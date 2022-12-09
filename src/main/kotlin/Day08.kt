package net.t53k

object Day08 {
    class Tree(val height:Int) {
        var west: Tree? = null
        var north: Tree? = null
        var east: Tree? = null
        var south: Tree? = null
    }
    class Forest {
        private lateinit var trees: List<List<Tree>>
        constructor(rows: List<String>) {
            trees = rows.map { it.toList().map(Character::getNumericValue) .map{ h -> Tree(h) }}
            trees.forEachIndexed { row, cols ->
                cols.forEachIndexed { col, tree ->
                    tree.east = findTree(row, col - 1)
                    tree.west = findTree(row, col + 1)
                    tree.north = findTree(row - 1, col)
                    tree.south = findTree(row + 1, col)
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
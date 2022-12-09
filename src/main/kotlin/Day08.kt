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
        }
        fun colCount() = trees.first().count()
        fun rowCount() = trees.count()
    }
    fun parse(rows: List<String>): Forest {
        return Forest(rows)
    }
}
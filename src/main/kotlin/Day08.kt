package net.t53k

object Day08 {
    data class Forest(val trees: List<List<Int>>) {
        val colCount = trees.first().count()
        val rowCount = trees.count()
    }
    fun parse(rows: List<String>): Forest {
        return Forest(rows.map { it.toList().map(Character::getNumericValue)  })
    }
}
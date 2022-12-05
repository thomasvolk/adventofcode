package net.t53k

import java.io.File
import java.net.URL

object Day02 {
    enum class Choice(private val score: Int) {
        ROCK(1), PAPER(2), SCISSORS(3);



        companion object {
            const val victory = 6
            const val draw = 3
            const val loss = 0

            private val scoreMapping = mapOf(
                Pair(ROCK, PAPER) to victory,
                Pair(ROCK, SCISSORS) to loss,
                Pair(ROCK, ROCK) to draw,
                Pair(PAPER, ROCK) to loss,
                Pair(PAPER, SCISSORS) to victory,
                Pair(PAPER, PAPER) to draw,
                Pair(SCISSORS, ROCK) to victory,
                Pair(SCISSORS, PAPER) to loss,
                Pair(SCISSORS, SCISSORS) to draw
            )

            fun findMappingForScore(score: Int): Set<Pair<Choice, Choice>> {
                return scoreMapping.filter { it.value == score } .keys
            }

            fun score(opponentMePair: Pair<Choice, Choice>): Int {
                val (_, me) = opponentMePair
                return scoreMapping[opponentMePair]!! + me.score
            }
        }
    }
    private val elfInputMapping = mapOf(
        "A" to Choice.ROCK,
        "B" to Choice.PAPER,
        "C" to Choice.SCISSORS
    )

    private val partOneMapping = mapOf(
        "X" to Choice.ROCK,
        "Y" to Choice.PAPER,
        "Z" to Choice.SCISSORS
    )
    private val partTwoMapping = mapOf(
        "X" to Choice.loss,
        "Y" to Choice.draw,
        "Z" to Choice.victory
    )

    fun totalScopeAdvancedMapping(url: URL): Int {
        File(url.toURI()).useLines {lines ->
            return lines.toList()
                .map { interpretPartTwo(it.trim()) }
                .sumOf { pair -> Choice.score(pair) }
        }
    }

    fun totalScopeSimpleMapping(url: URL): Int {
        File(url.toURI()).useLines {lines ->
            return lines.toList()
                .map { interpretPartOne(it.trim()) }
                .sumOf { pair -> Choice.score(pair) }
        }
    }

    private fun interpretPartTwo(line: String): Pair<Choice, Choice> {
        val choices = line.split(" ")
        val elfChoice = elfInputMapping[choices[0]]!!
        val result = partTwoMapping[choices[1]]!!
        val (_, myChoice) = Choice.findMappingForScore(result).first { it.first == elfChoice }
        return Pair(elfChoice, myChoice)
    }
    private fun interpretPartOne(line: String): Pair<Choice, Choice> {
        val choices = line.split(" ")
        val elfChoice = elfInputMapping[choices[0]]!!
        val myChoice = partOneMapping[choices[1]]!!
        return Pair(elfChoice, myChoice)
    }


}
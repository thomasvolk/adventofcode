package net.t53k

import java.io.File
import java.net.URL

enum class Guess(private val score: Int) {
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

        fun findMappingForScore(score: Int): Set<Pair<Guess, Guess>> {
            return scoreMapping.filter { it.value == score } .keys
        }

        fun round(opponentMePair: Pair<Guess, Guess>): Int {
            val (_, me) = opponentMePair
            return scoreMapping[opponentMePair]!! + me.score
        }
    }
}

object Day02 {
    private val mapping = mapOf(
        "A" to Guess.ROCK,
        "B" to Guess.PAPER,
        "C" to Guess.SCISSORS
    )

    private val partOneMapping = mapOf(
        "X" to Guess.ROCK,
        "Y" to Guess.PAPER,
        "Z" to Guess.SCISSORS
    )
    private val partTwoMapping = mapOf(
        "X" to Guess.loss,
        "Y" to Guess.draw,
        "Z" to Guess.victory
    )

    fun totalScopeAdvancedMapping(url: URL): Int {
        File(url.toURI()).useLines {lines ->
            return lines.toList()
                .map { interpretPartTwo(it.trim()) }
                .sumOf { pair -> Guess.round(pair) }
        }
    }

    fun totalScopeSimpleMapping(url: URL): Int {
        File(url.toURI()).useLines {lines ->
            return lines.toList()
                .map { interpretPartOne(it.trim()) }
                .sumOf { pair -> Guess.round(pair) }
        }
    }

    private fun interpretPartTwo(line: String): Pair<Guess, Guess> {
        val guesses = line.split(" ")
        val elfGuess = mapping[guesses[0]]!!
        val result = partTwoMapping[guesses[1]]!!
        val (_, myGuess) = Guess.findMappingForScore(result).first { it.first == elfGuess }
        return Pair(elfGuess, myGuess)
    }
    private fun interpretPartOne(line: String): Pair<Guess, Guess> {
        val guesses = line.split(" ")
        val elfGuess = mapping[guesses[0]]!!
        val myGuess = partOneMapping[guesses[1]]!!
        return Pair(elfGuess, myGuess)
    }


}
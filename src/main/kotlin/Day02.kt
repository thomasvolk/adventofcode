package net.t53k

import java.io.File
import java.net.URL

enum class Guess(private val score: Int) {
    ROCK(1), PAPER(2), SCISSORS(3);

    companion object {
        private const val victory = 6;
        private const val draw = 3
        fun round(opponentMePair: Pair<Guess, Guess>): Int {
            val (opponent, me) = opponentMePair
            return when (opponentMePair) {
                Pair(ROCK, PAPER) -> me.score + victory
                Pair(PAPER, ROCK) -> me.score
                Pair(SCISSORS, ROCK) -> me.score + victory
                Pair(ROCK, SCISSORS) -> me.score
                Pair(PAPER, SCISSORS) -> me.score + victory
                Pair(SCISSORS, PAPER) -> me.score
                else -> me.score + draw
            }
        }
    }
}

object Day02 {
    val mapping = mapOf<String, Guess>(
        "A" to Guess.ROCK,
        "B" to Guess.PAPER,
        "C" to Guess.SCISSORS,
        "X" to Guess.ROCK,
        "Y" to Guess.PAPER,
        "Z" to Guess.SCISSORS,
    )
    fun totalScope(url: URL): Int {
        val text = File(url.toURI()).useLines {lines ->
            return lines.toList().map { asPair(it.trim()) }.map { pair -> Guess.round(pair) }.sum()
        }
    }

    private fun asPair(line: String): Pair<Guess, Guess> {
        val guesses = line.split(" ").map { mapping[it] }
        return Pair(guesses[0]!!, guesses[1]!!)
    }


}
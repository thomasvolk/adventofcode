package net.t53k

import java.lang.IllegalArgumentException
import java.net.URL

object PuzzleInput {
    fun loadFile(name: String): URL {
        Day01::class.java.getResource(name)?.let { file ->
            return file
        }
        throw IllegalArgumentException("Input not found: $name")
    }
}
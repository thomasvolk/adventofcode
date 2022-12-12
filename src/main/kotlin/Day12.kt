package net.t53k

import java.net.URL

object Day12 {
    fun stepsForShortestPath(input: String): Long {
        TODO("implement")
    }
    fun stepsForShortestPath(input: URL): Long {
        return stepsForShortestPath(input.readText())
    }
}
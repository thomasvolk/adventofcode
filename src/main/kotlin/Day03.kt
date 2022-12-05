package net.t53k

import java.io.File
import java.net.URL
import java.util.stream.IntStream
import kotlin.streams.toList

object Day03 {
    data class Compartment(val items: List<Char>)
    data class Rucksack(val compartments: List<Compartment>) {

        fun itemsInCompartment(): Map<Char, Int> {
           return compartments
               .map { it.items }
               .flatten()
               .associateWith { itemInCompartment(it) }
        }

        private fun itemInCompartment(item: Char): Int {
            return compartments.count { it.items.contains(item) }
        }

        companion object {
            fun parse(items: String, compartmentCount: Int = 2): Rucksack {
                val input = items.trim()
                val step = input.count() / compartmentCount
                val compartments = (0 until compartmentCount)
                    .map { it * step }
                    .map { input.substring(it until (it + step)) }
                    .map { Compartment(it.toList()) }
                return Rucksack(compartments)
            }
        }
    }

    private val charMapping =
        (('a'..'z').toList() + ('A'..'Z').toList())
            .mapIndexed{ i, c -> c to i + 1 }
            .toMap()

    fun mapItem(item: Char): Int = charMapping.getOrDefault(item, 0)

    fun rucksacksFromFile(url: URL): List<Rucksack> {
        File(url.toURI()).useLines { lines ->
            return lines.toList()
                .map { Rucksack.parse(it) }
        }
    }

}
package net.t53k

import java.io.File
import java.net.URL

object Day03 {
    data class Compartment(val items: List<Char>)
    data class Rucksack(val groupId: Int, val compartments: List<Compartment>) {

        fun itemsInCompartment(): Map<Char, Int> {
           return compartments
               .map { it.items }
               .flatten()
               .associateWith { itemInCompartment(it) }
        }
         fun hasItem(item: Char): Boolean {
             return itemInCompartment(item) > 0
         }

        private fun itemInCompartment(item: Char): Int {
            return compartments.count { it.items.contains(item) }
        }

        companion object {
            fun create(items: String, groupId: Int, compartmentCount: Int = 2): Rucksack {
                val input = items.trim()
                val step = input.count() / compartmentCount
                val compartments = (0 until compartmentCount)
                    .map { it * step }
                    .map { input.substring(it until (it + step)) }
                    .map { Compartment(it.toList()) }
                return Rucksack(groupId, compartments)
            }
        }
    }

    private val itemTypes =
        (('a'..'z').toList() + ('A'..'Z').toList())

    private val itemScoreMapping = itemTypes
            .mapIndexed{ i, c -> c to i + 1 }
            .toMap()

    fun findGroupItem(rucksacks: List<Rucksack>): Char {
        val itemInRucksacks =  itemTypes.map { item ->
            item to rucksacks.count { r -> r.hasItem(item) }
        }
        return itemInRucksacks
            .filter { it.second == rucksacks.count() }
            .map { it.first }
            .first()
    }

    fun mapItem(item: Char): Int = itemScoreMapping.getOrDefault(item, 0)

    fun rucksacksFromFile(url: URL, groupSize: Int = 3): List<Rucksack> {
        File(url.toURI()).useLines { lines ->
            return lines.toList()
                .mapIndexed { n, line -> Rucksack.create(line, n - n % groupSize) }
        }
    }

}
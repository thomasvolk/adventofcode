package net.t53k

import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals

class Day03Test {
    private val inputFile: URL = Day01::class.java.getResource("/Day03-input.txt")!!

    @Test
    fun testRucksack() {
        val items = "vJrwpWtwJgWrhcsFMMfFFhFp"
        val rucksack2 = Day03.Rucksack.parse(items)
        assertEquals(2, rucksack2.compartments.count())
        assertEquals(listOf('v', 'J', 'r', 'w', 'p', 'W', 't', 'w', 'J', 'g', 'W', 'r'), rucksack2.compartments[0].items)
        assertEquals(listOf('h', 'c', 's', 'F', 'M', 'M', 'f', 'F', 'F', 'h', 'F', 'p'), rucksack2.compartments[1].items)
        val rucksack3 = Day03.Rucksack.parse(items, 3)
        assertEquals(3, rucksack3.compartments.count())
        assertEquals(listOf('v', 'J', 'r', 'w', 'p', 'W', 't', 'w'), rucksack3.compartments[0].items)
        assertEquals(listOf('J', 'g', 'W', 'r', 'h', 'c', 's', 'F'), rucksack3.compartments[1].items)
        assertEquals(listOf('M', 'M', 'f', 'F', 'F', 'h', 'F', 'p'), rucksack3.compartments[2].items)
    }
    @Test
    fun testItems() {
        val items = "vJrwpWtwJgWrhcsFMMfFFhFp"
        val rucksack2 = Day03.Rucksack.parse(items)
        val itemsInCompartments = rucksack2.itemsInCompartment()
        val itemInTwoCompartments = itemsInCompartments.filter { it.value > 1 }.keys
        assertEquals(1, itemInTwoCompartments.count())
        assertEquals('p', itemInTwoCompartments.first())
    }
    @Test
    fun testMapItem() {
        assertEquals(1, Day03.mapItem('a'))
        assertEquals(26, Day03.mapItem('z'))
        assertEquals(27, Day03.mapItem('A'))
        assertEquals(38, Day03.mapItem('L'))
    }
    @Test
    fun testDay03Part1() {
        val rucksacks = Day03.rucksacksFromFile(inputFile)
        val itemSummary = rucksacks
            .map { r -> r.itemsInCompartment().filter { it.value > 1 }.keys }
            .flatten()
            .sumOf { Day03.mapItem(it) }
        assertEquals(8349, itemSummary)
    }
}
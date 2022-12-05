package net.t53k

import java.io.File
import java.net.URL

object Day01 {
    data class Elf(val food: List<Int>) {
        fun calories(): Int = food.sum()
    }

    fun elfsFromURL(url: URL): List<Elf> {
        val text = File(url.toURI()).readText().trim()
        return text.split("\n\n").map { Elf(it.split("\n").map { it.toInt() }) }
    }


}
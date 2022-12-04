package net.t53k

import java.io.File
import java.net.URL

data class Elf(val food: List<Int>) {
    fun calories(): Int = food.sum()
}

object Day01 {
    fun elfsFromURL(url: URL): List<Elf> {
        val text = File(url.toURI()).readText().trim()
        return text.split("\n\n").map { Elf(it.split("\n").map { it.toInt() }) }
    }


}
package net.t53k

import java.io.File
import java.net.URL

object Day06 {

    private fun findFirstMarker(input: List<Char>): Int {
        for(p in 1..input.count()) {
            val dataReceived = input.slice(0 until p)
            if(dataReceived.count() > 3) {
                val possibleMarker = dataReceived.slice(p-4 until p)
                if(allDifferent(possibleMarker)) return p
            }
        }
        return -1
    }

    private fun allDifferent(slice: List<Char>): Boolean {
        return slice.groupBy { it }.count() == slice.count()
    }

    fun firstMarkerPosition(input: String): Int {
        return findFirstMarker(input.toList())
    }

    fun firstMarkerPosition(url: URL): Int {
        val text = File(url.toURI()).readText().trim()
        return findFirstMarker(text.toList())
    }
}
package net.t53k

import java.io.File
import java.net.URL

object Day06 {

    private fun findFirstMarker(input: List<Char>, markerSize: Int): Int {
        for(p in 1..input.count()) {
            val dataReceived = input.slice(0 until p)
            if(dataReceived.count() > (markerSize-1)) {
                val possibleMarker = dataReceived.slice(p-markerSize until p)
                if(allDifferent(possibleMarker)) return p
            }
        }
        return -1
    }

    private fun allDifferent(slice: List<Char>): Boolean {
        return slice.groupBy { it }.count() == slice.count()
    }

    fun firstPacketMarkerPosition(input: String): Int {
        return findFirstMarker(input.toList(), markerSize = 4)
    }
    fun firstPacketMarkerPosition(url: URL): Int {
        val text = File(url.toURI()).readText().trim()
        return firstPacketMarkerPosition(text)
    }
    fun firstMessageMarkerPosition(input: String): Int {
        return findFirstMarker(input.toList(), markerSize = 14)
    }
    fun firstMessageMarkerPosition(url: URL): Int {
        val text = File(url.toURI()).readText().trim()
        return firstMessageMarkerPosition(text)
    }
}
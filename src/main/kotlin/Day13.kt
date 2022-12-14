package net.t53k

import java.net.URL
import javax.script.ScriptEngineManager

object Day13 {
    interface Packet {
    }
    data class ListPacket(val values: List<Packet>): Packet
    data class NumberPacket(val value: Int): Packet
    data class PacketPair(val index: Int, val first: Packet, val second: Packet) {
        companion object {
            val jsEngine = ScriptEngineManager().getEngineByName("nashorn")
            fun parse(pair: String): PacketPair {
                val (first, last) = pair.split("\n").map { jsEngine.eval(it) }
                println("first: $first")
                println("last: $last")
                TODO("Not yet implemented")
            }
        }
    }

    fun parse(input: URL): List<PacketPair> {
        return parse(input.readText())
    }

    fun parse(input: String): List<PacketPair> {
        return input.split("\n\n").map {pair ->
            PacketPair.parse(pair)
        }
    }
}
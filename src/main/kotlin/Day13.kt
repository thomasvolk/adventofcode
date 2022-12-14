package net.t53k

import org.openjdk.nashorn.api.scripting.ScriptObjectMirror
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
                val (first, second) = pair.split("\n").map { jsEngine.eval(it) as ScriptObjectMirror }
                println("first: isArray=${first.isArray} ${first.entries}")
                println("second: isArray=${second.isArray} ${second.entries}")
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
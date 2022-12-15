package net.t53k

import org.openjdk.nashorn.api.scripting.ScriptObjectMirror
import java.net.URL
import javax.script.ScriptEngineManager

object Day13 {
    interface Packet {
        companion object {
            fun parse(item: ScriptObjectMirror): Packet {
                return if(item.isArray) {
                    ListPacket(item.values.map {
                        when(it) {
                            is Int -> NumberPacket(it)
                            else -> parse(it as ScriptObjectMirror)
                        }
                    })
                }
                else {
                    NumberPacket(0)
                }
            }
        }
    }
    data class ListPacket(val values: List<Packet>): Packet
    data class NumberPacket(val value: Int): Packet
    data class PacketPair(val index: Int, val first: Packet, val second: Packet) {
        companion object {
            val jsEngine = ScriptEngineManager().getEngineByName("nashorn")
            fun parse(index: Int, pair: String): PacketPair {
                val (first, second) = pair.split("\n").map { jsEngine.eval(it) as ScriptObjectMirror }
                return PacketPair(index, Packet.parse(first), Packet.parse(second))
            }
        }
    }

    fun parse(input: URL): List<PacketPair> {
        return parse(input.readText())
    }

    fun parse(input: String): List<PacketPair> {
        return input.split("\n\n").mapIndexed { i, pair ->
            PacketPair.parse(i, pair)
        }
    }
}
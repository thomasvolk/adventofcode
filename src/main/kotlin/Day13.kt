package net.t53k

import org.openjdk.nashorn.api.scripting.ScriptObjectMirror
import java.lang.IllegalArgumentException
import java.net.URL
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

object Day13 {
    private val jsEngine: ScriptEngine = ScriptEngineManager().getEngineByName("nashorn")
    interface Packet: Comparable<Packet> {

        fun toListPacket(): ListPacket

        companion object {
            fun create(item: ScriptObjectMirror): Packet {
                return if(item.isArray) {
                    ListPacket(item.values.map {
                        when(it) {
                            is Int -> NumberPacket(it)
                            else -> create(it as ScriptObjectMirror)
                        }
                    })
                }
                else {
                    NumberPacket(0)
                }
            }

            fun parse(item: String): Packet {
                return create(jsEngine.eval(item) as ScriptObjectMirror)
            }
        }
    }
    data class ListPacket(val values: List<Packet>): Packet {
        override fun toListPacket(): ListPacket = this

        override fun compareTo(other: Packet): Int  {
            when(other) {
                is NumberPacket -> return this.compareTo(other.toListPacket())
                is ListPacket -> {
                    for((i, leftItem) in values.withIndex()) {
                        if(i >= other.values.count()) {
                            // Right side ran out of items
                            return 1
                        }
                        val rightItem = other.values[i]
                        val compareToRight = leftItem.compareTo(rightItem)
                        if(compareToRight < 0) {
                            // Left side is smaller
                            return -1
                        }
                        if(compareToRight > 0) {
                            // Right side is smaller
                            return 1
                        }
                    }
                    if(this.values.count() < other.values.count()) {
                        // Left side ran out of items
                        return -1
                    }
                    return 0
                }
                else -> throw IllegalArgumentException("can not handle package type: ${other.javaClass}")
            }
        }
    }

    data class NumberPacket(val value: Int): Packet {
        override fun toListPacket(): ListPacket = ListPacket(listOf(this))

        override fun compareTo(other: Packet): Int {
            return if(other is NumberPacket) {
                this.value.compareTo(other.value)
            }
            else {
                this.toListPacket().compareTo(other)
            }
        }
    }

    data class PacketPair(val index: Int, val left: Packet, val right: Packet) {
        fun correctOrder(): Boolean {
            return left <= right
        }

        companion object {
            fun parse(index: Int, pair: String): PacketPair {
                val (first, second) = pair.split("\n").map { jsEngine.eval(it) as ScriptObjectMirror }
                return PacketPair(index, Packet.create(first), Packet.create(second))
            }
        }
    }

    fun parse(input: URL): List<PacketPair> {
        return parse(input.readText())
    }

    fun parse(input: String): List<PacketPair> {
        return input.trim().split("\n\n").mapIndexed { i, pair ->
            PacketPair.parse(i+1, pair)
        }
    }
}
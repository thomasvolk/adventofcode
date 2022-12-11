package net.t53k

import java.io.File
import java.lang.IllegalArgumentException
import java.net.URL

object Day10 {
    class DataBus(private var devices: List<Device> = listOf()) {
        fun registerDevice(device: Device) {
            devices = devices + device
        }

        fun call(cycle:Int, x: Int) {
            devices.forEach { it.call(cycle, x) }
        }
    }

    fun interface Device {
        fun call(cycle:Int, x: Int)
    }

    class CathodeRayTube(
        private val width: Int = 40,
        private val height: Int = 6,
    ): Device {
        private val display = MutableList(6) { _ -> MutableList(40) { _ -> '.'} }
        override fun call(cycle: Int, x: Int) {
            val (cursorX, cursorY) = calculatePosition(cycle - 1)
            val (spriteX, _) = calculatePosition(x)
            val spriteRange = listOf(spriteX - 1, spriteX, spriteX + 1).filter { it >= 0 || it < width }
            val litPixel = spriteRange.contains(cursorX)
            if(litPixel) display[cursorY][cursorX] = '#'
        }

        private fun calculatePosition(x: Int): Pair<Int, Int> {
            val rayPosition = x % (width*height)
            val row = (rayPosition / width)
            val col = rayPosition % width
            return Pair(col, row)
        }

        fun display(): String = display.joinToString("\n") { row -> row.joinToString("") }
    }

    data class Processor(var x: Int = 1, val dataBuss: DataBus = DataBus()) {
        private var cycles: Int = 0
        fun cycle() {
            cycles += 1
            dataBuss.call(cycles, x)
        }
    }
    interface Command {
        fun execute(processor: Processor)
    }

    class Noop: Command {
        override fun execute(processor: Processor) {
            processor.cycle()
        }

        override fun toString(): String {
            return "Noop()"
        }

    }

    class AddX(private val value: Int): Command {
        override fun execute(processor: Processor) {
            processor.cycle()
            processor.cycle()
            processor.x += value
        }

        override fun toString(): String {
            return "AddX($value)"
        }

    }

    private fun command(line: String): Command {
        val parts = line.split(" ")
        return when(parts[0]) {
            "noop" -> Noop()
            "addx" -> AddX(parts[1].toInt())
            else -> throw IllegalArgumentException("unknown command line: $line")
        }
    }

    fun parse(input: URL): List<Command> {
        File(input.toURI()).useLines { lines ->
            return lines.toList()
                .map { command(it.trim()) }
        }
    }
}
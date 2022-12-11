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

    data class Processor(var x: Int = 1, val dataBuss: DataBus = DataBus()) {
        private var cycles: Int = 0
        fun cycle() {
            cycles += 1
            dataBuss.call(cycles, x)
        }
        fun cycles() = cycles
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
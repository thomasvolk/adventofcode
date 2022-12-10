package net.t53k

import java.io.File
import java.lang.IllegalArgumentException
import java.net.URL

object Day10 {
    data class Register(var x: Int = 1)
    interface Command {
        fun execute(register: Register): Boolean
    }

    class Noop: Command {
        override fun execute(register: Register): Boolean {
            return true
        }

        override fun toString(): String {
            return "Noop()"
        }

    }

    class AddX(private val value: Int): Command {
        private var cyclesLeft = 2
        override fun execute(register: Register): Boolean {
            cyclesLeft -= 1
            val complete =  cyclesLeft < 1
            if(complete) {
                register.x += value
            }
            return complete
        }

        override fun toString(): String {
            return "AddX($value)"
        }

    }

    fun parse(input: URL) {
        File(input.toURI()).useLines { lines ->
            val commands = lines.toList()
                .map { command(it.trim()) }
            println(commands)
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
}
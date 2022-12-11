package net.t53k

object Day11 {
    interface Operand {
        fun value(context: Int): Int

        companion object {
            fun parse(input: String): Operand {
                return if(input == "old") Old() else Constant(input.toInt())
            }
        }
    }
    class Old: Operand {
        override fun value(context: Int): Int {
            return context
        }
    }
    class Constant(val value: Int): Operand {
        override fun value(context: Int): Int {
            return value
        }
    }

    enum class Operator {
        PLUS {
            override fun execute(left: Int, right: Int): Int {
                return left + right
            }
        },
        MULTIPLY {
            override fun execute(left: Int, right: Int): Int {
                return left * right
            }
        };
        abstract fun execute(left: Int, right: Int): Int

        companion object {
            fun parse(input: String): Operator {
                return if(input == "+") PLUS else MULTIPLY
            }
        }
    }

    class Operation(val left: Operand, val operator: Operator, val right: Operand) {
        fun execute(context: Int): Int {
            return operator.execute(left.value(context), right.value(context))
        }

        companion object {
            private val partsRe     = "([0-9a-z]+) ([\\*\\+]) ([0-9a-z]+)".toRegex()
            fun parse(input: String): Operation {
                partsRe.find(input)?.let { p ->
                    val left = Operand.parse(p.groupValues[1])
                    val right = Operand.parse(p.groupValues[3])
                    return Operation(left, Operator.parse(p.groupValues[2]), right)
                }
                throw IllegalArgumentException("inconsistent operation: $input")
            }
        }
    }
    class Monkey(
        val id: Int,
        var items: List<Int> = listOf(),
        val operation: Operation,
        val testDivisibleBy: Int,
        val targetMonkeyIfTestTrue: Int,
        val targetMonkeyIfTestFalse: Int

    ) {
        companion object {
            private val monkeyStartRe   = "Monkey ([0-9]+):".toRegex()
            private val startingItemsRe = "\\s\\sStarting items: ([0-9\\, ]+)".toRegex()
            private val operationRe     = "\\s\\sOperation: new = ([0-9a-z \\*\\+]+)".toRegex()
            private val testRe          = "\\s\\sTest: divisible by ([0-9]+)".toRegex()
            private val ifTrueRe        = "\\s\\s\\s\\sIf true: throw to monkey ([0-9]+)".toRegex()
            private val ifFalseRe       = "\\s\\s\\s\\sIf false: throw to monkey ([0-9]+)".toRegex()
            private fun create(text: String): Monkey {
                val lines = text.trim().split("\n")
                var line = 0
                monkeyStartRe.find(lines[line])?.let { start ->
                    line++
                    startingItemsRe.find(lines[line])?.let { items ->
                        line++
                        operationRe.find((lines[line]))?.let { operation ->
                            line++
                            testRe.find(lines[line])?.let {test ->
                                line++
                                ifTrueRe.find(lines[line])?.let { ifTrue ->
                                    line++
                                    ifFalseRe.find(lines[line])?.let { ifFalse ->
                                        return Monkey(
                                            start.groupValues[1].toInt(),
                                            items.groupValues[1]
                                                .split(",")
                                                .map { it.trim() }
                                                .map { it.toInt() },
                                            Operation.parse(operation.groupValues[1]),
                                            test.groupValues[1].toInt(),
                                            ifTrue.groupValues[1].toInt(),
                                            ifFalse.groupValues[1].toInt()
                                       )
                                    }
                                    throwError(line, lines)
                                }
                                throwError(line, lines)
                            }
                            throwError(line, lines)
                        }
                        throwError(line, lines)
                    }
                    throwError(line, lines)
                }
                throw IllegalArgumentException("inconsistent dataset: $text")
            }

            private fun throwError(line: Int, lines: List<String>) {
                throw IllegalArgumentException("error parsing line $line: '${lines[line]}'")
            }

            fun parse(input: String): List<Monkey> {
                return input.split("\n\n").map { Monkey.create(it) }
            }
        }
    }
}
package day10

import java.io.File

internal class Computer(instructions: Collection<Instruction>) {

    val register = Register()
    val cycle get() = time
    private var time = 0

    private val instructionIterator = instructions.iterator()
    private var currentInstruction = instructionIterator.next()
    private var timeToExecute: Int = currentInstruction.cycles
    private var programEnded = false

    fun hasNextInstruction(): Boolean = !programEnded
    fun tick(): Int {
        time++
        if (--timeToExecute == 0) {
            currentInstruction.execute(register)
            if (instructionIterator.hasNext()) {
                currentInstruction = instructionIterator.next()
                timeToExecute = currentInstruction.cycles
            } else {
                programEnded = true
            }
        }
        return time
    }

    data class Register(var x: Int = 1)
    sealed interface Instruction {

        val cycles: Int
        fun execute(register: Register)

        data class Add(val value: Int) : Instruction {

            override val cycles = 2
            override fun execute(register: Register) {
                register.x += value
            }
        }

        object Noop : Instruction {

            override val cycles = 1
            override fun execute(register: Register) {}
        }

    }
}

internal fun parseInstruction(string: String): Computer.Instruction = when {
    string.startsWith("addx") -> Computer.Instruction.Add(string.substringAfter("addx ").toInt())
    string == "noop" -> Computer.Instruction.Noop
    else -> throw IllegalArgumentException("Unknown instruction: $string")
}

internal fun File.readInstructions() = readLines().map { parseInstruction(it) }

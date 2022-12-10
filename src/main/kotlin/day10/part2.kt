package day10

import java.io.File


private class Screen(instructions: Collection<Computer.Instruction>) {
    val computer = Computer(instructions)
    private var drawPosition = 0
    private val sprite = Sprite()

    fun draw() {
        while (computer.hasNextInstruction()) {
            drawPixel()
        }
    }

    private fun drawPixel() {
        print(if (drawPosition in sprite) '#' else '.')
        computer.tick()
        if (++drawPosition == 40) {
            println()
            drawPosition = 0
        }
    }

    inner class Sprite {
        operator fun contains(drawPosition: Int): Boolean =
            computer.register.x - 1 <= drawPosition && drawPosition <= computer.register.x + 1
    }
}

fun main() {
    val instructions = File("input/day10/input.txt").readInstructions()

    val screen = Screen(instructions)

    screen.draw()
}

package day10

import java.io.File

fun main() {
    val instructions = File("input/day10/simple.txt").readInstructions()

    val computer = Computer(instructions)

    val progression = 20..220 step 40
    var signal = 0

    for (border in progression) {
        while (computer.cycle + 1 < border) {
            computer.tick()
        }
        signal += border * computer.register.x
    }


    println(signal)

}

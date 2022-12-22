package day21

import java.io.File

fun main() {
    val monkeys = File("input/day21/input.txt").readLines().map { it.toMonkey() }

    resolve(monkeys)

    val result = monkeys.filterIsInstance<Monkey.ExpressionMonkey>().first { it.name == "root" }
        .resolve()?.value
    println(result)
}

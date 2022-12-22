package day21

import java.io.File
import java.lang.IllegalStateException


fun main() {
    val monkeys = File("input/day21/input.txt").readLines().mapTo(mutableListOf()) { it.toMonkey() }

    resolve(monkeys.filterNot { it.name == "humn" })

    val expressionMonkeys = monkeys.filterIsInstance<Monkey.ExpressionMonkey>()
        .map { it.resolve() ?: it }
        .filterIsInstance<Monkey.ExpressionMonkey>()

    val nameToMonkey = expressionMonkeys.associateBy { it.name }

    val root = checkNotNull(nameToMonkey["root"]) { "Root monkey doesn't exists" }
    var current = root.toEquals()

    while (current.unknown != "humn") {
        val monkey =
            checkNotNull(nameToMonkey[current.unknown]) { "Monkey with name: ${current.unknown} doesn't exists" }

        current = current.with(monkey)
    }

    println(current.known)
}

private fun Equals.with(monkey: Monkey.ExpressionMonkey): Equals {
    val left = monkey.leftValue
    val operation = monkey.operation
    val right = monkey.rightValue

    return when {
        right != null -> Equals(operation.reverseLeft(known, right), monkey.leftMonkey)
        left != null -> Equals(operation.reverseRight(known, left), monkey.rightMonkey)
        else -> throw IllegalStateException()
    }
}

private fun Monkey.Operation.reverseLeft(left: Long, right: Long): Long = when (this) {
    Monkey.Operation.ADD -> left - right
    Monkey.Operation.MULTIPLY -> left / right

    Monkey.Operation.SUBTRACT -> left + right
    Monkey.Operation.DIVIDE -> left * right
}

private fun Monkey.Operation.reverseRight(left: Long, right: Long): Long = when (this) {
    Monkey.Operation.ADD -> left - right
    Monkey.Operation.MULTIPLY -> left / right

    Monkey.Operation.SUBTRACT -> right - left
    Monkey.Operation.DIVIDE -> right / left
}

private fun Monkey.ExpressionMonkey.toEquals(): Equals {
    val (left, right) = listOf(leftValue, rightValue)
    return when {
        left != null -> Equals(left, rightMonkey)
        right != null -> Equals(right, leftMonkey)
        else -> throw IllegalStateException()
    }
}

private data class Equals(val known: Long, val unknown: String)

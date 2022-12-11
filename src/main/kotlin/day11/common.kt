package day11

import java.io.File

internal class Simulation(
    private val monkeys: List<Monkey>,
    private val afterIntrospect: (Item) -> Unit = {}
) {

    fun round() {
        monkeys.forEach { monkey ->
            monkey.giveItems().forEach { item ->
                monkey.inspect(item)
                afterIntrospect(item)
                monkey.throwItem(item, monkeys)
            }
        }
    }

    fun monkeyBusiness(): Long {
        return monkeys.map { it.inspectionCount }
            .sortedDescending()
            .take(2)
            .reduce { a, b -> a * b }
    }

    fun monkeyInspectCount(): String {
        return buildString {
            monkeys.forEachIndexed { index, monkey ->
                appendLine("Monkey $index inspected items ${monkey.inspectionCount} times.")
            }
            appendLine()
        }
    }

    class Monkey(
        items: Collection<Item>,
        val inspectOperation: InspectOperation,
        private val testCondition: Condition,
        private val testTrueMonkey: Int,
        private val testFalseMonkey: Int,
    ) {

        val inspectionCount get() = inspectOperation.count
        val divider get() = testCondition.divider

        private var items = items.toMutableList()

        fun giveItems(): Collection<Item> = items.also { items = mutableListOf() }

        fun inspect(item: Item) {
            item.worryLevel = inspectOperation(item)
        }

        fun throwItem(item: Item, monkeys: List<Monkey>) {
            monkeys[if (testCondition(item)) testTrueMonkey else testFalseMonkey].catchItem(item)
        }

        private fun catchItem(item: Item) {
            items.add(item)
        }
    }

    data class Item(var worryLevel: Long)

    class InspectOperation(private val operation: (Long) -> Long) {
        var count: Long = 0

        operator fun invoke(item: Item): Long = operation(item.worryLevel).also { count++ }
    }

    class Condition(val divider: Long) {

        operator fun invoke(item: Item): Boolean {
            return item.worryLevel divisibleBy divider
        }

    }

}

internal infix fun Long.divisibleBy(value: Long): Boolean = this % value == 0L

internal fun File.readMonkeys(): List<Simulation.Monkey> = readLines().chunked(7).map { MonkeyParser.parseMonkey(it) }

private object MonkeyParser {

    fun parseMonkey(lines: List<String>): Simulation.Monkey {
        return Simulation.Monkey(
            parseItems(lines[1]),
            parseOperation(lines[2]),
            parseCondition(lines[3]),
            parseThrowAction(true, lines[4]),
            parseThrowAction(false, lines[5])
        )
    }

    private fun parseItems(string: String): Collection<Simulation.Item> =
        string.substringAfter("Starting items: ").split(", ").map { Simulation.Item(it.toLong()) }

    private fun parseOperation(string: String): Simulation.InspectOperation {
        require(string.contains("Operation: new = ")) { "Can't parse operation: $string" }
        val formula = string.substringAfter("Operation: new = ")

        return Simulation.InspectOperation(parseFormula(formula))
    }

    private fun parseFormula(formula: String): (Long) -> Long {
        val (left, op, right) = formula.split(' ')
            .also { require(it.size == 3) { "Can't parse formula: $it" } }

        return op.toOperation().bind(left.toExpression(), right.toExpression())
    }

    private fun String.toOperation(): FormulaToken.Operation = FormulaToken.Operation.parse(this)

    private fun String.toExpression(): FormulaToken.Expression = FormulaToken.Expression.parse(this)

    private fun parseCondition(string: String): Simulation.Condition {
        require(string.contains("Test: divisible by ")) { string }
        val divide = string.substringAfter("Test: divisible by ").toLong()
        return Simulation.Condition(divide)
    }

    private fun parseThrowAction(result: Boolean, string: String): Int {
        require(string.contains("If ${result}: throw to monkey "))
        return string.substringAfter("If ${result}: throw to monkey ").toInt()
    }

    sealed interface FormulaToken {

        sealed interface Expression : FormulaToken {
            fun with(context: Long): Long

            companion object {
                fun parse(string: String): Expression {
                    return when {
                        string == "old" -> Parameter
                        string.all { it.isDigit() } -> Literal(string.toLong())
                        else -> throw IllegalArgumentException("Can't parse expression: $string")
                    }
                }
            }
        }

        object Parameter : Expression {
            override fun with(context: Long) = context

        }

        class Literal(private val value: Long) : Expression {
            override fun with(context: Long) = value
        }

        enum class Operation(val op: (Long, Long) -> Long) : FormulaToken {

            PLUS({ a, b -> a + b }),
            MULTIPLY({ a, b -> a * b });

            fun bind(left: Expression, right: Expression): (Long) -> Long = { context ->
                op(left.with(context), right.with(context))
            }

            companion object {
                fun parse(op: String): Operation {
                    return when (op) {
                        "+" -> PLUS
                        "*" -> MULTIPLY
                        else -> throw IllegalArgumentException("Unknown operation: $op")
                    }
                }
            }
        }
    }
}

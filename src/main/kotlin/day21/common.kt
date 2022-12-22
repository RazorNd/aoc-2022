package day21

internal sealed interface Monkey {

    val name: String

    data class ValueMonkey(override val name: String, val value: Long) : Monkey

    data class ExpressionMonkey(
        override val name: String,
        val leftMonkey: String,
        val rightMonkey: String,
        val operation: Operation,
        var leftValue: Long? = null,
        var rightValue: Long? = null
    ) : Monkey {

        val depends = setOf(leftMonkey, rightMonkey)

        fun resolve(monkey: ValueMonkey): ValueMonkey? {
            updateValue(monkey)
            return resolve()
        }

        fun resolve(): ValueMonkey? {
            val (left, right) = listOf(leftValue, rightValue)
            if (left != null && right != null) {
                return ValueMonkey(name, execute(left, right))
            }
            return null
        }

        private fun updateValue(monkey: ValueMonkey) {
            when (monkey.name) {
                leftMonkey -> leftValue = monkey.value
                rightMonkey -> rightValue = monkey.value
            }
        }

        private fun execute(left: Long, right: Long): Long = when (operation) {
            Operation.ADD -> left + right
            Operation.SUBTRACT -> left - right
            Operation.MULTIPLY -> left * right
            Operation.DIVIDE -> left / right
        }
    }

    enum class Operation(val symbol: Char) {
        ADD('+'), SUBTRACT('-'), MULTIPLY('*'), DIVIDE('/');
    }
}

private fun String.toOperations(): Monkey.Operation =
    Monkey.Operation.values().firstOrNull { this == it.symbol.toString() }
        ?: throw IllegalArgumentException("Operation: $this doesn't exists")

internal fun String.toMonkey(): Monkey {
    val (name, monkey) = split(": ", limit = 2)
    if (Monkey.Operation.values().any { monkey.contains(it.symbol) }) {
        val (left, op, right) = monkey.split(' ', limit = 3)
        return Monkey.ExpressionMonkey(name, left, right, op.toOperations())
    }
    return Monkey.ValueMonkey(name, monkey.toLong())
}

internal fun resolve(monkeys: List<Monkey>) {
    val dependsToMonkey = monkeys.filterIsInstance<Monkey.ExpressionMonkey>()
        .flatMap { monkey -> monkey.depends.map { it to monkey } }
        .groupBy({ it.first }, { it.second })

    val queue = ArrayDeque(monkeys.filterIsInstance<Monkey.ValueMonkey>())

    while (queue.isNotEmpty()) {
        val monkey = queue.removeFirst()

        val resolved = (dependsToMonkey[monkey.name] ?: listOf()).mapNotNull { it.resolve(monkey) }

        queue.addAll(resolved)
    }
}

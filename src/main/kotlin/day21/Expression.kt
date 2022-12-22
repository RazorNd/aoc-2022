package day21

private sealed interface Expression {

    @JvmInline
    value class Constant(val value: Long) : Expression

    sealed interface Operation : Expression {
        val parameter1: Expression

        val parameter2: Expression

        operator fun component1() = parameter1
        operator fun component2() = parameter2
    }

    data class AddOperation(override val parameter1: Expression, override val parameter2: Expression) : Operation
    data class MultiplyOperation(override val parameter1: Expression, override val parameter2: Expression) : Operation
    data class SubtractOperation(override val parameter1: Expression, override val parameter2: Expression) : Operation
    data class DivisionOperation(override val parameter1: Expression, override val parameter2: Expression) : Operation

    data class Reference(val name: String) : Expression
}

private data class Equation(val left: Expression, val right: Expression) {

    val reference get() = sequenceOf(left, right).single { it.isReference }

    val noneReference get() = sequenceOf(left, right).single { !it.isReference }

    val operationWithReference
        get() = sequenceOf(left, right).filterIsInstance<Expression.Operation>()
            .singleOrNull { (p1, p2) -> p1 is Expression.Reference || p2 is Expression.Reference }

    infix fun combine(other: Equation): Equation {
        require(reference == other.reference) { "Not equals reference combination" }
        return Equation(noneReference, other.noneReference)
    }

    fun simplify(): Equation = Equation(left.simplify(), right.simplify())

    operator fun plus(other: Equation): Equation = this combine other

    private val Expression.isReference get() = this is Expression.Reference

    private fun Expression.simplify(): Expression {
        return when (this) {
            is Expression.Operation -> this.tryExecute()
            else -> this
        }
    }

    private fun Expression.Operation.tryExecute(): Expression {
        val (param1, param2) = listOf(parameter1, parameter2).map { it.simplify() }
        if (param1 is Expression.Constant && param2 is Expression.Constant) {
            val value = when (this) {
                is Expression.AddOperation -> param1.value + param2.value
                is Expression.SubtractOperation -> param1.value - param2.value
                is Expression.MultiplyOperation -> param1.value * param2.value
                is Expression.DivisionOperation -> param1.value / param2.value
            }
            return Expression.Constant(value)
        }
        return this
    }
}

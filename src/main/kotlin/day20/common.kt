package day20

internal fun List<Long>.mixed(): List<Long> = toOrdered().apply { mix() }.sortedByOrder()

internal fun Collection<Order>.sortedByOrder() = sortedBy { it.position }.map { it.value }

internal fun List<Long>.toOrdered() = mapIndexed { index, value -> Order(index.toLong(), value) }

internal data class Order(var position: Long, val value: Long)

internal fun List<Order>.mix() {
    fun Long.wrapToSize() = (this % lastIndex).let { if (it <= 0) it + lastIndex else it }
    fun Order.newPosition() = (position + value).wrapToSize()

    for (current in this) {
        val newPosition = current.newPosition().takeIf { it != current.position } ?: continue

        val changedRange = minOf(current.position + 1, newPosition)..maxOf(current.position - 1, newPosition)

        val forChange = asSequence().filter { it.position in changedRange }

        when {
            newPosition > current.position -> forChange.forEach { it.position-- }
            newPosition < current.position -> forChange.forEach { it.position++ }
        }

        current.position = newPosition
    }
}

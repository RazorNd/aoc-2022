package day13

internal object IntListComparator : Comparator<Any> {
    override fun compare(left: Any, right: Any): Int = when {
        left is List<*> && right is List<*> -> compareList(left, right)
        left is Int && right is Int -> naturalOrder<Int>().compare(left, right)
        left is List<*> -> compareList(left, listOf(right))
        right is List<*> -> compareList(listOf(left), right)
        else -> throw IllegalArgumentException()
    }

    private fun compareList(left: List<*>, right: List<*>): Int {
        return left.zip(right)
            .asSequence()
            .map { (l, r) -> compare(l!!, r!!) }
            .firstOrNull { it != 0 } ?: naturalOrder<Int>().compare(left.size, right.size)
    }
}

internal fun String.parse(): Any = when {
    startsWith("[") && endsWith("]") -> substring(1, lastIndex).parseList()
    else -> this.toInt()
}

private fun String.parseList(): List<Any> {
    if (isEmpty()) {
        return listOf()
    }

    val result = mutableListOf<Any>()

    var start = 0
    var openBraces = 0
    for ((i, c) in this.withIndex()) {
        when {
            c == '[' -> openBraces++
            c == ']' -> openBraces--
            c == ',' && openBraces == 0 -> {
                result.add(substring(start, i).parse())
                start = i + 1
            }
        }
    }
    if (start != length) {
        result.add(substring(start, length).parse())
    }
    return result
}

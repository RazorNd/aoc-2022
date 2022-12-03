package day03

import java.util.BitSet

internal fun Char.priority(): Int = when (this) {
    in 'a'..'z' -> this - 'a'
    in 'A'..'Z' -> this - 'A' + 26
    else -> throw IllegalArgumentException("Incorrect item: $this")
}

fun indexBitSet(items: String) = BitSet(52).apply { items.forEach { this[it.priority()] = true } }

package day04

private fun parseRange(it: String) = it.split("-").map { it.toInt() }.let { (a, b) -> a..b }
internal fun parsePair(line: String) = line.split(",").map { parseRange(it) }.let { (a, b) -> a to b }

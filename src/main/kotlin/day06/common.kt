package day06

internal fun packetPosition(input: String, size: Int): Int {
    return input.windowed(size).indexOfFirst { it.toSet().size == size } + size
}

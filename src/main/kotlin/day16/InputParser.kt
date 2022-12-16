package day16

import java.io.File

internal fun File.parseTunnels() = readLines()
    .map { it.split("; ", limit = 2) }
    .map { (valve, directions) ->
        valve.parseValve().let { (name, rate) -> Tunnel(rate, name, directions.parseDirections()) }
    }

private fun String.parseDirections() = substringAfter("tunnels lead to ")
    .substringAfter("valves ")
    .substringAfter("valve ")
    .split(", ")

private const val VALVE_NAME_PREFIX = "Valve "
private const val RATE_PREFIX = " has flow rate="
private fun String.parseValve(): Pair<String, Int> {
    val name = substringAfter(VALVE_NAME_PREFIX).substringBefore(RATE_PREFIX)
    val rate = substringAfter(RATE_PREFIX).toInt()
    return name to rate
}

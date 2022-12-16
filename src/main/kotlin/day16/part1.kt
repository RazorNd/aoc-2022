package day16

import java.io.File


fun main() {
    val tunnels = File("input/day16/input.txt").parseTunnels()

    println(findMaxRate(tunnels))
}

private fun findMaxRate(tunnels: List<Tunnel>) = PathFinder(tunnels).findAllPaths(30).maxBy { it.rate }.rate



package day14

import day14.CaveScan.Tile.ROCK
import day14.CaveScan.Tile.SAND
import java.io.File

fun main() {
    val scan = File("input/day14/input.txt").readCaveScan()

    val source = Position(500, 0)
    val simulation = Simulation(scan.bordered(scan.endlessBorder() + 2), source) { it == source && this[it] == SAND }

    println(simulation.simulate())
}

private fun CaveScan.bordered(border: Int): CaveScan = BottomBorderedCaveScan(this, border)

private class BottomBorderedCaveScan(private val delegate: CaveScan, private val border: Int) : CaveScan {
    override val sandCount get() = delegate.sandCount

    override fun addSand(position: Position) = delegate.addSand(position)

    override fun toMap() = delegate.toMap()

    override fun get(x: Int, y: Int) = ROCK.takeIf { y >= border } ?: delegate[x, y]

}

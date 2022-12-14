package day14

internal class Simulation(
    private val scan: CaveScan,
    private val sandSource: Position = Position(500, 0),
    private val endPredicate: CaveScan.(Position) -> Boolean
) {

    private var simulationEnded = false

    fun simulate(): Int {
        while (!simulationEnded) produceSand()
        return scan.sandCount
    }

    private fun produceSand() {
        if (simulationEnded) return
        var sandPosition = sandSource
        var rest = false

        while (!rest) {
            if (sandPosition.isLast()) return
            when (CaveScan.Tile.AIR) {
                scan[sandPosition.below] -> sandPosition = sandPosition.below
                scan[sandPosition.leftBelow] -> sandPosition = sandPosition.leftBelow
                scan[sandPosition.rightBelow] -> sandPosition = sandPosition.rightBelow
                else -> rest = true
            }
        }
        scan.addSand(sandPosition)
    }

    private fun Position.isLast(): Boolean = (scan.endPredicate(this)).also { simulationEnded = it }
    private val Position.below: Position get() = Position(x, y + 1)
    private val Position.leftBelow: Position get() = Position(x - 1, y + 1)
    private val Position.rightBelow: Position get() = Position(x + 1, y + 1)
}

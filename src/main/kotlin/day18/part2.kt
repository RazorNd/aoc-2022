package day18

import day18.QubeStructure.Companion.toStructures
import java.io.File


private val Collection<Coordinate>.leftTopFront
    get() = Coordinate(
        minOf { it.x },
        minOf { it.y },
        minOf { it.z },
    )

private val Collection<Coordinate>.rightBottomBack
    get() = Coordinate(
        maxOf { it.x },
        maxOf { it.y },
        maxOf { it.z },
    )

private fun Collection<Coordinate>.minMaxRange(): CoordinateRange = leftTopFront..rightBottomBack

private class QubeStructure(val coordinates: Set<Coordinate>) {

    val leftTopFront: Coordinate get() = coordinates.leftTopFront

    val rightBottomBack: Coordinate get() = coordinates.rightBottomBack

    val surfaceArea: Int get() = nearest.count { it !in coordinates }

    companion object {
        fun Collection<Coordinate>.toStructures(nearest: (Coordinate) -> Iterable<Coordinate>): List<QubeStructure> {
            val groups = createGroups2(this, nearest)

            return groups.map { QubeStructure(it) }
        }

        private fun createGroups2(
            coordinates: Collection<Coordinate>,
            nearest: (Coordinate) -> Iterable<Coordinate>
        ): List<Set<Coordinate>> {
            val solids = coordinates.toSet()

            val globalQueue = coordinates.toMutableSet()

            return buildList {
                while (globalQueue.isNotEmpty()) {
                    val coordinate = globalQueue.first()
                    globalQueue.remove(coordinate)

                    val group = mutableSetOf(coordinate)

                    val localQueue = ArrayDeque(group)

                    while (localQueue.isNotEmpty()) {
                        val neighbors = nearest(localQueue.removeFirst())
                            .filter { it in solids && it !in group }

                        neighbors.forEach { neighbor ->
                            globalQueue.remove(neighbor)

                            group.add(neighbor)
                            localQueue.addLast(neighbor)
                        }
                    }


                    add(group)
                }
            }

        }

        private fun createGroups(
            coordinates: Collection<Coordinate>,
            nearest: (Coordinate) -> Iterable<Coordinate>
        ): List<Set<Coordinate>> {
            val coordinateToQube = coordinates.map { Qube(it) }.associateBy { it.coordinate }

            coordinateToQube.values.forEach { qube ->
                nearest(qube.coordinate).mapNotNull { coordinateToQube[it] }.forEach { qube.connectWith(it) }
            }

            return coordinateToQube.values.map { it.structure }.distinct()
        }

        private class Qube(val coordinate: Coordinate) {

            private var internalStructure: MutableSet<Coordinate>? = null
            val structure: MutableSet<Coordinate>
                get() {
                    if (internalStructure == null) {
                        internalStructure = mutableSetOf(coordinate)
                    }
                    return internalStructure!!
                }

            fun connectWith(other: Qube) {

                when {
                    internalStructure == null && other.internalStructure == null -> {
                        internalStructure = mutableSetOf(this.coordinate, other.coordinate)
                        other.internalStructure = internalStructure
                    }

                    internalStructure != null && other.internalStructure == null -> {
                        internalStructure?.add(other.coordinate)
                        other.internalStructure = internalStructure
                    }

                    internalStructure == null && other.internalStructure != null -> {
                        other.connectWith(this)
                    }

                    (other.internalStructure?.size ?: 0) > (internalStructure?.size ?: 0) -> {
                        other.connectWith(this)
                    }

                    else -> {
                        internalStructure?.addAll(other.internalStructure ?: setOf())
                        other.internalStructure = internalStructure
                    }

                }
            }

            override fun equals(other: Any?): Boolean =
                this === other || (other is Qube && other.coordinate == coordinate)

            override fun hashCode(): Int = coordinate.hashCode()
        }
    }
}

private class CoordinateRange(
    private val leftTopFront: Coordinate,
    private val rightBottomBack: Coordinate
) : Iterable<Coordinate> {
    operator fun contains(coordinate: Coordinate): Boolean = Dimension.values()
        .all { coordinate[it] in leftTopFront[it]..rightBottomBack[it] }

    override fun iterator(): Iterator<Coordinate> = iterator {
        for (z in leftTopFront.z..rightBottomBack.z) {
            for (y in leftTopFront.y..rightBottomBack.y) {
                for (x in leftTopFront.x..rightBottomBack.x) {
                    yield(Coordinate(x, y, z))
                }
            }
        }
    }
}

private operator fun Coordinate.rangeTo(other: Coordinate): CoordinateRange = CoordinateRange(this, other)
private operator fun Array<Surface>.plus(it: Coordinate) = toList() + it
private val Iterable<Coordinate>.surfaceArea: Int
    get() {
        val set = toSet()

        return sumOf { coordinate -> coordinate.nearest.count { it !in set } }
    }
private val Coordinate.nearest get() = Surface.values().map { this + it }
private val Coordinate.diagonals get() = Surface.diagonals().map { this + it }
private val QubeStructure.nearest get() = coordinates.asSequence().flatMap { it.nearest }

fun main() {
    val coordinates = File("input/day18/input.txt").readCoordinates().toSet()
    val structures = coordinates.toStructures { it.nearest + it.diagonals }

    checkStructures(coordinates, structures)
    check(coordinates.surfaceArea == structures.sumOf { it.surfaceArea })
    val allSurfaceArea = coordinates.surfaceArea

    val airBubbles = structures.flatMap { structure ->
        val structureRange = structure.leftTopFront..structure.rightBottomBack

        val airCoordinates = airCoordinates2(structure.coordinates)
        val airStructures = airCoordinates.toStructures { coordinate -> coordinate.nearest }
        checkStructures(airCoordinates, airStructures)

        airStructures
            .filter { air -> air.nearest.all { it in structureRange } }
    }

    println("airBubbles.size=${airBubbles.size} (expected=42)")

    val airs = airBubbles.flatMap { it.coordinates }.toSet()
    println("airs.size=${airs.size} (expected=1447)")

    val airSurfaceArea = airs.surfaceArea

    val result = allSurfaceArea - airSurfaceArea

    check(result == 2444) { "Result not correct. Expected 2444, actual $result" }

    println("$allSurfaceArea - $airSurfaceArea = $result")
}

private fun checkStructures(coordinates: Collection<Coordinate>, structures: Collection<QubeStructure>) {
    check(coordinates.all { c -> structures.count { c in it.coordinates } == 1 })
}

private enum class Dimension {
    X, Y, Z
}


private fun airCoordinates2(coordinates: Collection<Coordinate>): Collection<Coordinate> {
    val solid = coordinates.toSet()
    return coordinates.minMaxRange().filter { it !in solid }
}

private fun airCoordinates(coordinates: Collection<Coordinate>): Collection<Coordinate> =
    Dimension.values().flatMap { dimension -> byDimension(dimension, coordinates) }.toSet()

private fun byDimension(
    dimension: Dimension,
    coordinates: Collection<Coordinate>
): List<Coordinate> {
    val groupBy = coordinates.groupBy({ it.to2D(dimension) }, { it[dimension] })

    return groupBy.entries
        .sortedWith(compareBy({ it.key.first }, { it.key.second }))
        .flatMap { (coordinates, values) ->

            val set = values.toSet()

            val c = (values.min() + 1 until values.max())
                .filter { it !in set }
                .map { dimension.toCoordinate(coordinates, it) }

//            println("(${coordinates.first}, ${coordinates.second}) = ${values.sorted()}, c=$c")

            c
        }
}

private operator fun Coordinate.get(dimension: Dimension): Int = when (dimension) {
    Dimension.X -> x
    Dimension.Y -> y
    Dimension.Z -> z
}

private fun Coordinate.to2D(dimension: Dimension): Pair<Int, Int> = when (dimension) {
    Dimension.X -> y to z
    Dimension.Y -> x to z
    Dimension.Z -> x to y
}

private fun Dimension.toCoordinate(coordinates: Pair<Int, Int>, i: Int): Coordinate = when (this) {
    Dimension.X -> Coordinate(i, coordinates.first, coordinates.second)
    Dimension.Y -> Coordinate(coordinates.first, i, coordinates.second)
    Dimension.Z -> Coordinate(coordinates.first, coordinates.second, i)
}

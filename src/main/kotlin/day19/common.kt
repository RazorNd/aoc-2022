package day19

import java.io.File
import java.util.*

internal data class Blueprint(val number: Int, val robots: List<Robot>)
internal data class Robot(val type: ResourceType, val costs: Map<ResourceType, Int>)
internal data class Decision(
    val timeLeft: Int = 24,
    private val robots: List<Int> = List(ResourceType.values().size) { if (it == 0) 1 else 0 },
    val resources: List<Int> = List(ResourceType.values().size) { 0 },
) : Comparable<Decision> {
    val geodeCount get() = resources[ResourceType.GEODE.ordinal]

    fun next(blueprint1: Blueprint): Collection<Decision> {
        return blueprint1.robots.filter { it.canBeMade }.mapNotNull { buildRobot(it) }
    }

    fun noMoreRobots(): Decision = copy(timeLeft = 0, resources = addResources(timeLeft).toList())

    private val Robot.canBeMade: Boolean get() = costs.all { (type) -> robotsCount(type) > 0 }

    private fun addResources(time: Int): IntArray {
        val newResources = resources.toIntArray()

        robots.forEachIndexed { type, count -> newResources[type] += time * count }

        return newResources
    }

    private fun buildRobot(robot: Robot): Decision? {
        val time = robot.costs.maxOf { (type, value) -> (value - resourceCount(type)) divUpper robotsCount(type) } + 1

        if (time > timeLeft || time < 1) return null

        val newResources = addResources(time)

        robot.costs.forEach { (type, value) -> newResources[type.ordinal] -= value }

        return copy(timeLeft = timeLeft - time, robots = newRobot(robot.type), resources = newResources.toList())
    }

    private fun newRobot(type: ResourceType): List<Int> {
        return robots.mapIndexed { typeIndex, count -> if (type.ordinal == typeIndex) count + 1 else count }
    }

    private fun resourceCount(type: ResourceType): Int = resources[type.ordinal]

    private fun robotsCount(type: ResourceType) = robots[type.ordinal]
    private infix fun Int.divUpper(division: Int) = (this + division - 1) / division

    override fun compareTo(other: Decision): Int {
        return compareValuesBy(this, other) { it.timeLeft }
    }
}

internal fun Decision.findMaxGeode(blueprint: Blueprint): Int {
    val allDecisions = mutableSetOf(this)

    var best = allDecisions.first()

    while (allDecisions.isNotEmpty()) {
        val decision = allDecisions.first()
        allDecisions.remove(decision)
        decision.noMoreRobots().takeIf { it.geodeCount > best.geodeCount }?.let { best = it }

        val newDecisions = decision.next(blueprint).filter { it !in allDecisions }

        allDecisions.addAll(newDecisions)
    }

    return best.geodeCount.also { println("Blueprint #${blueprint.number}: ${best.geodeCount}") }
}

internal enum class ResourceType {
    ORE, CLAY, OBSIDIAN, GEODE
}
internal fun File.readBluePrints(): List<Blueprint> = readLines().map { it.toBluePrint() }

private fun String.toBluePrint(): Blueprint {
    val (name, robots) = split(": ", limit = 2)

    return Blueprint(
        name.substringAfter("Blueprint ").toInt(),
        robots.split(".").filter(String::isNotEmpty).map { it.trim().toRobot() }
    )
}

private fun String.toRobot(): Robot {
    val robotType = substringAfter("Each ").substringBefore(" robot costs ").toResource()
    val cost = substringAfter(" robot costs ")
        .split(" and ")
        .associateTo(EnumMap(ResourceType::class.java)) {
            val (cost, resource) = it.split(" ")
            resource.toResource() to cost.toInt()
        }

    return Robot(robotType, cost)
}
private fun String.toResource(): ResourceType = ResourceType.valueOf(uppercase(Locale.ROOT))

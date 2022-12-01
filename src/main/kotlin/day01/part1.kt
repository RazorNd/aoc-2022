package day01

fun main() {
    val caloriesByElf = readCalories()

    val maxCalories = caloriesByElf.maxOfOrNull { it.sum() }

    println(maxCalories)
}

package day01

fun main() {
    val calories = readCalories()
        .map { it.sum() }
        .sortedDescending()

    println(calories.take(3).sum())
}

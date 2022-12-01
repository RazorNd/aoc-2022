package day01

import java.io.File

fun readCalories(): List<List<Int>> {
    val caloriesByElf = mutableListOf<MutableList<Int>>(mutableListOf())

    File("input/day01/input.txt").bufferedReader()
        .lineSequence()
        .forEach {
            if (it.isBlank()) {
                caloriesByElf.add(mutableListOf())
            } else {
                caloriesByElf.last().add(it.toInt())
            }
        }

    return caloriesByElf
}

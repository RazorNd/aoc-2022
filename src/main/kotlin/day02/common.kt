package day02

enum class Shape(val scope: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    fun outcomeScore(opponent: Shape): Int = when (opponent) {
        defeatShape() -> 6
        this -> 3
        else -> 0
    }

    fun defeatShape() = when (this) {
        ROCK -> SCISSORS
        PAPER -> ROCK
        SCISSORS -> PAPER
    }

    fun loseShape(): Shape = checkNotNull(LOSE_MAP[this])

    companion object {
        val LOSE_MAP = values().associateBy { it.defeatShape() }
    }
}

fun opponentChose(value: String) = when (value) {
    "A" -> Shape.ROCK
    "B" -> Shape.PAPER
    "C" -> Shape.SCISSORS
    else -> throw IllegalArgumentException("Wrong opponent chose: $value")
}

fun calculateScore(opponent: Shape, you: Shape): Int = you.scope + you.outcomeScore(opponent)





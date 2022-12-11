package day11


import java.io.File
import kotlin.math.max
import kotlin.math.pow


fun calcPrimes(size: Int): MutableList<Long> {
    val list = ArrayList<Long>(size)
    var number = 2L
    while (list.size < size) {
        if (list.none { number divisibleBy it }) {
            list.add(number)
        }
        number++
    }
    return list
}

class FactorizedNumber private constructor(private val primes: ShortArray) {

    constructor(value: Long) : this(factorize(value))

    private constructor(primes: List<Int>) : this(primes.map { it.toShort() }.toShortArray())

    fun toLong(): Long {
        return primes.withIndex()
            .filter { (_, v) -> v != 0.toShort() }
            .map { (i, v) -> PRIMES[i].toDouble().pow(v.toInt()).toLong() }
            .reduce(Long::times)
    }

    infix fun lcm(other: FactorizedNumber): FactorizedNumber {
        return FactorizedNumber(primesPairs(other).map { (a, b) -> max(a.toInt(), b.toInt()) })
    }

    private fun primesPairs(other: FactorizedNumber) = primes.zip(other.primes)

    companion object {

        private const val SIZE = 100
        private val PRIMES = calcPrimes(SIZE)
        private fun factorize(value: Long): ShortArray {
            val array = ShortArray(SIZE) { 0 }

            if (value == 1L) {
                return array
            }

            var v = value

            PRIMES.forEachIndexed { index, prime ->
                while (v divisibleBy prime) {
                    v /= prime
                    array[index]++
                    if (v == 1L) {
                        return array
                    }
                }
            }

            throw IllegalArgumentException("Can't factorize number: $value, $v")
        }
    }
}


fun main() {

    val monkeys = File("input/day11/input.txt").readMonkeys()

    val lcm = monkeys.map { FactorizedNumber(it.divider) }.reduce(FactorizedNumber::lcm).toLong()

    val simulation = Simulation(monkeys) { item -> item.worryLevel %= lcm }

    val printRound = setOf(1, 20) + (1_000..10_000 step 1_000)
    (1..10_000).forEach { round ->
        simulation.round()
        if (round in printRound) {
            println("== After round $round ==")
            println(simulation.monkeyInspectCount())
        }
    }

    println()
    println()
    println("=== Monkey Business: ${simulation.monkeyBusiness()} ===")
}

import java.io.File

object Day1 {
    private val spelledDigits = mapOf(
        "ZERO" to 0,
        "ONE" to 1,
        "TWO" to 2,
        "THREE" to 3,
        "FOUR" to 4,
        "FIVE" to 5,
        "SIX" to 6,
        "SEVEN" to 7,
        "EIGHT" to 8,
        "NINE" to 9,
    )

    fun firstPart(lines: Collection<String>) {
        val sumByLine = lines.map { line ->
            line.filter { it.isDigit() }.let {
                it.first().digitToInt() * 10 + it.last().digitToInt() }
        }
        sumByLine.sumOf { it }.also {
            println(it)
        }
    }

    fun secondPart(lines: Collection<String>) = lines.map { line ->
        var newLine = line
        newLine.uppercase().findAnyOf(spelledDigits.keys)?.let {
            newLine = newLine.replace(it.second.lowercase(),  spelledDigits[it.second].toString() + it.second.lowercase())
        }
        newLine.uppercase().findLastAnyOf(spelledDigits.keys)?.let {
            newLine = newLine.replace(it.second.lowercase(), it.second.lowercase() + spelledDigits[it.second].toString())
        }

        newLine
    }.let { firstPart(it) }

}


fun main() {
    val puzzleInput = File("input/puzzle1_input").useLines { it.toList() }

    print("Puzzle Input First Part: ")
    Day1.firstPart(puzzleInput)

    print("Puzzle Input Second Part: ")
    Day1.secondPart(puzzleInput)
}


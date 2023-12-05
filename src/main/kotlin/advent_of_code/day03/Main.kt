package main.kotlin.advent_of_code.day03

import java.io.File
import java.util.Collections

val partRegex = "\\d+".toRegex()
val specialCharsRegex = "[^a-zA-Z\\d]".toRegex()

fun main() {
    val puzzleInput = File("./input.txt").useLines { it.toList() }

    firstPart(puzzleInput)
    secondPart(puzzleInput)


}

fun IntRange.lookForSymbol(allElements: List<String>, currentLine: Int): Boolean {
    val range = (this.first..this.last step this.step).toList()

    if (range.any { checkSides(currentLine, it, allElements) }) return true
    return false

}

// Checks all of the sides of a given position and tells us if there is a special character there
fun checkSides(line: Int, column: Int, allElements: List<String>): Boolean {
    val whatToCheck = listOf(-1, 0, 1)

    for (columnVar in whatToCheck) {
        for (lineVar in whatToCheck) {
            try {
                if (!allElements[line + lineVar][column + columnVar].isDigit() && allElements[line + lineVar][column + columnVar] != '.')
                    return true
            } catch (_: java.lang.Exception) {
            }
        }
    }

    return false
}


fun firstPart(puzzleInput: List<String>) {
    var accumulator = 0
    for ((i, line) in puzzleInput.withIndex()) {
        if (line.contains(partRegex)) {
            val groups = partRegex.findAll(puzzleInput[i]).map { Triple(it.groupValues, it.value, it.range) }.toList()

            groups.forEach { numberGroup ->
                val number = numberGroup.second.toInt()

                if (numberGroup.third.lookForSymbol(puzzleInput, i)) {
                    accumulator += number
                }
            }
        }
    }

    println(accumulator)
}

fun secondPart(puzzleInput: List<String>) {
    var accumulator = 0
    for (i in 1 until puzzleInput.size) {
        val matched = "\\*".toRegex().findAll(puzzleInput[i])
        matched.forEach { gearMatched ->
            if (puzzleInput[i].contains("*")) {
                val previousLine = puzzleInput[i - 1]
                val nextLine = puzzleInput[i + 1]

                val previousLineGroups =
                    partRegex.findAll(previousLine).map { it }.toList()
                val nextLineGroups =
                    partRegex.findAll(nextLine).map { it }.toList()

                previousLineGroups.forEach { previousLineGroup ->
                    nextLineGroups.forEach { nextLineGroup ->
                        if (isInsideOrOneApart(previousLineGroup.range, gearMatched.range.first) && isInsideOrOneApart(nextLineGroup.range, gearMatched.range.first)) {
                            accumulator += previousLineGroup.value.toInt() * nextLineGroup.value.toInt()
                        }
                    }
                }

            }
        }
        val matchedSideBySide = "(\\d+)\\*(\\d+)".toRegex().findAll(puzzleInput[i])
        matchedSideBySide.forEach { matchedGroupSideBySide ->
            accumulator += matchedGroupSideBySide.groups[1]!!.value.toInt() * matchedGroupSideBySide.groups[2]!!.value.toInt()
        }

    }



    println(accumulator)

}

fun isInsideOrOneApart(range: IntRange, number: Int): Boolean {
    return number in range || number + 1 in range || number - 1 in range
}

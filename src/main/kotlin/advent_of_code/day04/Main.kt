package main.kotlin.advent_of_code.day04

import java.io.File
import kotlin.math.pow

val cardNumberRegex = "\\d+".toRegex()

data class CardCopyMachine(
    var totalCards: Int = 0,
    val cardCopies: HashMap<Int, Int> = HashMap()
) {
    fun addCopies(card: Int, numberOfCopies: Int, times: Int) {
        val goUntil = if (card + numberOfCopies + 1 > totalCards) totalCards else card + numberOfCopies + 1
        (0 until times).forEach { _ ->
            (card + 1 until goUntil).forEach {
                cardCopies[it] = (cardCopies[it] ?: 0) + 1
            }
        }
    }

    fun initMachine(cards: Int) {
        totalCards = cards
        (0 until cards).forEach { card ->
            cardCopies[card] = 1
        }
    }
}

fun main() {
    val puzzleInput = File("./input.txt").useLines { it.toList() }

    println(firstPart(puzzleInput))
    println(secondPart(puzzleInput))

}

private fun secondPart(puzzleInput: Collection<String>): Int {
    val machine = CardCopyMachine()
    machine.initMachine(puzzleInput.size)

    puzzleInput.forEach { line ->
        val (card, numbers) = line.split(":")
        val (winningNumbers, myNumbers) = numbers.split(" | ")

        val winningNumbersParsed = "\\d+".toRegex().findAll(winningNumbers).mapTo(HashSet()) { it.value }
        val myNumbersParsed = "\\d+".toRegex().findAll(myNumbers).mapTo(HashSet()) { it.value }

        val intersection = winningNumbersParsed.intersect(myNumbersParsed)
        val cardNumber = cardNumberRegex.find(card)!!.value.toInt()

        machine.addCopies(cardNumber - 1, intersection.size, machine.cardCopies[cardNumber - 1]!!)

        intersection.size * machine.cardCopies[cardNumber - 1]!!
    }

    return machine.cardCopies.values.sum()

}

private fun firstPart(puzzleInput: Collection<String>): Int {
    return puzzleInput.sumOf { line ->
        val (card, numbers) = line.split(":")
        val (winningNumbers, myNumbers) = numbers.split(" | ")

        val winningNumbersParsed = "\\d+".toRegex().findAll(winningNumbers).mapTo(HashSet()) { it.value }
        val myNumbersParsed = "\\d+".toRegex().findAll(myNumbers).mapTo(HashSet()) { it.value }

        val intersection = winningNumbersParsed.intersect(myNumbersParsed)
        2.0.pow(intersection.size.toDouble() - 1).toInt()
    }
}
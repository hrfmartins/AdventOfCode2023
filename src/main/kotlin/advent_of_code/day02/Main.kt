package main.kotlin.advent_of_code.day02

import java.io.File

data class Game(
    val gameNumber: Int,
    val plays: Collection<BlocksDraw> = emptyList(),
)

data class BlocksDraw(
    val redPieces: Int = 0,
    val greenPieces: Int = 0,
    val bluePieces: Int = 0
)

enum class BlocksColor {
    RED,
    BLUE,
    GREEN
}

val maximums = mapOf(
    BlocksColor.RED to 12,
    BlocksColor.BLUE to 14,
    BlocksColor.GREEN to 13,
)


val gameNumberRegex = "(?<=Game )\\d+".toRegex()

fun main() {
    val puzzleInput = File("./input.txt").useLines { it.toList() }

    val allPiecesInGame = puzzleInput.fold(ArrayList<Game>()) { acc, game ->
        val (gameAndNumber, gamePlays) = game.split(": ")
        val multiplePlays = gamePlays.split("; ")

        val allPlaysInAGame = multiplePlays.map { playsOfGame ->
            val blocksDraw = BlocksDraw()
            val plays = playsOfGame.split(", ").map {
                val blocks = it.split(" ")[0].toInt()
                val color = BlocksColor.valueOf(it.split(" ")[1].uppercase())
                Pair(blocks, color)
            }

            plays.groupBy { it.second }.let {
                blocksDraw.copy(
                    bluePieces = it[BlocksColor.BLUE]?.first()?.first ?: 0,
                    redPieces = it[BlocksColor.RED]?.first()?.first  ?: 0,
                    greenPieces = it[BlocksColor.GREEN]?.first()?.first  ?: 0
                )
            }
        }

        acc += Game(
            gameNumberRegex.find(gameAndNumber)!!.value.toInt(),
            plays = allPlaysInAGame
        )
        acc
    }

    println(allPiecesInGame.filter { game ->
        game.plays.all {
            maximums[BlocksColor.BLUE]!! >= it.bluePieces &&
                    maximums[BlocksColor.RED]!! >= it.redPieces &&
                    maximums[BlocksColor.GREEN]!! >= it.greenPieces
        }

    }.sumOf { it.gameNumber })

    println(allPiecesInGame.sumOf { game ->
        val maxGreen = game.plays.maxOf { it.greenPieces }
        val maxBlue = game.plays.maxOf { it.bluePieces }
        val maxRed = game.plays.maxOf { it.redPieces }
        maxGreen * maxBlue * maxRed
    })
}
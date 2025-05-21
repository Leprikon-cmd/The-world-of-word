// CrosswordBuilder.kt)

package com.example.thewordgamefixed.logic

object CrosswordBuilder {

    data class PositionedWord(
        val word: String,
        val x: Int, // координаты первого символа
        val y: Int,
        val isHorizontal: Boolean
    )

    data class CrosswordResult(
        val grid: Array<CharArray>,
        val placedWords: List<PositionedWord>
    )

    fun buildCrossword(words: List<String>): CrosswordResult? {
        if (words.isEmpty()) return null

        val sorted = words.sortedByDescending { it.length }
        val baseWord = sorted.first()
        val others = sorted.drop(1).toMutableList()

        // Определим размеры поля с запасом
        val gridSize = 15 // 15x15 поле
        val grid = Array(gridSize) { CharArray(gridSize) { ' ' } }
        val centerY = gridSize / 2
        val centerX = (gridSize - baseWord.length) / 2

        // Помещаем базовое слово горизонтально
        for (i in baseWord.indices) {
            grid[centerY][centerX + i] = baseWord[i]
        }

        val placedWords = mutableListOf(
            PositionedWord(baseWord, centerX, centerY, isHorizontal = true)
        )

        val usedPositions = mutableSetOf<Pair<Int, Int>>()
        baseWord.forEachIndexed { i, c ->
            usedPositions.add(Pair(centerX + i, centerY))
        }

        for (word in others) {
            var placed = false
            for (i in word.indices) {
                val letter = word[i]
                for ((j, baseChar) in baseWord.withIndex()) {
                    if (baseChar == letter) {
                        val crossX = centerX + j
                        val crossY = centerY - i // начальная координата сверху вниз

                        if (crossY >= 0 && crossY + word.length <= gridSize) {
                            var fits = true
                            for (k in word.indices) {
                                val y = crossY + k
                                val existing = grid[y][crossX]
                                if (existing != ' ' && existing != word[k]) {
                                    fits = false
                                    break
                                }
                            }

                            if (fits) {
                                for (k in word.indices) {
                                    grid[crossY + k][crossX] = word[k]
                                    usedPositions.add(Pair(crossX, crossY + k))
                                }
                                placedWords.add(PositionedWord(word, crossX, crossY, isHorizontal = false))
                                placed = true
                                break
                            }
                        }
                    }
                }
                if (placed) break
            }
        }

        return CrosswordResult(grid, placedWords)
    }
}

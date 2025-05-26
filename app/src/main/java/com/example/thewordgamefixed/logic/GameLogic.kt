package com.example.thewordgamefixed.logic

import android.util.Log

object GameLogic {

    private var currentLetters: List<Char> = emptyList()
    private var validWords: Set<String> = emptySet()

    fun generateNewLevel() {
        val set = LetterSetGenerator.generateSet()
        if (set != null) {
            currentLetters = set.letters
            validWords = set.words

            Log.d("GameLogic", "Сгенерированы буквы: $currentLetters")
            Log.d("GameLogic", "Допустимых слов: ${validWords.size}")
            validWords.sortedByDescending { it.length }.forEach {
                Log.d("GameWords", it)
            }

        } else {
            currentLetters = listOf('А', 'Р', 'С', 'Т', 'У') // fallback
            validWords = emptySet()
            Log.w("GameLogic", "Не удалось сгенерировать набор, использован запасной")
        }
    }

    fun getLetters(): List<Char> = currentLetters

    fun isValidWord(input: String): Boolean {
        val word = input.lowercase()
        return word.length >= 2 && validWords.contains(word)
    }

    private fun canBuildWord(word: String, letters: List<Char>): Boolean {
        val available = letters.groupingBy { it }.eachCount().toMutableMap()
        for (c in word) {
            val count = available.getOrDefault(c, 0)
            if (count == 0) return false
            available[c] = count - 1
        }
        return true
    }

    fun getValidWords(): List<String> = validWords.toList()
}
// Логика игры — буквы звезды, проверка правильности слова
// Здесь будет подключён словарь и генерация уровней

package com.example.thewordgamefixed.logic

import com.example.thewordgamefixed.logic.DictionaryManager
import com.example.thewordgamefixed.logic.LetterSetGenerator

// ++ Логика текущей звезды: буквы + допустимые слова
object GameLogic {

    // + Набор букв, полученный от генератора
    private var currentLetters: List<Char> = emptyList()
    private var validWords: Set<String> = emptySet()

    // + Генерация при запуске уровня
    fun generateNewLevel() {
        val set = LetterSetGenerator.generateSet()
        if (set != null) {
            currentLetters = set.letters
            validWords = set.words
            android.util.Log.d("GameLogic", "Сгенерированы буквы: $currentLetters")
            android.util.Log.d("GameLogic", "Допустимые слова: $validWords")
        } else {
            currentLetters = listOf('А', 'Р', 'С', 'Т', 'У') // fallback
            validWords = emptySet()
            android.util.Log.w("GameLogic", "Не удалось сгенерировать набор, использован запасной")
        }
    }

    fun getLetters(): List<Char> = currentLetters

    fun isValidWord(input: String): Boolean {
        val word = input.uppercase()
        return word.length >= 3 && validWords.contains(word)
    }

    // 🔍 Проверяет, можно ли составить слово поштучно из букв набора
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
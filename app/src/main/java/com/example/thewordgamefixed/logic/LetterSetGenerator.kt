// Генератор набора букв для игры: выбираем 5 букв и ищем все слова, которые можно составить из них (с повторами)
package com.example.thewordgamefixed.logic

object LetterSetGenerator {
    private const val LETTER_COUNT = 5            // 🔢 размер набора букв (звезда)
    private const val MIN_VALID_WORDS = 5         // 📉 минимальное количество слов из этих букв

    fun generateSet(): LetterSet? {
        val words = DictionaryManager.getWords()

        // 1️⃣ Выбираем случайное слово из 5 букв (можно с повторами)
        val candidates = words.filter { it.length == LETTER_COUNT }.shuffled()

        for (base in candidates) {
            val letters = base.toList() // ⬅️ сохраняем даже повторы (например, П, Л, А, Н, П)

            // 2️⃣ Находим все слова, которые можно собрать из этих букв с учётом количества каждой
            val matching = words.filter { w ->
                w.length >= 3 &&
                        canBuildWord(w, letters)
            }

            if (matching.size >= MIN_VALID_WORDS) {
                return LetterSet(letters, matching.toSet())
            }
        }

        return null // ❌ Не удалось — пусть пробует заново
    }

    // 🔧 Проверяет, можно ли собрать слово из available букв (учитывая повторы)
    private fun canBuildWord(word: String, available: List<Char>): Boolean {
        return word.all { it in available }
    }

    data class LetterSet(
        val letters: List<Char>,    // 🟡 5 букв (возможно с повторами)
        val words: Set<String>      // ✅ допустимые слова из этих букв
    )
}
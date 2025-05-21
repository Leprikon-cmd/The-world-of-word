package com.example.thewordgamefixed.logic

import kotlin.random.Random

object LetterSetGenerator {
    private const val LETTER_COUNT = 5
    private const val MIN_VALID_WORDS = 5 // Можно настраивать

    fun generateSet(): LetterSet? {
        val words = DictionaryManager.getWords()

        // Фильтруем только слова длиной от 5 и без повторяющихся букв
        val candidateWords = words.filter { it.length == LETTER_COUNT && it.toSet().size == LETTER_COUNT }

        val shuffledCandidates = candidateWords.shuffled()

        for (baseWord in shuffledCandidates) {
            val letters = baseWord.toSet().toList()

            // Ищем все слова длиной 3–5 букв, составленные из этих букв
            val matchingWords = words.filter { w ->
                w.length in 3..5 && w.all { it in letters }
            }.toSet()

            // Проверим, что среди них хотя бы MIN_VALID_WORDS и желательно — не однотипных
            val distinctFirstLetters = matchingWords.map { it.first() }.toSet()
            if (matchingWords.size >= MIN_VALID_WORDS && distinctFirstLetters.size >= 3) {
                return LetterSet(letters, matchingWords)
            }
        }

        return null // Не удалось найти подходящий набор
    }

    data class LetterSet(
        val letters: List<Char>,
        val words: Set<String>
    )
}
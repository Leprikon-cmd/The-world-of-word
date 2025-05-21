package com.example.thewordgamefixed.logic

import kotlin.random.Random

object LetterSetGenerator {
    private const val LETTER_COUNT = 5
    private const val MIN_VALID_WORDS = 5 // Можно настраивать

    fun generateSet(): LetterSet? {
        val words = DictionaryManager.getWords()

        // ✅ Слова из 5 уникальных букв
        val candidateWords = words.filter {
            it.length == LETTER_COUNT && it.toSet().size == LETTER_COUNT
        }.shuffled() // сразу перемешаем

        for (baseWord in candidateWords) {
            val letters = baseWord.toSet().toList()

            // ✅ Ищем все слова, которые можно составить из этих букв (от 3 до 5 символов, без повторов)
            val matchingWords = words.filter { w ->
                w.length in 3..5 &&
                        w.all { it in letters } &&
                        w.toSet().size == w.length // 🔥 исключаем слова с повторяющимися буквами
            }

            // ✅ Проверка разнообразия — хотя бы 3 разные первые буквы
            val groupedByFirst = matchingWords.groupBy { it.first() }

            // Обрезаем: не более 2 слов от каждой первой буквы
            val limited = groupedByFirst.values
                .flatMap { it.take(2) } // <= здесь ограничиваем 2 словами на букву
                .take(MIN_VALID_WORDS)

            val distinctFirstLetters = limited.map { it.first() }.toSet()

            if (limited.size >= MIN_VALID_WORDS && distinctFirstLetters.size >= 3) {
                return LetterSet(letters, limited.toSet())
            }
        }

        // ❗ Этот return должен быть ВНЕ цикла
        return null // Не нашли подходящий набор — можно будет попробовать ещё раз
    }

    data class LetterSet(
        val letters: List<Char>,
        val words: Set<String>
    )
}
package com.example.thewordgamefixed.logic

object LetterSetGenerator {
    private const val LETTER_COUNT = 5 // размер набора букв (буквы звезды)
    private const val MIN_VALID_WORDS = 5 // минимальное количество слов из набора

    fun generateSet(): LetterSet? {
        val words = DictionaryManager.getWords()

        // 🔎 1. Выбираем слова длиной ровно 5 букв без повторяющихся символов
        val candidateWords = words.filter {
            it.length == LETTER_COUNT && it.toSet().size == LETTER_COUNT
        }.shuffled() // Перемешиваем, чтобы была случайность

        for (baseWord in candidateWords) {
            val letters = baseWord.toSet().toList() // 5 уникальных букв

            // 🔎 2. Находим все слова, которые можно составить из этих букв
            val matchingWords = words.filter { w ->
                w.length in 3..5 &&                  // длина от 3 до 5 символов
                        w.all { it in letters } &&           // только буквы из базового набора
                        w.toSet().size == w.length           // без повторяющихся символов
            }

            // 🎯 3. Хотим разнообразия — группируем по первой букве
            val groupedByFirst = matchingWords.groupBy { it.first() }

            // ✂️ 4. Обрезаем: максимум 2 слова на каждую первую букву
            val limited = groupedByFirst.values
                .flatMap { it.take(2) }
                .take(MIN_VALID_WORDS)

            // ✅ 5. Проверка: достаточно слов и хотя бы 3 разных начальных буквы
            val distinctFirstLetters = limited.map { it.first() }.toSet()

            if (limited.size >= MIN_VALID_WORDS && distinctFirstLetters.size >= 3) {
                return LetterSet(letters, limited.toSet()) // 🎉 Готовый набор
            }
        }

        // ❌ Не удалось — можно будет попробовать снова
        return null
    }

    data class LetterSet(
        val letters: List<Char>, // 5 букв — будут расставлены по звезде
        val words: Set<String>   // допустимые слова из этих букв
    )
}
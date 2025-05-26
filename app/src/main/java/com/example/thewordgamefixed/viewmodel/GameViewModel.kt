package com.example.thewordgamefixed.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thewordgamefixed.logic.GameLogic
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    val backgroundImage = mutableStateOf("Background1.jpg") // 🎨 текущий фон

    val lastResultSymbol = mutableStateOf<String?>(null) // Значек угадал не угадал

    val showOverlay = mutableStateOf(false)

    fun pickNewBackground() {
        val index = (1..62).random() // 📸 случайный номер картинки
        backgroundImage.value = "Background$index.jpg"
    }

    val selectedLetters = mutableStateListOf<Char>() // ✍️ текущее набранное слово (посимвольно)
    val result = mutableStateOf("") // 💬 результат ("✅ слово", "❌")

    val validWords = mutableStateListOf<String>() // 📋 допустимые слова из GameLogic
    val foundWords = mutableStateListOf<String>() // ✅ угаданные слова

    private var lastAddedChar: Char? = null // 🧠 защита от многократного тапа по той же букве подряд

    val score = mutableStateOf(0) // 🧮 текущий счёт

    val showWinDialog = mutableStateOf(false) // 🏆 победа

    fun resetGame() {
        foundWords.clear()              // сбрасываем найденные слова
        score.value = 0                 // сбрасываем счёт
        result.value = ""               // сбрасываем сообщение
        selectedLetters.clear()         // сбрасываем ввод
        updateWords()                   // обновляем список допустимых слов
    }

    fun validateWord() {
        val word = selectedLetters.joinToString("")
        tryAddWord(word)                // 👈 добавляем слово, если оно допустимо
        clearSelection()                // 🧹 очищаем ввод после попытки
    }

    fun tryAddWord(word: String) {
        if (GameLogic.isValidWord(word) && !foundWords.contains(word)) {
            foundWords.add(word)
            addScore(word.length)
            result.value = "✅ $word"
            lastResultSymbol.value = "✅"

            // ✅ Если все слова угаданы — показать победу
            if (foundWords.size == GameLogic.getValidWords().size) {
                showWinDialog.value = true
            }

            // 🎉 Проверка на полный проход
            if (foundWords.size == GameLogic.getValidWords().size) {
                val bonus = foundWords.size * 5
                score.value += bonus
                result.value += " 🎁 +$bonus бонусных очков!"
            }
        } else {
            result.value = "❌"
            lastResultSymbol.value = "❌"
        }

        viewModelScope.launch {
            delay(1500)
            lastResultSymbol.value = null
        }
    }

    fun getFoundWordCount(): Int = foundWords.size // 👉 количество угаданных
    fun getTotalValidWordCount(): Int = GameLogic.getValidWords().size // 👉 всего возможных

    private fun addScore(length: Int) {
        // 📊 начисляем очки по длине слова
        val base = when (length) {
            in 2..3 -> 5
            4 -> 10
            5 -> 20
            6 -> 30
            else -> 40 + (length - 6) * 10
        }
        score.value += base
    }

    fun updateWords() {
        validWords.clear()
        validWords.addAll(GameLogic.getValidWords().sortedByDescending { it.length }) // 📥 обновляем список слов
    }

    fun addLetter(letter: Char) {
        selectedLetters.add(letter) // ➕ добавляем букву в ввод
    }

    fun clearSelection() {
        selectedLetters.clear()     // 🧹 очищаем текущий ввод
        result.value = ""           // 🧹 сбрасываем сообщение
        lastAddedChar = null        // 🔄 сброс защиты
    }

    fun getWord(): String = selectedLetters.joinToString("") // 🔤 текущее слово
}
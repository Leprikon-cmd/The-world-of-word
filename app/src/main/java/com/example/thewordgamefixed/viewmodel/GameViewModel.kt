package com.example.thewordgamefixed.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.thewordgamefixed.logic.GameLogic

class GameViewModel : ViewModel() {

    val backgroundImage = mutableStateOf("Background1.jpg") // 🎨 текущий фон

    fun pickNewBackground() {
        val index = (1..17).random() // 📸 случайный номер картинки
        backgroundImage.value = "Background$index.jpg"
    }

    val selectedLetters = mutableStateListOf<Char>() // ✍️ текущее набранное слово (посимвольно)
    val result = mutableStateOf("") // 💬 результат ("✅ слово", "❌")

    val validWords = mutableStateListOf<String>() // 📋 допустимые слова из GameLogic
    val foundWords = mutableStateListOf<String>() // ✅ угаданные слова

    private var lastAddedChar: Char? = null // 🧠 защита от многократного тапа по той же букве подряд

    val score = mutableStateOf(0) // 🧮 текущий счёт

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
            foundWords.add(word)       // ✅ добавляем слово в найденные
            addScore(word.length)      // ➕ начисляем очки
            result.value = "✅ $word"   // сообщение пользователю
        } else {
            result.value = "❌"         // ❌ неправильное слово
        }
    }

    fun getFoundWordCount(): Int = foundWords.size // 👉 количество угаданных
    fun getTotalValidWordCount(): Int = GameLogic.getValidWords().size // 👉 всего возможных

    private fun addScore(length: Int) {
        // 📊 начисляем очки по длине слова
        val base = when (length) {
            in 3..4 -> 10
            5 -> 20
            6 -> 30
            else -> 40 + (length - 6) * 5
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
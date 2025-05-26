package com.example.thewordgamefixed.logic

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

object DictionaryManager {

    // 🔤 Список всех слов
    private val allWords = mutableSetOf<String>()

    // 🧠 Полный словарь: слово -> данные (определение + автор)
    private val fullDictionary = mutableMapOf<String, DictionaryItem>()

    // 📥 Загрузка словаря
    fun loadWords(context: Context) {
        try {
            val inputStream = context.assets.open("dictionary.json")
            val reader = InputStreamReader(inputStream, "UTF-8")
            val type = object : TypeToken<List<DictionaryItem>>() {}.type
            val items: List<DictionaryItem> = Gson().fromJson(reader, type)
            val withoutDefinition = items.count { it.definition.isBlank() }
            Log.d("DictionaryManager", "Слов без определения: $withoutDefinition")

            fullDictionary.clear()
            allWords.clear()

            for (item in items) {
                val word = item.word.lowercase()
                fullDictionary[word] = item
                allWords.add(word)  // ⬅️ загружаем все слова, без ограничений
            }

            Log.d("DictionaryManager", "Слов загружено: ${allWords.size}")
        } catch (e: Exception) {
            Log.e("DictionaryManager", "Ошибка загрузки словаря: ${e.message}")
        }
    }

    // 🔍 Все допустимые слова
    fun getWords(): Set<String> = allWords

    // 📘 Получить определение
    fun getDefinition(word: String): String {
        return fullDictionary[word.lowercase()]?.definition ?: "Нет определения"
    }

    // ✍ Получить автора
    fun getAuthor(word: String): String {
        return fullDictionary[word.lowercase()]?.author ?: "Неизвестно"
    }

    // 🔖 Структура одного элемента
    data class DictionaryItem(
        val word: String,
        val definition: String,
        val author: String
    )
}
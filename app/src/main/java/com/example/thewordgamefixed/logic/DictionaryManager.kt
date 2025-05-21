//Создадим менеджер словаря, который:
//	1.	Загружает слова из assets/words.txt
//	2.	Фильтрует по количеству букв (например, 5 букв)
//	3.	Хранит отфильтрованный список в памяти

package com.example.thewordgamefixed.logic

import android.content.Context
import android.util.Log

object DictionaryManager {
    private val allWords = mutableSetOf<String>()

    fun loadWords(context: Context) {
        try {
            val inputStream = context.assets.open("words.txt")
            inputStream.bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    val word = line.trim().uppercase()
                    if (word.length in 3..8) { // фильтр по длине, можно масштабировать
                        allWords.add(word)
                    }
                }
            }
            Log.d("DictionaryManager", "Слов загружено: ${allWords.size}")
        } catch (e: Exception) {
            Log.e("DictionaryManager", "Ошибка загрузки словаря: ${e.message}")
        }
    }

    // Возвращает все загруженные слова
    fun getWords(): Set<String> = allWords
}
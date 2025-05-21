// Главная активность — точка входа приложения
// Здесь вызывается GameScreen через setContent

package com.example.thewordgamefixed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.thewordgamefixed.ui.GameScreen
import com.example.thewordgamefixed.ui.TheWordGameFixedTheme
import com.example.thewordgamefixed.logic.GameLogic
import com.example.thewordgamefixed.logic.DictionaryManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DictionaryManager.loadWords(this)
        GameLogic.generateNewLevel() // ✅ подгружаем набор перед отображением
        setContent {
            TheWordGameFixedTheme {
                GameScreen()
            }
        }
    }
}
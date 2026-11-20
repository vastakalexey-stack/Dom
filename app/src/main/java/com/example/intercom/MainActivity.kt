package com.example.intercom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storage = AppStorage(this)
        val client = DomRuClient()
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                Surface(Modifier.fillMaxSize()) {
                    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Домик", style = MaterialTheme.typography.headlineMedium)
                        Text("Подключение домофона Дом.ру и камеры Xiaomi")
                        Text("Настройте доступ в приложении, чтобы получить видеопоток и управление дверью.")
                        var token by remember { mutableStateOf(storage.load().accessToken) }
                        OutlinedTextField(value = token, onValueChange = { token = it }, label = { Text("Токен Дом.ру") }, modifier = Modifier.fillMaxWidth())
                        Button(onClick = { storage.save(storage.load().copy(accessToken = token.trim())) }) { Text("Сохранить") }
                        Text("Проверка соединения и управление дверью будут доступны после авторизации.")
                    }
                }
            }
        }
    }
}

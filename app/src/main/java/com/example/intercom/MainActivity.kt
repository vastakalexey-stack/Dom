package com.example.intercom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private enum class DomikPage { HOME, DOMRU, XIAOMI, SETTINGS }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storage = AppStorage(this)

        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var page by remember { mutableStateOf(DomikPage.HOME) }
                    var config by remember { mutableStateOf(storage.load()) }
                    var token by remember { mutableStateOf(config.accessToken) }
                    var cameraUrl by remember { mutableStateOf(config.xiaomiRtspUrl) }
                    var saved by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "ДОМИК",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Домофон и умный дом в одном приложении",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            NavButton("Главная", page == DomikPage.HOME) { page = DomikPage.HOME }
                            NavButton("Дом.ру", page == DomikPage.DOMRU) { page = DomikPage.DOMRU }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            NavButton("Xiaomi", page == DomikPage.XIAOMI) { page = DomikPage.XIAOMI }
                            NavButton("Настройки", page == DomikPage.SETTINGS) { page = DomikPage.SETTINGS }
                        }

                        when (page) {
                            DomikPage.HOME -> {
                                SectionCard(
                                    title = "Дом.ру",
                                    subtitle = "Домофон, входящие вызовы и открытие двери",
                                    detail = "Раздел подготовлен. Для реальных звонков и открытия двери потребуется подключить подтверждённый способ авторизации Дом.ру."
                                ) {
                                    Button(onClick = { page = DomikPage.DOMRU }) {
                                        Text("Открыть Дом.ру")
                                    }
                                }
                                SectionCard(
                                    title = "Xiaomi Home",
                                    subtitle = "Камеры и устройства умного дома",
                                    detail = "Раздел подготовлен отдельно от Дом.ру. Управление устройствами появится после подключения поддерживаемого официального способа доступа."
                                ) {
                                    Button(onClick = { page = DomikPage.XIAOMI }) {
                                        Text("Открыть Xiaomi")
                                    }
                                }
                                Text(
                                    text = "Аккаунты Дом.ру и Xiaomi будут независимыми — вход в один сервис не заменяет вход в другой.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            DomikPage.DOMRU -> {
                                Text("ДОМОФОН ДОМ.РУ", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                                InfoCard("Что будет в разделе", "Входящие видеозвонки, ответ и разговор, отклонение вызова, открытие двери, список адресов и история вызовов — если эти функции доступны через официальный доступ Дом.ру.")
                                InfoCard("Подключение", "Пока не подключено. Мы не будем имитировать SMS-вход или показывать фальшивую кнопку открытия двери. Сначала нужен проверенный интерфейс авторизации и API.")
                                OutlinedButton(onClick = { page = DomikPage.SETTINGS }) {
                                    Text("Перейти к настройкам")
                                }
                            }

                            DomikPage.XIAOMI -> {
                                Text("XIAOMI HOME", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                                InfoCard("Устройства и камеры", "Здесь разместим список устройств, просмотр камер, управление и автоматизации. Реальные команды будут включены только после проверки официального способа доступа.")
                                InfoCard("Без регистрации разработчика", "Продолжаем без Xiaomi Developer. Это позволяет подготовить приложение и интерфейс, но само по себе не даёт приложению доступ ко всем устройствам Xiaomi Home.")
                                OutlinedButton(onClick = { page = DomikPage.SETTINGS }) {
                                    Text("Настроить адрес видеопотока")
                                }
                            }

                            DomikPage.SETTINGS -> {
                                Text("НАСТРОЙКИ", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                                InfoCard("Дом.ру", "Поле для технического токена оставлено только для совместимости с текущей заготовкой. Не вводите сюда пароль от личного кабинета или SMS-код.")
                                OutlinedTextField(
                                    value = token,
                                    onValueChange = { token = it; saved = false },
                                    label = { Text("Технический токен Дом.ру") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = cameraUrl,
                                    onValueChange = { cameraUrl = it; saved = false },
                                    label = { Text("RTSP-адрес камеры (если есть)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                Button(onClick = {
                                    config = config.copy(
                                        accessToken = token.trim(),
                                        xiaomiRtspUrl = cameraUrl.trim()
                                    )
                                    storage.save(config)
                                    saved = true
                                }) {
                                    Text("Сохранить настройки")
                                }
                                if (saved) {
                                    Text("Настройки сохранены на этом телефоне.", color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Версия интерфейса 0.2 • Интеграции подключаются отдельно",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun NavButton(label: String, selected: Boolean, onClick: () -> Unit) {
    if (selected) {
        Button(onClick = onClick) { Text(label) }
    } else {
        OutlinedButton(onClick = onClick) { Text(label) }
    }
}

@androidx.compose.runtime.Composable
private fun SectionCard(
    title: String,
    subtitle: String,
    detail: String,
    action: @androidx.compose.runtime.Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            action()
        }
    }
}

@androidx.compose.runtime.Composable
private fun InfoCard(title: String, detail: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(detail, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

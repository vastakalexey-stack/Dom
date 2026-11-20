package com.example.intercom

import android.content.Context

data class AppConfig(
    val accessToken: String = "",
    val operatorId: String = "1",
    val placeId: String = "",
    val accessControlId: String = "",
    val xiaomiRtspUrl: String = "rtsp://192.168.1.50:8554/xiaomi_entrance"
)

class AppStorage(context: Context) {
    private val prefs = context.getSharedPreferences("intercom_prefs", Context.MODE_PRIVATE)
    fun load() = AppConfig(
        accessToken = prefs.getString("access_token", "") ?: "",
        operatorId = prefs.getString("operator_id", "1") ?: "1",
        placeId = prefs.getString("place_id", "") ?: "",
        accessControlId = prefs.getString("access_control_id", "") ?: "",
        xiaomiRtspUrl = prefs.getString("xiaomi_url", "rtsp://192.168.1.50:8554/xiaomi_entrance") ?: ""
    )
    fun save(config: AppConfig) {
        prefs.edit().putString("access_token", config.accessToken)
            .putString("operator_id", config.operatorId).putString("place_id", config.placeId)
            .putString("access_control_id", config.accessControlId)
            .putString("xiaomi_url", config.xiaomiRtspUrl).apply()
    }
}

class DomRuClient

package com.example.app_grupo9.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.app_grupo9.data.model.UserDto
import com.google.gson.Gson

class TokenManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("ueapd_secure_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveAuth(token: String, user: UserDto) {
        prefs.edit()
            .putString("jwt_token", token)
            .putString("user_json", gson.toJson(user))
            .apply()
    }

    fun getToken(): String? {
        return prefs.getString("jwt_token", null)
    }

    fun getUser(): UserDto? {
        val json = prefs.getString("user_json", null) ?: return null
        return try {
            gson.fromJson(json, UserDto::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}

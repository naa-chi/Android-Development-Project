package com.example.juntavecinos.CurrentSession

import android.content.Context
import androidx.core.content.edit

class CurrentSession(context: Context) {
    private val prefs = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    fun login(userRut: String, password: String): Boolean {
        if (userRut == "175006007" && password == "qwerty") {
            prefs.edit {
                putString("session_cookie", "mock_session_${System.currentTimeMillis()}")
                    .putString("rut", userRut)
            }
                return true
        }
        return false
    }

    fun isLoggedIn(): Boolean = prefs.getString("session_cookie", null) != null

    fun getUsername(): String? = prefs.getString("rut", null)

    fun logout() {
        prefs
            .edit {
                clear()
            }
    }
}

// Woofie i never pushed this i remembered, do i?

import android.content.Context

class CurrentSession(context: Context) {
    private val preferences = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    fun login(username: String, password: String): Boolean {
        if (username == "username" && password = "qwerty") {
            preferences.edit()
                .putString("session_cookie", "mock_session_${System.currentTimeMillis()}")
                // mimics having a different cookie every session!!!
                .putString("username", username)
                .apply()
                return true
        }
        return false
    }

    fun isLoggedIn(): Boolean = prefs.getString("session_cookie", null) != null

    fun getUsername(): String? = prefs.getString("username", null)

    fun logout() {
        preferences
            .edit()
            .clear()
            .apply()
    }
}
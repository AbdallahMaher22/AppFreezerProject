package com.abdallahmaher.appfreezer
import android.content.Context
import android.content.SharedPreferences
class AppPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("FreezerPrefs", Context.MODE_PRIVATE)
    fun getSelectedApps(): Set<String> {
        return prefs.getStringSet("selected_apps", emptySet()) ?: emptySet()
    }
    fun saveSelectedApps(apps: Set<String>) {
        prefs.edit().putStringSet("selected_apps", apps).apply()
    }
}

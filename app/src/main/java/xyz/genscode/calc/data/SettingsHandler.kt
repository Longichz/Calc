package xyz.genscode.calc.data

import android.content.Context

class SettingsHandler private constructor() {
    var theme = 0
    var hint = false

    fun getSettings(context: Context) { //Настройки загружаются единожды в TasksActivity.onCreate()
        val settings = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
        theme = settings.getInt(THEME, THEME_SYSTEM)
        hint = settings.getBoolean(HINT, true)
    }

    companion object {
        val instance = SettingsHandler()

        //Storage
        const val STORAGE_NAME = "settings"
        const val THEME = "theme"
        const val HINT = "hint"

        //Значения
        const val THEME_SYSTEM = 0
        const val THEME_NIGHT = 1
        const val THEME_DAY = 2
    }
}
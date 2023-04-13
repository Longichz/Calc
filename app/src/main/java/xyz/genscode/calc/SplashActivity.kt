package xyz.genscode.calc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import xyz.genscode.calc.data.SettingsHandler
import xyz.genscode.calc.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    lateinit var b : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(b.root)

        //Просто показываем логотип
    }

    override fun onStart() {
        super.onStart()
        //Определяем тему приложения
        val settings = getSharedPreferences(SettingsHandler.STORAGE_NAME, Context.MODE_PRIVATE)
        val theme = settings.getInt(SettingsHandler.THEME, SettingsHandler.THEME_AUTO)
        setThemeForActivity(theme)

        if(!SettingsHandler.instance.isActivityCreated) {
            SettingsHandler.instance.isActivityCreated = true
            Handler().postDelayed({
                intent = Intent(this, TasksActivity::class.java)
                startActivity(intent)
                SettingsHandler.instance.isActivityCreated = true
                super.finish()
            }, 1500)
        }
    }

    fun setThemeForActivity(mode: Int) {
        val theme: Int
        theme = if (mode == -1) { //Если передаем -1, тема ставится автоматически по настройкам
            SettingsHandler.instance.theme
        } else {
            mode
        }
        when (theme) {
            SettingsHandler.THEME_AUTO -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            SettingsHandler.THEME_NIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            SettingsHandler.THEME_DAY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
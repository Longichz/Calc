package xyz.genscode.calc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import xyz.genscode.calc.data.LevelHandler
import xyz.genscode.calc.data.SettingsHandler
import xyz.genscode.calc.databinding.ActivityTasksBinding
import xyz.genscode.calc.utils.ui.HoverUtils

class TasksActivity : AppCompatActivity() {
    lateinit var b : ActivityTasksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(b.root)

        //Загружаем настройки
        SettingsHandler.instance.getSettings(this)

        //Назначаем Hover
        HoverUtils().setHover(b.llTaskMultiply)
        HoverUtils().setHover(b.llTaskDif)
        HoverUtils().setHover(b.llTaskSum)
        HoverUtils().setHover(b.llTaskDiv)
        HoverUtils().setHover(b.llTaskSquared)
        HoverUtils().setHover(b.llTaskCubed)
        HoverUtils().setHover(b.llTaskEasyRandom)
        HoverUtils().setHover(b.llTaskMultiplyMedium)
        HoverUtils().setHover(b.ivOpenSettings)

        val intent = Intent(this, MainActivity::class.java)
        b.llTaskMultiply.setOnClickListener {//умножение
            intent.putExtra("type", LevelHandler.TYPE_MULTIPLY)
            startActivity(intent)
        }

        b.llTaskSum.setOnClickListener {//сложение
            intent.putExtra("type", LevelHandler.TYPE_SUM)
            startActivity(intent)
        }

        b.llTaskDif.setOnClickListener {//вычитание
            intent.putExtra("type", LevelHandler.TYPE_DIF)
            startActivity(intent)
        }

        b.llTaskDiv.setOnClickListener {//деление
            intent.putExtra("type", LevelHandler.TYPE_DIV)
            startActivity(intent)
        }

        b.llTaskEasyRandom.setOnClickListener {//рандом
            intent.putExtra("type", LevelHandler.TYPE_RANDOM)
            startActivity(intent)
        }

        b.llTaskSquared.setOnClickListener {//квадрат
            intent.putExtra("type", LevelHandler.TYPE_SQUARED)
            startActivity(intent)
        }

        b.llTaskCubed.setOnClickListener {//куб
            intent.putExtra("type", LevelHandler.TYPE_CUBED)
            startActivity(intent)
        }
        b.llTaskMultiplyMedium.setOnClickListener {//куб
            intent.putExtra("type", LevelHandler.TYPE_MULTIPLY)
            intent.putExtra("level", LevelHandler.LEVEL_MEDIUM)
            startActivity(intent)
        }

        b.ivOpenSettings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

        setThemeForActivity()
    }

    //Загружаем тему из настроек
    private fun setThemeForActivity() {
        when (SettingsHandler.instance.theme) {
            SettingsHandler.THEME_AUTO -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            SettingsHandler.THEME_NIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            SettingsHandler.THEME_DAY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
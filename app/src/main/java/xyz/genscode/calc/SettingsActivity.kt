package xyz.genscode.calc

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import xyz.genscode.calc.data.SettingsHandler
import xyz.genscode.calc.databinding.ActivitySettingsBinding
import xyz.genscode.calc.utils.Vibrator
import xyz.genscode.calc.utils.ui.HoverUtils

class SettingsActivity : AppCompatActivity() {
    lateinit var b : ActivitySettingsBinding
    var settings: SharedPreferences? = null
    var settingsEditor: SharedPreferences.Editor? = null

    var isAboutAppOpened = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(b.root)

        //Загружаем Storage
        settings = getSharedPreferences(SettingsHandler.STORAGE_NAME, Context.MODE_PRIVATE)
        settingsEditor = settings!!.edit()

        //Устанавливаем Hover
        HoverUtils().setHover(b.llThemeDay)
        HoverUtils().setHover(b.llThemeNight)
        HoverUtils().setHover(b.llThemeAuto)
        HoverUtils().setHover(b.llHint)
        HoverUtils().setHover(b.llVibro)
        HoverUtils().setHover(b.llAbout)
        HoverUtils().setHover(b.tvSettingsBack)

        //Назад
        b.tvSettingsBack.setOnClickListener { onBackPressed() }
        b.llAbout.setOnClickListener {
            isAboutAppOpened = true
            b.fcvAbout.visibility = View.VISIBLE
        }

        //Темы
        //Темы
        //Темы
        b.llThemeAuto.setOnClickListener{//Авто
            //Устанавливаем тему для приложения
            SettingsHandler.instance.theme = SettingsHandler.THEME_AUTO

            updateUiTheme()

            //Сохраняем
            settingsEditor!!.putInt(SettingsHandler.THEME, SettingsHandler.instance.theme)
            settingsEditor!!.commit()

            changeTheme(-1)
        }
        b.llThemeDay.setOnClickListener{//День
            SettingsHandler.instance.theme = SettingsHandler.THEME_DAY
            updateUiTheme()

            settingsEditor!!.putInt(SettingsHandler.THEME, SettingsHandler.instance.theme)
            settingsEditor!!.commit()

            changeTheme(-1)
        }
        b.llThemeNight.setOnClickListener{//Ночь
            SettingsHandler.instance.theme = SettingsHandler.THEME_NIGHT
            updateUiTheme()

            settingsEditor!!.putInt(SettingsHandler.THEME, SettingsHandler.instance.theme)
            settingsEditor!!.commit()

            changeTheme(-1)
        }

        //Обучение
        //Обучение
        //Обучение
        b.llHint.setOnClickListener {
            val hint = SettingsHandler.instance.hint
            SettingsHandler.instance.hint = !hint

            settingsEditor!!.putBoolean(SettingsHandler.HINT, SettingsHandler.instance.hint)
            settingsEditor!!.commit()

            updateUiHint()
        }

        //Вибрация
        //Вибрация
        //Вибрация
        b.llVibro.setOnClickListener {
            val vibro = SettingsHandler.instance.vibro
            SettingsHandler.instance.vibro = !vibro

            settingsEditor!!.putBoolean(SettingsHandler.VIBRO, SettingsHandler.instance.vibro)
            settingsEditor!!.commit()

            updateUiVibro()
        }

        updateUiTheme()
        updateUiHint()
        updateUiVibro()
    }

    //Отображаем на экране статус вибро отклика
    fun updateUiVibro(){
        val hint = SettingsHandler.instance.vibro

        if(hint){
            b.tvSettingsVibroStatus.text = resources.getString(R.string.settings_off)
        }else{
            b.tvSettingsVibroStatus.text = resources.getString(R.string.settings_on)
        }

    }

    //Отображаем на экране статус обучения
    fun updateUiHint(){
        val hint = SettingsHandler.instance.hint

        if(hint){
            b.tvSettingsHintStatus.text = resources.getString(R.string.settings_off)
        }else{
            b.tvSettingsHintStatus.text = resources.getString(R.string.settings_hint_clear)
        }

    }

    //Отображаем на экране какая тема выбрана
    fun updateUiTheme(){
        val currentTheme = SettingsHandler.instance.theme

        b.llAutoIndicator.background = ContextCompat.getDrawable(this, R.drawable.turn_off)
        b.llDayIndicator.background = ContextCompat.getDrawable(this, R.drawable.turn_off)
        b.llNightIndicator.background = ContextCompat.getDrawable(this, R.drawable.turn_off)

        when(currentTheme){
            SettingsHandler.THEME_AUTO ->{
                b.llAutoIndicator.background = ContextCompat.getDrawable(this, R.drawable.turn_on)
            }
            SettingsHandler.THEME_DAY ->{
                b.llDayIndicator.background = ContextCompat.getDrawable(this, R.drawable.turn_on)
            }
            SettingsHandler.THEME_NIGHT ->{
                b.llNightIndicator.background = ContextCompat.getDrawable(this, R.drawable.turn_on)
            }
        }

    }

    fun changeTheme(mode : Int){
        var theme : Int
        if(mode == -1){ //Если передаем -1, тема ставится автоматически по настройкам
            theme = SettingsHandler.instance.theme
        }else{
            theme = mode
        }

        when(theme){
            SettingsHandler.THEME_AUTO ->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            SettingsHandler.THEME_DAY ->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            SettingsHandler.THEME_NIGHT ->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    override fun onBackPressed() {
        if(isAboutAppOpened){
            isAboutAppOpened = false
            b.fcvAbout.visibility = View.GONE
            return
        }
        super.onBackPressed()
    }
}
package xyz.genscode.calc.utils.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.MotionEvent
import android.view.View
import xyz.genscode.calc.data.SettingsHandler
import xyz.genscode.calc.databinding.ActivityMainBinding

class HintUtils(var b : ActivityMainBinding) {

    //Обучение представляет из себя сообщение о том, что человек может переключать уровни

    var settings: SharedPreferences? = null
    var settingsEditor: SharedPreferences.Editor? = null
    var enabled = true
    @SuppressLint("ClickableViewAccessibility")
    fun startLevelChangeHint(){
        b.includeHint.root.setOnTouchListener { view,  motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    stopLevelChangeHint()
                    return@setOnTouchListener false
                }
                else -> {return@setOnTouchListener false}
            }
        }
        b.includeHint.root.visibility = View.VISIBLE
        toLevel1()
    }

    fun toLevel1(){
        //Переключаемся на уровень 1
        b.llPopupHeaderContent.animate().x(0f).setDuration(250).start()
        b.includeHint.llTouchHint.animate().translationX(50f).setDuration(250).start()

        android.os.Handler().postDelayed({
            if(enabled) toLevel2()
        },1000)
    }
    fun toLevel2(){
        //Переключаемся на уровень 2
        val toX = -(b.llPopupHeaderContent.width -
                b.tvPopupHeader1.measuredWidth*2)/2

        b.llPopupHeaderContent.animate().x(toX.toFloat()).setDuration(250).start()
        b.includeHint.llTouchHint.animate().translationX(-50f).setDuration(250).start()

        android.os.Handler().postDelayed({
            if(enabled) toLevel1()
        },500)
    }

    fun stopLevelChangeHint(){
        //Заканчиваем обучение
        enabled = false
        b.includeHint.root.visibility = View.GONE
        b.llPopupHeaderContent.animate().x(0f).setDuration(250).start()

        //Загружаем Storage
        settings = b.ll.context.getSharedPreferences(SettingsHandler.STORAGE_NAME, Context.MODE_PRIVATE)
        settingsEditor = settings!!.edit()

        SettingsHandler.instance.hint = false

        //Сохраняем
        settingsEditor!!.putBoolean(
            SettingsHandler.HINT,
            SettingsHandler.instance.hint
        )
        settingsEditor!!.commit()
    }

}
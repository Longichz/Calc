package xyz.genscode.calc

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import xyz.genscode.calc.databinding.ActivityMainBinding
import xyz.genscode.calc.interfaces.OnLevelSelectedListener
import xyz.genscode.calc.utils.ChangeColorUtils
import xyz.genscode.calc.utils.ui.HoverUtils
import xyz.genscode.calc.utils.ui.LevelSelectUtils
import xyz.genscode.calc.utils.ui.LevelUtils

class MainActivity : AppCompatActivity(), OnLevelSelectedListener {
    lateinit var b : ActivityMainBinding
    var changeColorUtils = ChangeColorUtils(this)
    var changeColorUtils2 = ChangeColorUtils(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Фулскрин
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        b = ActivityMainBinding.inflate(layoutInflater)

        setContentView(b.root)

        //Назначаем для ScrollView width (для реализации переключения уровня свайпом)
        b.tvPopupHeader1.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        b.ll.isEnabled = false
        b.ll.layoutParams.width = b.tvPopupHeader1.measuredWidth; b.ll.requestLayout()
        //Назначаем Hover
        HoverUtils().setHover(b.llStart)
        HoverUtils().setHover(b.tvBackMain)

        //Нажатие назад
        b.tvBackMain.setOnClickListener {
            finish()
        }

        //Реализация прокрутки уровней (class OnLevelSelected ниже)
        LevelSelectUtils(this).setOnTouchEvent(b)
    }

    @SuppressLint("SetTextI18n")
    override fun onLevelSelected(level: Int) {
        when(level){
            1 -> { //Первый
                b.tvPopupTime.text = "${LevelUtils().LEVEL1_TIME} ${resources.getString(R.string.sec)}"
                b.tvPopupTasks.text = "${LevelUtils().LEVEL1_TASKS}"
                b.tvPopupDifficult.text = resources.getString(R.string.difficult_easy)

                //Анимированно меняем цвет
                changeColorUtils.setColor(b.tvPopupTime, R.color.easy)
                changeColorUtils.setColor(b.tvPopupTasks, R.color.easy)
                changeColorUtils.setColor(b.tvPopupDifficult, R.color.easy)
                changeColorUtils.setColor(b.tvStart, R.color.easy_secondary)
                changeColorUtils.setBackground(b.llStart, R.drawable.item_easy, 250)
                changeColorUtils2.setBackground(b.llMainBackground, R.drawable.gradient_easy, 1000)

                //Меняем цвет индикаторов
                b.llIndicatorLevel2.background = ContextCompat.getDrawable(this, R.drawable.item_oval)
                b.llIndicatorLevel3.background = ContextCompat.getDrawable(this, R.drawable.item_oval)
                val drawable = ContextCompat.getDrawable(this, R.drawable.item_oval)
                drawable!!.setColorFilter(ContextCompat.getColor(this, R.color.easy), PorterDuff.Mode.SRC_IN)
                b.llIndicatorLevel1.background = drawable
            }
            2 -> { //Второй
                b.tvPopupTime.text = "${LevelUtils().LEVEL2_TIME} ${resources.getString(R.string.sec)}"
                b.tvPopupTasks.text = "${LevelUtils().LEVEL2_TASKS}"
                b.tvPopupDifficult.text = resources.getString(R.string.difficult_medium)

                //Анимированно меняем цвет
                changeColorUtils.setColor(b.tvPopupTime, R.color.medium)
                changeColorUtils.setColor(b.tvPopupTasks, R.color.medium)
                changeColorUtils.setColor(b.tvPopupDifficult, R.color.medium)
                changeColorUtils.setColor(b.tvStart, R.color.medium_secondary)
                changeColorUtils.setBackground(b.llStart, R.drawable.item_medium, 250)
                changeColorUtils2.setBackground(b.llMainBackground, R.drawable.gradient_medium, 1000)

                //Меняем цвет индикаторов
                b.llIndicatorLevel1.background = ContextCompat.getDrawable(this, R.drawable.item_oval)
                b.llIndicatorLevel3.background = ContextCompat.getDrawable(this, R.drawable.item_oval)
                val drawable = ContextCompat.getDrawable(this, R.drawable.item_oval)
                drawable!!.setColorFilter(ContextCompat.getColor(this, R.color.medium), PorterDuff.Mode.SRC_IN)
                b.llIndicatorLevel2.background = drawable
            }
            3 -> { //Третий
                b.tvPopupTime.text = "${LevelUtils().LEVEL3_TIME} ${resources.getString(R.string.sec)}"
                b.tvPopupTasks.text = "${LevelUtils().LEVEL3_TASKS}"
                b.tvPopupDifficult.text = resources.getString(R.string.difficult_hard)

                //Анимированно меняем цвет
                changeColorUtils.setColor(b.tvPopupTime, R.color.hard)
                changeColorUtils.setColor(b.tvPopupTasks, R.color.hard)
                changeColorUtils.setColor(b.tvPopupDifficult, R.color.hard)
                changeColorUtils.setColor(b.tvStart, R.color.hard_secondary)
                changeColorUtils.setBackground(b.llStart, R.drawable.item_hard, 250)
                changeColorUtils2.setBackground(b.llMainBackground, R.drawable.gradient_hard, 1000)

                //Меняем цвет индикаторов
                b.llIndicatorLevel1.background = ContextCompat.getDrawable(this, R.drawable.item_oval)
                b.llIndicatorLevel2.background = ContextCompat.getDrawable(this, R.drawable.item_oval)
                val drawable = ContextCompat.getDrawable(this, R.drawable.item_oval)
                drawable!!.setColorFilter(ContextCompat.getColor(this, R.color.hard), PorterDuff.Mode.SRC_IN)
                b.llIndicatorLevel3.background = drawable
            }
        }
    }


}
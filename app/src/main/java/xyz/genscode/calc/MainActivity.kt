package xyz.genscode.calc

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import xyz.genscode.calc.databinding.ActivityMainBinding
import xyz.genscode.calc.interfaces.OnLevelSelectedListener
import xyz.genscode.calc.utils.ui.HoverUtils
import xyz.genscode.calc.utils.ui.LevelSelectUtils
import xyz.genscode.calc.utils.ui.LevelUtils

class MainActivity : AppCompatActivity(), OnLevelSelectedListener {
    lateinit var b : ActivityMainBinding

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
            }
            2 -> { //Второй
                b.tvPopupTime.text = "${LevelUtils().LEVEL2_TIME} ${resources.getString(R.string.sec)}"
                b.tvPopupTasks.text = "${LevelUtils().LEVEL2_TASKS}"
                b.tvPopupDifficult.text = resources.getString(R.string.difficult_medium)
            }
            3 -> { //Третий
                b.tvPopupTime.text = "${LevelUtils().LEVEL3_TIME} ${resources.getString(R.string.sec)}"
                b.tvPopupTasks.text = "${LevelUtils().LEVEL3_TASKS}"
                b.tvPopupDifficult.text = resources.getString(R.string.difficult_hard)
            }
        }
    }


}
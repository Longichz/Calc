package xyz.genscode.calc

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.genscode.calc.data.LevelHandler
import xyz.genscode.calc.data.SettingsHandler
import xyz.genscode.calc.data.StatsAdapter
import xyz.genscode.calc.data.levels.Controller
import xyz.genscode.calc.databinding.ActivityMainBinding
import xyz.genscode.calc.interfaces.OnLevelSelectedListener
import xyz.genscode.calc.utils.ChangeColorUtils
import xyz.genscode.calc.utils.Vibrator
import xyz.genscode.calc.utils.ui.HintUtils
import xyz.genscode.calc.utils.ui.HoverUtils
import xyz.genscode.calc.utils.ui.KeyboardUtils
import xyz.genscode.calc.utils.ui.LevelSelectUtils

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), OnLevelSelectedListener {
    lateinit var b : ActivityMainBinding
    var changeColorUtils = ChangeColorUtils(this)
    var changeColorUtils2 = ChangeColorUtils(this)
    var controller : Controller? = null
    var type = LevelHandler.TYPE_MULTIPLY
    var difficult = LevelHandler.LEVEL_SIMPLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Фулскрин
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        b = ActivityMainBinding.inflate(layoutInflater)

        setContentView(b.root)

        //Получаем тип заданий
        type = intent.getIntExtra("type", LevelHandler.TYPE_MULTIPLY)
        difficult = intent.getIntExtra("level", LevelHandler.LEVEL_SIMPLE)
        setHeader()

        //Назначаем для ScrollView width (для реализации переключения уровня свайпом)
        b.tvPopupHeader1.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        b.ll.isEnabled = false
        b.ll.layoutParams.width = b.tvPopupHeader1.measuredWidth; b.ll.requestLayout()

        //Назначаем Hover
        val hoverUtils = HoverUtils()
        hoverUtils.setHover(b.llStart)
        hoverUtils.setHover(b.tvBackMain); hoverUtils.setHover(b.llBackPopup)
        hoverUtils.setHover(b.keyBoard0); hoverUtils.setHover(b.keyBoard1); hoverUtils.setHover(b.keyBoard2)
        hoverUtils.setHover(b.keyBoard3); hoverUtils.setHover(b.keyBoard4); hoverUtils.setHover(b.keyBoard5)
        hoverUtils.setHover(b.keyBoard6); hoverUtils.setHover(b.keyBoard7); hoverUtils.setHover(b.keyBoard8)
        hoverUtils.setHover(b.keyBoard9); hoverUtils.setHover(b.keyBoardBack)

        hoverUtils.setHover(b.includeStopTask.llStopTask); hoverUtils.setHover(b.includeStopTask.llCancelStopTask)
        hoverUtils.setHover(b.llStatsShow); hoverUtils.setHover(b.llBackInstancesPopup)

        //Устанавливаем клавитуру
        KeyboardUtils(b).setKeyBoard()

        //Нажатие назад
        b.tvBackMain.setOnClickListener {
            finish()
        }

        //Реализация прокрутки уровней (class OnLevelSelected ниже)
        LevelHandler.instance.currentLevel = 0
        LevelSelectUtils(this).setOnTouchEvent(b)

        //Нажатие на старт
        b.llStart.setOnClickListener {
            controller = Controller(LevelHandler.instance.currentLevel, type, difficult, b)
            controller?.startLevel()
        }

        //Нажатие на кнопки назад
        b.llBackPopup.setOnClickListener {
            b.llTask.visibility = View.GONE; b.llStats.visibility = View.GONE; b.llStatsInstances.visibility = View.GONE
            b.llLevels.visibility = View.VISIBLE; b.tvBackMain.visibility = View.VISIBLE
        }
        b.llBackInstancesPopup.setOnClickListener {
            b.llStatsInstances.visibility = View.GONE; b.llStats.visibility = View.VISIBLE
        }

        //Слушатели нажатий на кнопки окна завершения задания
        b.includeStopTask.llCancelStopTask.setOnClickListener { hideStopTask() }
        b.includeStopTask.llStopTask.setOnClickListener {
            controller?.cancel()
            hideStopTask()
        }

        //Слушатель на статистику
        b.llStatsShow.setOnClickListener {
            b.llTask.visibility = View.GONE
            b.llStats.visibility = View.GONE
            b.llLevels.visibility = View.GONE
            b.llStatsInstances.visibility = View.VISIBLE
            b.tvBackMain.visibility = View.GONE

            val adapter = StatsAdapter(this, LevelHandler.instance.tasks)
            val linearLayoutManager = LinearLayoutManager(this)
            b.rvStats.layoutManager = linearLayoutManager
            b.rvStats.adapter = adapter
        }

        //Если обучение ни разу не было пройдено, запускаем его
        if(SettingsHandler.instance.hint){
            HintUtils(b).startLevelChangeHint()
        }
    }

    fun setHeader(){
        when(type){
            LevelHandler.TYPE_SUM -> b.tvMainHeader.text = resources.getString(R.string.sum)
            LevelHandler.TYPE_DIV -> b.tvMainHeader.text = resources.getString(R.string.div)
            LevelHandler.TYPE_DIF -> b.tvMainHeader.text = resources.getString(R.string.dif)
            LevelHandler.TYPE_MULTIPLY -> b.tvMainHeader.text = resources.getString(R.string.multiply)
            LevelHandler.TYPE_SQUARED -> b.tvMainHeader.text = resources.getString(R.string.squared)
            LevelHandler.TYPE_RANDOM -> b.tvMainHeader.text = resources.getString(R.string.random)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onLevelSelected(level: Int) {
        when(level){
            1 -> { //Первый
                if (LevelHandler.instance.currentLevel == 1) return

                b.tvPopupTime.text = "${LevelHandler.getTime(type, 1, difficult)} ${resources.getString(R.string.sec)}"
                b.tvPopupTasks.text = "${LevelHandler.getTasks(type, 1)}"
                b.tvPopupDifficult.text = resources.getString(R.string.difficult_easy)

                if(type == LevelHandler.TYPE_RANDOM) b.tvPopupTime.text = resources.getString(R.string.undefined)

                //Анимированно меняем цвет
                changeColorUtils.setColor(b.tvPopupTime, R.color.easy)
                changeColorUtils.setColor(b.tvPopupTasks, R.color.easy)
                changeColorUtils.setColor(b.tvPopupDifficult, R.color.easy)
                changeColorUtils.setColor(b.tvStart, R.color.easy_secondary)
                changeColorUtils.setBackground(b.llStart, R.drawable.item_easy, 250)
                changeColorUtils2.setBackground(b.llMainBackground, R.drawable.gradient_easy, 500)

                b.tvPopupStatsTask.setTextColor(ContextCompat.getColor(this, R.color.easy))
                b.tvPopupStatsTime.setTextColor(ContextCompat.getColor(this, R.color.easy))
                b.tvBackPopup.setTextColor(ContextCompat.getColor(this, R.color.easy_secondary))
                b.tvBackInstancesPopup.setTextColor(ContextCompat.getColor(this, R.color.easy_secondary))
                b.tvStatsShow.setTextColor(ContextCompat.getColor(this, R.color.easy_secondary))
                b.llBackPopup.background = ContextCompat.getDrawable(this, R.drawable.item_easy)
                b.llStatsShow.background = ContextCompat.getDrawable(this, R.drawable.item_easy)
                b.llBackInstancesPopup.background = ContextCompat.getDrawable(this, R.drawable.item_easy)

                //Меняем цвет индикаторов
                b.llIndicatorLevel2.background = ContextCompat.getDrawable(this, R.drawable.item_oval)
                b.llIndicatorLevel3.background = ContextCompat.getDrawable(this, R.drawable.item_oval)
                val drawable = ContextCompat.getDrawable(this, R.drawable.item_oval)
                drawable!!.setColorFilter(ContextCompat.getColor(this, R.color.easy), PorterDuff.Mode.SRC_IN)
                b.llIndicatorLevel1.background = drawable

                LevelHandler.instance.currentLevel = 1
                Vibrator(this).vibrate(15)
            }
            2 -> { //Второй
                if (LevelHandler.instance.currentLevel == 2) return

                b.tvPopupTime.text = "${LevelHandler.getTime(type, 2, difficult)} ${resources.getString(R.string.sec)}"
                b.tvPopupTasks.text = "${LevelHandler.getTasks(type, 2)}"
                b.tvPopupDifficult.text = resources.getString(R.string.difficult_medium)

                if(type == LevelHandler.TYPE_RANDOM) b.tvPopupTime.text = resources.getString(R.string.undefined)

                //Анимированно меняем цвет
                changeColorUtils.setColor(b.tvPopupTime, R.color.medium)
                changeColorUtils.setColor(b.tvPopupTasks, R.color.medium)
                changeColorUtils.setColor(b.tvPopupDifficult, R.color.medium)
                changeColorUtils.setColor(b.tvStart, R.color.medium_secondary)
                changeColorUtils.setBackground(b.llStart, R.drawable.item_medium, 250)
                changeColorUtils2.setBackground(b.llMainBackground, R.drawable.gradient_medium, 500)

                b.tvPopupStatsTask.setTextColor(ContextCompat.getColor(this, R.color.medium))
                b.tvPopupStatsTime.setTextColor(ContextCompat.getColor(this, R.color.medium))
                b.tvBackPopup.setTextColor(ContextCompat.getColor(this, R.color.medium_secondary))
                b.tvBackInstancesPopup.setTextColor(ContextCompat.getColor(this, R.color.medium_secondary))
                b.tvStatsShow.setTextColor(ContextCompat.getColor(this, R.color.medium_secondary))
                b.llBackPopup.background = ContextCompat.getDrawable(this, R.drawable.item_medium)
                b.llStatsShow.background = ContextCompat.getDrawable(this, R.drawable.item_medium)
                b.llBackInstancesPopup.background = ContextCompat.getDrawable(this, R.drawable.item_medium)

                //Меняем цвет индикаторов
                b.llIndicatorLevel1.background = ContextCompat.getDrawable(this, R.drawable.item_oval)
                b.llIndicatorLevel3.background = ContextCompat.getDrawable(this, R.drawable.item_oval)
                val drawable = ContextCompat.getDrawable(this, R.drawable.item_oval)
                drawable!!.setColorFilter(ContextCompat.getColor(this, R.color.medium), PorterDuff.Mode.SRC_IN)
                b.llIndicatorLevel2.background = drawable

                LevelHandler.instance.currentLevel = 2
                Vibrator(this).vibrate(15)
            }
            3 -> { //Третий
                if (LevelHandler.instance.currentLevel == 3) return

                b.tvPopupTime.text = "${LevelHandler.getTime(type, 3, difficult)} ${resources.getString(R.string.sec)}"
                b.tvPopupTasks.text = "${LevelHandler.getTasks(type, 3)}"
                b.tvPopupDifficult.text = resources.getString(R.string.difficult_hard)

                if(type == LevelHandler.TYPE_RANDOM) b.tvPopupTime.text = resources.getString(R.string.undefined)

                //Анимированно меняем цвет
                changeColorUtils.setColor(b.tvPopupTime, R.color.hard)
                changeColorUtils.setColor(b.tvPopupTasks, R.color.hard)
                changeColorUtils.setColor(b.tvPopupDifficult, R.color.hard)
                changeColorUtils.setColor(b.tvStart, R.color.hard_secondary)
                changeColorUtils.setBackground(b.llStart, R.drawable.item_hard, 250)
                changeColorUtils2.setBackground(b.llMainBackground, R.drawable.gradient_hard, 500)

                b.tvPopupStatsTask.setTextColor(ContextCompat.getColor(this, R.color.hard))
                b.tvPopupStatsTime.setTextColor(ContextCompat.getColor(this, R.color.hard))
                b.tvBackPopup.setTextColor(ContextCompat.getColor(this, R.color.hard_secondary))
                b.tvBackInstancesPopup.setTextColor(ContextCompat.getColor(this, R.color.hard_secondary))
                b.tvStatsShow.setTextColor(ContextCompat.getColor(this, R.color.hard_secondary))
                b.llBackPopup.background = ContextCompat.getDrawable(this, R.drawable.item_hard)
                b.llStatsShow.background = ContextCompat.getDrawable(this, R.drawable.item_hard)
                b.llBackInstancesPopup.background = ContextCompat.getDrawable(this, R.drawable.item_hard)

                //Меняем цвет индикаторов
                b.llIndicatorLevel1.background = ContextCompat.getDrawable(this, R.drawable.item_oval)
                b.llIndicatorLevel2.background = ContextCompat.getDrawable(this, R.drawable.item_oval)
                val drawable = ContextCompat.getDrawable(this, R.drawable.item_oval)
                drawable!!.setColorFilter(ContextCompat.getColor(this, R.color.hard), PorterDuff.Mode.SRC_IN)
                b.llIndicatorLevel3.background = drawable

                LevelHandler.instance.currentLevel = 3
                Vibrator(this).vibrate(15)
            }
        }
    }

    fun showStopTask(){
        b.includeStopTask.root.visibility = View.VISIBLE
        b.includeStopTask.llStopTaskBackground.alpha = 0f
        b.includeStopTask.llStopTaskBackground.scaleX = 0.9f
        b.includeStopTask.llStopTaskBackground.animate().alpha(1f).scaleX(1.05f).scaleY(1.05f).setDuration(150).start()
        Handler().postDelayed({
            b.includeStopTask.llStopTaskBackground.alpha = 1f
            b.includeStopTask.llStopTaskBackground.scaleX = 1.05f
            b.includeStopTask.llStopTaskBackground.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
        },150)
    }
    fun hideStopTask(){
        b.includeStopTask.llStopTaskBackground.animate().scaleX(1.05f).scaleY(1.05f).setDuration(100).start()
        Handler().postDelayed({
            b.includeStopTask.llStopTaskBackground.animate().alpha(0f).scaleX(0.9f).scaleY(0.9f).setDuration(250).start()
            b.includeStopTask.root.visibility = View.GONE
        },100)
    }

    override fun onBackPressed() {
        //Проверяем открыто ли stopTask окно
        if(b.includeStopTask.root.visibility == View.VISIBLE){
            hideStopTask() //закрываем
            return
        }

        //Проверяем запущен ли уровень
        if(controller?.id != 0 && controller?.id != -1 &&
            controller?.id != controller?.tasks?.plus(1)){
            showStopTask() //открываем stopTask окно
            return
        }

        super.onBackPressed()
    }

}
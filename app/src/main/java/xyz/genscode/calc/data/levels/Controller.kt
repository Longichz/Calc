package xyz.genscode.calc.data.levels

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import xyz.genscode.calc.R
import xyz.genscode.calc.data.LevelHandler
import xyz.genscode.calc.databinding.ActivityMainBinding
import xyz.genscode.calc.models.Task
import xyz.genscode.calc.utils.ChangeColorUtils
import xyz.genscode.calc.utils.DrawTaskUitls

class Controller(private val level : Int, private val type : Int, private val difficult : Int, var b : ActivityMainBinding) {
    private val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
    private lateinit var tvListener: TextWatcher

    private var timeStats = 0L //время начала решения заданий (для статистики)

    var time = 0 //время на каждое задание
    var tasks = 0 //количество заданий

    private var correctAnswer = -1

    var id = 0 //id задачи на данный момент

    lateinit var task : Task

    fun startLevel(){
        id = 1

        LevelHandler.instance.tasks.clear()

        //Показываем для popup меню примеров
        b.llLevels.visibility = View.GONE
        b.tvBackMain.visibility = View.GONE
        b.llTask.visibility = View.VISIBLE

        //Определяем какой уровень выбран и получаем для него настройки
        if (type != LevelHandler.TYPE_RANDOM) time = LevelHandler.getTime(type, level, difficult)
        tasks = LevelHandler.getTasks(type, level)

        //Слушатель ответа для досрочного ответа
        tvListener = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Получаем ответ пользователя из текста
                var answer = 0
                try{ answer = Integer.parseInt(b.tvPopupAnswer.text.toString()) }catch (_: java.lang.Exception){ }

                if(b.tvPopupAnswer.length() != 0) {
                    if (answer == correctAnswer) {
                        createTask(id)
                    }
                }
            }
        }
        b.tvPopupAnswer.addTextChangedListener(tvListener)

        //Засекаем время
        timeStats = System.currentTimeMillis()

        //Генерируем первую задачу
        startTask(id)
    }

    @SuppressLint("SetTextI18n")
    fun startTask(id : Int) {
        if (this.id < 0) return

        //СТАТИСТИКА
        //Если id задачи превышает количество задач в уровне, заканчиваем
        if (id > tasks) {
            //Открываем вкладку статистики
            b.llTask.visibility = View.GONE
            b.llStats.visibility = View.VISIBLE
            setHeader()

            //Подсчитываем количество правильных ответов
            var correct = 0
            for (i in LevelHandler.instance.tasks.indices){
                val task = LevelHandler.instance.tasks[i]
                if(task.answer == task.correctAnswer){
                    correct++
                }
            }
            b.tvPopupStatsTask.text = "$correct/$tasks"

            //Подсчитываем время
            val timeNow = System.currentTimeMillis()
            val timeFormatted = timeNow - timeStats
            b.tvPopupStatsTime.text = "${(timeFormatted/1000).toInt()} ${b.ll.context.resources.getString(
                R.string.sec)}"

            //Удаляем слушатель изменения текста
            b.tvPopupAnswer.removeTextChangedListener(tvListener)

            this.id = -1
            this.tasks = 0

            return
        }

        b.tvPopupAnswer.text = "" //очищаем edit с ответом

        //Генерируем задачу, в зависимости от сложности
        when(difficult){
           LevelHandler.LEVEL_SIMPLE -> task = SimpleLevels().generateTask(type, level)
           LevelHandler.LEVEL_MEDIUM -> task = MediumLevels().generateTask(type, level)
        }

        correctAnswer = task.correctAnswer

        //Определяем время на выполнение задание
        time = LevelHandler.getTime(task.type, level, difficult)

        //Рисуем пример
        b.tvMainHeader.text = DrawTaskUitls().taskToStr(task, false)

        //Таймер, указывающий когда произойдет принудительная проверка на правльный ответ
        @Suppress("DEPRECATION")
        Handler().postDelayed({
            createTask(id)
        }, (time*1000).toLong())

        //Анимация для прогресс бара
        valueAnimator.duration = (time * 1000).toLong()
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            b.llProgress.scaleX = value // обновляем значение scaleX у вью
        }
        valueAnimator.start()

        //Показываем прогресс бар
        b.llProgress.background = ContextCompat.getDrawable(b.llProgress.context, R.drawable.gradient_progress)
        b.llProgress.visibility = View.VISIBLE
    }

    //Сохраняем задачу с ответом пользователя для дальнейшего анализа в статистике
    @Suppress("DEPRECATION")
    fun createTask(id : Int){
        //Если человек ответил досрочно и id задачи уже был добавлен в список, пропускаем
        if (LevelHandler.instance.tasks.size >= id || this.id < 0) return

        //Получаем ответ пользователя из текста
        var answer = 0
        try{ answer = Integer.parseInt(b.tvPopupAnswer.text.toString()) }catch (_: java.lang.Exception){ }
        task.answer = answer

        //Добавляем задачу
        LevelHandler.instance.tasks.add(task)

        //Проверяем правильность ответа
        if (answer == correctAnswer){
            showCorrect()
        }else{
            showError()
        }

        //Закрываем окно завершения задания если оно открыто
        if(id == tasks){
            b.includeStopTask.llStopTaskBackground.animate().scaleX(1.05f).scaleY(1.05f).setDuration(100).start()
            Handler().postDelayed({
                b.includeStopTask.llStopTaskBackground.animate().alpha(0f).scaleX(0.9f).scaleY(0.9f).setDuration(250).start()
                b.includeStopTask.root.visibility = View.GONE
            },100)
        }
    }

    //Правильно
    @Suppress("DEPRECATION")
    fun showCorrect(){
        valueAnimator.cancel()
        b.llProgress.animate().scaleX(1f).start()
        ChangeColorUtils(b.llProgress.context).setBackground(b.llProgress, R.drawable.gradient_progress_correct, 250)
        Handler().postDelayed({
            b.llProgress.animate().scaleX(0.0f).setDuration(250).start()
            Handler().postDelayed({
                this.id += 1
                startTask(this.id) //Запускаем след. задачу
            }, 250)
        }, 1250)
    }

    //Неверно
    @Suppress("DEPRECATION")
    fun showError(){
        valueAnimator.cancel()
        b.llProgress.animate().scaleX(1f).start()
        ChangeColorUtils(b.llProgress.context).setBackground(b.llProgress, R.drawable.gradient_progress_error, 250)
        Handler().postDelayed({
            b.llProgress.animate().scaleX(0.0f).setDuration(250).start()
            Handler().postDelayed({
                this.id += 1
                startTask(this.id) //Запускаем след. задачу
            }, 250)
        }, 1250)
    }

    fun cancel(){
        tasks = id
        createTask(id)
    }

    private fun setHeader(){
        when(type){
            LevelHandler.TYPE_SUM -> b.tvMainHeader.text = b.ll.context.resources.getString(R.string.sum)
            LevelHandler.TYPE_DIV -> b.tvMainHeader.text = b.ll.context.resources.getString(R.string.div)
            LevelHandler.TYPE_DIF -> b.tvMainHeader.text = b.ll.context.resources.getString(R.string.dif)
            LevelHandler.TYPE_MULTIPLY -> b.tvMainHeader.text = b.ll.context.resources.getString(R.string.multiply)
            LevelHandler.TYPE_SQUARED -> b.tvMainHeader.text = b.ll.context.resources.getString(R.string.squared)
            LevelHandler.TYPE_RANDOM -> b.tvMainHeader.text = b.ll.context.getString(R.string.random)
        }
    }
}
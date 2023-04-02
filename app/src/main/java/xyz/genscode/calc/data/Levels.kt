package xyz.genscode.calc.data

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import xyz.genscode.calc.R
import xyz.genscode.calc.databinding.ActivityMainBinding
import xyz.genscode.calc.models.Task
import xyz.genscode.calc.utils.ChangeColorUtils
import xyz.genscode.calc.utils.Vibrator

class Levels(val level : Int, val type : Int, var b : ActivityMainBinding) {

    private val valueAnimator = ValueAnimator.ofFloat(0f, 1f)

    private var timeStats = 0L //время начала решения заданий (для статистики)

    private var time = 0 //время на каждое задание
    private var tasks = 0 //количество заданий

    private var _a = 0
    private var _b = 0
    private var correctAnswer = -1

    var id = 1 //id задачи на данный момент

    lateinit var tvListener: TextWatcher

    fun startLevel(){
        id = 1

        //Показываем для popup меню примеров
        b.llLevels.visibility = View.GONE
        b.tvBackMain.visibility = View.GONE
        b.llTask.visibility = View.VISIBLE

        //Определяем какой уровень выбран и получаем для него настройки
        when(level){
            1 -> {
                time = LevelHandler.LEVEL1_TIME
                tasks = LevelHandler.LEVEL1_TASKS
            }
            2 -> {
                time = LevelHandler.LEVEL2_TIME
                tasks = LevelHandler.LEVEL2_TASKS
            }
            3 -> {
                time = LevelHandler.LEVEL3_TIME
                tasks = LevelHandler.LEVEL3_TASKS
            }
        }

        //Слушатель ответа для досрочного ответа
        tvListener = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Получаем ответ пользователя из текста
                var answer = 0
                try{ answer = Integer.parseInt(b.tvPopupAnswer.text.toString()) }catch (e : java.lang.Exception){ }

                if (answer == correctAnswer){
                    createTask(id)
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
    fun startTask(id : Int){

        //СТАТИСТИКА
        //Если id задачи превышает количество задач в уровне, заканчиваем
        if (id > tasks){

            //Открываем вкладку статистики
            b.llTask.visibility = View.GONE
            b.llStats.visibility = View.VISIBLE
            b.tvMainHeader.text = b.tvMainHeader.context.resources.getString(R.string.multiply)

            //Подсчитываем количество правильных ответов
            var correct = 0;
            for (i in LevelHandler.instance.tasks.indices){
                val task = LevelHandler.instance.tasks.get(i)
                if(task.answer == task.correctAnswer){
                    correct++
                }
            }
            b.tvPopupStatsTask.text = "$correct/$tasks"

            //Подсчитываем время
            val timeNow = System.currentTimeMillis()
            val timeFormatted = timeNow - timeStats
            b.tvPopupStatsTime.text = "$timeFormatted ${b.ll.context.resources.getString(R.string.sec)}"

            //Удаляем слушатель изменения текста
            b.tvPopupAnswer.removeTextChangedListener(tvListener)

            return
        }

        b.tvPopupAnswer.text = ""

        //Создаем a и b, и вычисляем правильный ответ
        _a = kotlin.random.Random.nextInt(1, 9)
        _b = kotlin.random.Random.nextInt(1, 9)
        correctAnswer = _a * _b

        //Отображаем пример
        b.tvMainHeader.text = "$_a * $_b"

        //Таймер, указывающий когда произойдет принудительная проверка на правльный ответ
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
    fun createTask(id : Int){

        //Если человек ответил досрочно и id задачи уже был добавлен в список, пропускаем
        if (LevelHandler.instance.tasks.size >= id) {
            return}

        //Получаем ответ пользователя из текста
        var answer = 0
        try{ answer = Integer.parseInt(b.tvPopupAnswer.text.toString()) }catch (e : java.lang.Exception){ }

        //Добавляем задачу
        LevelHandler.instance.tasks.add(Task(_a, _b, answer, correctAnswer, type))

        //Проверяем правильность ответа
        if (answer == correctAnswer){
            showCorrect()
        }else{
            showError()
        }
    }

    //Правильно
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

}
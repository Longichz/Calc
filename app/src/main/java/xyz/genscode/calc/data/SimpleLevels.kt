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
import java.util.logging.Level

//SimpleLevels это простые уровни, генерирующие примеры только с двумя числами (+, -, *)
//Например 2*5, 2-6 и т.п
class SimpleLevels(val level : Int, val type : Int, var b : ActivityMainBinding) {

    private val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
    private lateinit var tvListener: TextWatcher

    private var timeStats = 0L //время начала решения заданий (для статистики)

    var time = 0 //время на каждое задание
    var tasks = 0 //количество заданий

    private var _a = 0
    private var _b = 0
    private var correctAnswer = -1

    var id = 0 //id задачи на данный момент

    fun startLevel(){
        id = 1

        //Показываем для popup меню примеров
        b.llLevels.visibility = View.GONE
        b.tvBackMain.visibility = View.GONE
        b.llTask.visibility = View.VISIBLE

        //Определяем какой уровень выбран и получаем для него настройки
        time = LevelHandler.getTime(type, level)
        tasks = LevelHandler.getTasks(type, level)

        //Слушатель ответа для досрочного ответа
        tvListener = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Получаем ответ пользователя из текста
                var answer = 0
                try{ answer = Integer.parseInt(b.tvPopupAnswer.text.toString()) }catch (e : java.lang.Exception){ }

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
    fun startTask(id : Int){
        if (this.id < 0) return

        //СТАТИСТИКА
        //Если id задачи превышает количество задач в уровне, заканчиваем
        if (id > tasks){

            //Открываем вкладку статистики
            b.llTask.visibility = View.GONE
            b.llStats.visibility = View.VISIBLE
            setHeader()

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
            b.tvPopupStatsTime.text = "${(timeFormatted/1000).toInt()} ${b.ll.context.resources.getString(R.string.sec)}"

            //Удаляем слушатель изменения текста
            b.tvPopupAnswer.removeTextChangedListener(tvListener)

            this.id = -1

            LevelHandler.instance.tasks.clear()

            return
        }

        b.tvPopupAnswer.text = ""

        //Область значений для примеров на умножения
        var from = 1
        var until = 50

        when(type){
            //ОЗ для умножения
            LevelHandler.TYPE_MULTIPLY -> {
                when (level) {
                    1 -> {
                        from = 1
                        until = 9
                    }
                    2 -> {
                        from = 2
                        until = 9
                    }
                    3 -> {
                        from = -9
                        until = 9
                    }
                }
            }

            //ОЗ для сложения и вычитания
            else -> {
                when (level) {
                    1 -> {
                        from = 0
                        until = 9
                    }
                    2 -> {
                        from = 4
                        until = 35
                    }
                    3 -> {
                        from = -50
                        until = 50
                    }
                }
            }
        }

        //Создаем a и b, и вычисляем правильный ответ
        _a = kotlin.random.Random.nextInt(from, until)
        _b = kotlin.random.Random.nextInt(from, until)

        //Для первого и второго уровня исключаем минусовые ответы и сложные ситуации (5 + -2)
        if(level <= 2 && _a < 0) _a = -_a //убираем минусовое значение у _a
        if(level <= 2 && _b < 0) _b = -_b //убираем минусовое значение у _b
        if(level <= 2 && type != LevelHandler.TYPE_MULTIPLY) if(_b > _a) {val temp = _b; _b = _a; _a = temp} //устанавливаем, что a - всегда максимальное

        when(type){
            LevelHandler.TYPE_MULTIPLY ->{
                correctAnswer = _a * _b
            }
            LevelHandler.TYPE_SUM ->{
                correctAnswer = _a + _b
            }
            LevelHandler.TYPE_DIF ->{
                correctAnswer = _a - _b
            }
        }

        //отображаем пример
        var str_a = "$_a"; var str_b = "$_b";
        if(_a < 0) str_a = "($_a)";
        if(_b < 0) str_b = "($_b)"
        b.tvMainHeader.text = "$str_a ${LevelHandler.getChar(type)} $str_b"

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
        if (LevelHandler.instance.tasks.size >= id || this.id < 0) return

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

    fun cancel(){
        tasks = id
        createTask(id)
    }

    fun setHeader(){
        when(type){
            LevelHandler.TYPE_SUM -> b.tvMainHeader.text = b.ll.context.resources.getString(R.string.sum)
            LevelHandler.TYPE_DIF -> b.tvMainHeader.text = b.ll.context.resources.getString(R.string.dif)
            LevelHandler.TYPE_MULTIPLY -> b.tvMainHeader.text = b.ll.context.resources.getString(R.string.multiply)
        }
    }

}
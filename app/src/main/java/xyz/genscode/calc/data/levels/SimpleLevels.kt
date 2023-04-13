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
import kotlin.math.pow

//SimpleLevels это простые уровни, генерирующие примеры только с двумя числами (+, -, *)
//Например 2*5, 2-6 и т.п
class SimpleLevels(val level : Int, val type : Int, var b : ActivityMainBinding) {

    private val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
    private lateinit var tvListener: TextWatcher

    private var timeStats = 0L //время начала решения заданий (для статистики)

    var time = 0 //время на каждое задание
    var tasks = 0 //количество заданий
    var _type = 0 //_type добавляет конкретный тип задания, потому что type класса может равняться рандому

    private var _a = 0
    private var _b = 0
    private var correctAnswer = -1

    var id = 0 //id задачи на данный момент

    fun startLevel(){
        id = 1

        LevelHandler.instance.tasks.clear()

        //Показываем для popup меню примеров
        b.llLevels.visibility = View.GONE
        b.tvBackMain.visibility = View.GONE
        b.llTask.visibility = View.VISIBLE

        //Определяем какой уровень выбран и получаем для него настройки
        if(type != LevelHandler.TYPE_RANDOM) time = LevelHandler.getTime(type, level)
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

            return
        }

        b.tvPopupAnswer.text = ""

        //Определяем тип задания
        _type = type
        if(_type == LevelHandler.TYPE_RANDOM) _type = kotlin.random.Random.nextInt(0, 3)

        time = LevelHandler.getTime(_type, level)

        //Область значений для примеров на умножения
        var from = 1
        var until = 50

        when(_type){
            //ОЗ для умножения
            LevelHandler.TYPE_MULTIPLY, LevelHandler.TYPE_DIV -> {
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

            //ОЗ для квадрата и куба
            LevelHandler.TYPE_SQUARED, LevelHandler.TYPE_CUBED -> {
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
                        from = -100
                        until = 100
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

        //Дополнительная настройка для квадрата и куба
        if(_type == LevelHandler.TYPE_SQUARED) _b = 2
        if(_type == LevelHandler.TYPE_CUBED) _b = 3

        //Дополнительная настройка для деления
        if(_type == LevelHandler.TYPE_DIV && _b == 0) _b = 1
        if(_type == LevelHandler.TYPE_DIV) _a *= _b

        //Для первого и второго уровня исключаем минусовые ответы и сложные ситуации (5 + -2)
        if(level <= 2 && _a < 0) _a = -_a //убираем минусовое значение у _a
        if(level <= 2 && _b < 0) _b = -_b //убираем минусовое значение у _b
        if(level <= 2
            && (_type != LevelHandler.TYPE_MULTIPLY)
            && (_type != LevelHandler.TYPE_SQUARED)
            && (_type != LevelHandler.TYPE_CUBED)
        ) if(_b > _a) {val temp = _b; _b = _a; _a = temp} //устанавливаем, что a - всегда максимальное

        when(_type){
            LevelHandler.TYPE_MULTIPLY ->{
                correctAnswer = _a * _b
            }
            LevelHandler.TYPE_DIV ->{
                correctAnswer = _a / _b
            }
            LevelHandler.TYPE_SUM ->{
                correctAnswer = _a + _b
            }
            LevelHandler.TYPE_DIF ->{
                correctAnswer = _a - _b
            }
            LevelHandler.TYPE_SQUARED, LevelHandler.TYPE_CUBED ->{
                correctAnswer = _a.toDouble().pow(_b.toDouble()).toInt()
            }
        }

        //отображаем пример
        var str_a = "$_a"; var str_b = "$_b";
        if(_a < 0) str_a = "($_a)";
        if(_b < 0) str_b = "($_b)"
        if(_type == LevelHandler.TYPE_SQUARED) str_b = "²" //настройка для квадрата чисел
        if(_type == LevelHandler.TYPE_CUBED) str_b = "³" //настройка для куба чисел
        b.tvMainHeader.text = "$str_a${LevelHandler.getChar(_type)}$str_b"

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
        LevelHandler.instance.tasks.add(Task(_a, _b, answer, correctAnswer, _type))

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
            LevelHandler.TYPE_DIV -> b.tvMainHeader.text = b.ll.context.resources.getString(R.string.div)
            LevelHandler.TYPE_DIF -> b.tvMainHeader.text = b.ll.context.resources.getString(R.string.dif)
            LevelHandler.TYPE_MULTIPLY -> b.tvMainHeader.text = b.ll.context.resources.getString(R.string.multiply)
            LevelHandler.TYPE_SQUARED -> b.tvMainHeader.text = b.ll.context.resources.getString(R.string.squared)
            LevelHandler.TYPE_CUBED -> b.tvMainHeader.text = b.ll.context.resources.getString(R.string.cubed)
            LevelHandler.TYPE_RANDOM -> b.tvMainHeader.text = b.ll.context.getString(R.string.random)
        }
    }

}
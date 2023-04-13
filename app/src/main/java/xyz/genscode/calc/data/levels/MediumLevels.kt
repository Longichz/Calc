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

//MediumLevels это уровни средней сложности, генерирующие примеры с двумя действиями (+, -, *, /)
//Например (2*5) * 2, (13-5) / (5+2)
class MediumLevels(val level : Int, val type : Int, var b : ActivityMainBinding) {
    private val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
    private lateinit var tvListener: TextWatcher

    private var timeStats = 0L //время начала решения заданий (для статистики)

    var time = 0 //время на каждое задание
    var tasks = 0 //количество заданий
    var _type = 0 //_type добавляет конкретный тип задания, потому что type класса может равняться рандому

    private var _a = 0
    private var _b = 0
    private var _c = 0
    private var _d = 0
    private var firstPartAnswer = -1
    private var secondPartAnswer = -1
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
            b.tvPopupStatsTime.text = "${(timeFormatted/1000).toInt()} ${b.ll.context.resources.getString(
                R.string.sec)}"

            //Удаляем слушатель изменения текста
            b.tvPopupAnswer.removeTextChangedListener(tvListener)

            this.id = -1

            return
        }

        b.tvPopupAnswer.text = ""

        //Определяем тип действия в скобках
        _type = kotlin.random.Random.nextInt(1, 3)

        time = LevelHandler.getTime(_type, level)

        //Область значений для примеров на умножения
        when(type) {
            LevelHandler.TYPE_MULTIPLY -> {
                //создаем значения в скобках которые не превысят в конечном итоге 9
                if (_type == LevelHandler.TYPE_DIF) { //действие в скобках -
                    _a = kotlin.random.Random.nextInt(0, 25)
                    _b = kotlin.random.Random.nextInt(_a - 9, _a)
                    _c = kotlin.random.Random.nextInt(0, 25)
                    _d = kotlin.random.Random.nextInt(_c - 9, _c)
                    firstPartAnswer = _a - _b
                    secondPartAnswer = _c - _d
                } else if (_type == LevelHandler.TYPE_SUM) { //действие в скобках +
                    _a = kotlin.random.Random.nextInt(0, 9)
                    _b = kotlin.random.Random.nextInt(0, 9 - _a)
                    _c = kotlin.random.Random.nextInt(0, 9)
                    _d = kotlin.random.Random.nextInt(0,  9 - _a)
                    firstPartAnswer = _a + _b
                    secondPartAnswer = _c + _d
                }
            }
        }

        when(type){
            LevelHandler.TYPE_MULTIPLY -> {
                correctAnswer = firstPartAnswer * secondPartAnswer
            }
        }

        //отображаем пример
        var str_a = "$_a"; var str_b = "$_b"; var str_c = "$_c"; var str_d = "$_d"; //создаем строку для каждого члена
        //если число в минусе, отображаем в скобках как (-9)
        if(_a < 0) str_a = "($_a)"; if(_b < 0) str_b = "($_b)"; if(_c < 0) str_c = "($_c)"; if(_d < 0) str_d = "($_d)"
        b.tvMainHeader.text=
            "($str_a${LevelHandler.getChar(_type)}$str_b) ${LevelHandler.getChar(type)} ($str_c${LevelHandler.getChar(_type)}$str_d)"

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
        LevelHandler.instance.tasks.add(Task(_a, _b, _c, _d, answer, correctAnswer, type, _type))

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
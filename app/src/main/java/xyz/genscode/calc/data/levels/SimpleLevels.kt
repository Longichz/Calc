package xyz.genscode.calc.data.levels

import xyz.genscode.calc.data.LevelHandler
import xyz.genscode.calc.models.Task
import kotlin.math.pow

//SimpleLevels это простые уровни, генерирующие примеры только с двумя числами (+, -, *)
//Например 2*5, 2-6 и т.п
class SimpleLevels {
    private var _type = -1

    fun generateTask(type : Int, level : Int) : Task{
        //Определяем тип задания
        _type = type
        if(_type == LevelHandler.TYPE_RANDOM) _type = kotlin.random.Random.nextInt(0, 4)

        //Область значений
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

            //ОЗ для квадрата
            LevelHandler.TYPE_SQUARED -> {
                when (level) {
                    1 -> {
                        from = 1
                        until = 9
                    }
                    2, 3 -> {
                        from = 2
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
        var _a = kotlin.random.Random.nextInt(from, until)
        var _b = kotlin.random.Random.nextInt(from, until)

        //Дополнительная настройка для квадрата
        if(_type == LevelHandler.TYPE_SQUARED) _b = 2

        //Дополнительная настройка для деления
        if(_type == LevelHandler.TYPE_DIV && _b == 0) _b = 1
        if(_type == LevelHandler.TYPE_DIV) _a *= _b

        //Для первого и второго уровня исключаем минусовые ответы и сложные ситуации (5 + -2)
        if(level <= 2 && _a < 0) _a = -_a //убираем минусовое значение у _a
        if(level <= 2 && _b < 0) _b = -_b //убираем минусовое значение у _b
        if(level <= 2
            && (_type != LevelHandler.TYPE_MULTIPLY)
            && (_type != LevelHandler.TYPE_SQUARED)
            && (_type != LevelHandler.TYPE_SUM)
        ) if(_b > _a) {val temp = _b; _b = _a; _a = temp} //устанавливаем, что a - всегда максимальное

        var correctAnswer = 0
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
            LevelHandler.TYPE_SQUARED ->{
                correctAnswer = _a.toDouble().pow(_b.toDouble()).toInt()
            }
        }

        return Task(_a, _b, 0, correctAnswer, _type)
    }

}
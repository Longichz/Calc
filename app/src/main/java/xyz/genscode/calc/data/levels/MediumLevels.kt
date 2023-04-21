package xyz.genscode.calc.data.levels

import xyz.genscode.calc.data.LevelHandler
import xyz.genscode.calc.models.Task

//MediumLevels это уровни средней сложности, генерирующие примеры с двумя действиями (+, -, *, /)
//Например (2*5) * 2, (13-5) / (5+2)
class MediumLevels{
    var typeParts = 0 //typeParts это тип действия в скобках, например в (4+1) * (5+4), type = умножение, а typeParts = сумма

    private var _a = 0
    private var _b = 0
    private var _c = 0
    private var _d = 0
    private var firstPartAnswer = 0
    private var secondPartAnswer = 0
    private var correctAnswer = 0

    fun generateTask(type : Int, level : Int) : Task{
        var _type = type
        if(type == LevelHandler.TYPE_RANDOM) _type = kotlin.random.Random.nextInt(0, 4)

        typeParts = kotlin.random.Random.nextInt(1, 3)

        //Область значений для примеров на умножения
        when(_type) {
            LevelHandler.TYPE_MULTIPLY -> {
                var from = 0
                var until = 9
                when(level){
                    2 -> {from = -25; until = 25}
                    3 -> {from = -50; until = 50}
                }

                if(typeParts == LevelHandler.TYPE_SUM){ //умножение -> сложение
                    //Генерируем числа
                    _a = kotlin.random.Random.nextInt(from, until)
                    _b = kotlin.random.Random.nextInt(-9 + -_a, 10 + -_a)
                    _c = kotlin.random.Random.nextInt(from, until)
                    _d = kotlin.random.Random.nextInt(-9 + -_c, 10 + -_c)

                    firstPartAnswer = _a + _b
                    secondPartAnswer = _c + _d
                }

                if(typeParts == LevelHandler.TYPE_DIF){ //умножение -> вычитание
                    //Генерируем числа
                    _a = kotlin.random.Random.nextInt(from, until)
                    _b = kotlin.random.Random.nextInt(-9 + _a, 10 + _a)

                    _c = kotlin.random.Random.nextInt(from, until)
                    _d = kotlin.random.Random.nextInt(-9 + _c, 10 + _c)

                    firstPartAnswer = _a - _b
                    secondPartAnswer = _c - _d
                }

            }

            LevelHandler.TYPE_SUM, LevelHandler.TYPE_DIF -> { //сложение и вычитание
                var from = 0
                var until = 9
                when(level){
                    2 -> {from = 0; until = 25}
                    3 -> {from = -50; until = 50}
                }

                //Генерируем числа
                _a = kotlin.random.Random.nextInt(from, until)
                _b = kotlin.random.Random.nextInt(from, until)
                _c = kotlin.random.Random.nextInt(from, until)
                _d = kotlin.random.Random.nextInt(from, until)

                if (typeParts == LevelHandler.TYPE_DIF) { //сложение и вычитание -> вычитание
                    if(level <= 2 && _b > _a) {val _t = _b; _b = _a; _a = _t} //для первых уровней убираем отрицательные ответы
                    if(level <= 2 && _d > _c) {val _t = _d; _d = _c; _c = _t} //для первых уровней убираем отрицательные ответы
                    firstPartAnswer = _a - _b
                    secondPartAnswer = _c - _d
                } else if (typeParts == LevelHandler.TYPE_SUM) { //сложение и вычитание -> сложение
                    firstPartAnswer = _a + _b
                    secondPartAnswer = _c + _d
                }

                if(type == LevelHandler.TYPE_DIF){ //для первых уровней убираем отрицательные ответы
                    if(secondPartAnswer > firstPartAnswer){
                        val _t = secondPartAnswer
                        secondPartAnswer = firstPartAnswer
                        firstPartAnswer = _t
                    }
                }
            }

            LevelHandler.TYPE_DIV -> { //деление
                // Генерация числителя и знаменателя таким образом, чтобы знаменатель был делителем числителя
                var numerator = 0
                var denominator = 0

                do {
                    var from = 0
                    val until = 50
                    if(level == 3) from = -50

                    _a = kotlin.random.Random.nextInt(from, until)
                    _b = kotlin.random.Random.nextInt(from, until)
                    _c = kotlin.random.Random.nextInt(from, until)
                    _d = kotlin.random.Random.nextInt(from, until)

                    if(typeParts == LevelHandler.TYPE_SUM){ //деление -> сложение
                        numerator = _a + _b
                        denominator = _c + _d
                    }else if(typeParts == LevelHandler.TYPE_DIF) { //деление -> вычитание
                        if(level <= 2 && _b > _a) {val _t = _b; _b = _a; _a = _t}
                        if(level <= 2 && _d > _c) {val _t = _d; _d = _c; _c = _t}
                        numerator = _a - _b
                        denominator = _c - _d
                    }

                    // Если знаменатель не является делителем числителя или результат деления больше или равен 9, генерируем новые числа
                }while (denominator == 0 //не делим на ноль
                    || numerator % denominator != 0 //полученные части делятся на цело
                    || numerator / denominator >= 9 //полученные части делятся в пределах таблтцы умножения до 9
                    || numerator == denominator //полученные части не равны
                    || denominator > 9 || denominator < 9) //делитель не больше и не меньше 9
                firstPartAnswer = numerator
                secondPartAnswer = denominator
            }
        }

        when(_type){
            LevelHandler.TYPE_MULTIPLY -> { //умножение
                correctAnswer =  firstPartAnswer * secondPartAnswer
            }
            LevelHandler.TYPE_DIV -> { //деление
                correctAnswer = firstPartAnswer / secondPartAnswer
            }
            LevelHandler.TYPE_SUM -> { //сложение
                correctAnswer = firstPartAnswer + secondPartAnswer
            }
            LevelHandler.TYPE_DIF -> { //вычитание
                correctAnswer = firstPartAnswer - secondPartAnswer
            }
        }

        //возвращаем сгенерированный пример
        return Task(_a, _b, _c, _d, 0, correctAnswer, _type, typeParts)
    }

}
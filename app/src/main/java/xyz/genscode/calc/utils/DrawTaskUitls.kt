package xyz.genscode.calc.utils

import xyz.genscode.calc.data.LevelHandler
import xyz.genscode.calc.models.Task

//Этот класс принимает задачу (task), и возвращает эту задачу в строчке - (5+2) * (6-6)
class DrawTaskUitls() {

    fun taskToStr(task : Task, showCorrectAnswer : Boolean) : String{
        val _a = task.a
        val _b = task.b
        val _c = task.c
        val _d = task.d
        val type = task.type
        val typeParts = task.typeParts
        val correctAnswer = task.correctAnswer

        when(task.difficult){
            LevelHandler.LEVEL_SIMPLE -> {
                var str_a = "$_a"; var str_b = "$_b" //создаем строку для каждого члена
                if(_a < 0) str_a = "($_a)" //Если а минусовое - добавляем скобки
                if(_b < 0) str_b = "($_b)" //Если b минусовое - добавляем скобки
                if(type == LevelHandler.TYPE_SQUARED) str_b = "²"

                return if(showCorrectAnswer)
                    "$str_a${LevelHandler.getChar(type)}$str_b = $correctAnswer"
                else
                    "$str_a${LevelHandler.getChar(type)}$str_b"
            }

            else -> {
                var str_a = "$_a"; var str_b = "$_b"; var str_c = "$_c"; var str_d = "$_d" //создаем строку для каждого члена

                //если число в минусе, отображаем в скобках
                if(_a < 0) str_a = "($_a)"; if(_b < 0) str_b = "($_b)"; if(_c < 0) str_c = "($_c)"; if(_d < 0) str_d = "($_d)"

                return if(showCorrectAnswer)
                    "($str_a${LevelHandler.getChar(typeParts)}$str_b) ${LevelHandler.getChar(type)} ($str_c${LevelHandler.getChar(typeParts)}$str_d) = $correctAnswer"
                else
                    "($str_a${LevelHandler.getChar(typeParts)}$str_b) ${LevelHandler.getChar(type)} ($str_c${LevelHandler.getChar(typeParts)}$str_d)"
            }
        }

    }
}
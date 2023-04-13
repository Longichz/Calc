package xyz.genscode.calc.data

import xyz.genscode.calc.models.Task

class LevelHandler private constructor() {
    var currentLevel : Int = 0
    var tasks = ArrayList<Task>()

    companion object {
        val instance = LevelHandler()

        //Настройки времени и количества заданий
        // для каждого уровня и каждого типа задания

        //Умножение
        private const val LEVEL1_MULTIPLY_TIME = 10
        private const val LEVEL1_MULTIPLY_TASKS = 8

        private const val LEVEL2_MULTIPLY_TIME = 6
        private const val LEVEL2_MULTIPLY_TASKS = 10

        private const val LEVEL3_MULTIPLY_TIME = 4
        private const val LEVEL3_MULTIPLY_TASKS = 12

        //Деление
        private const val LEVEL1_DIV_TIME = 10
        private const val LEVEL1_DIV_TASKS = 8

        private const val LEVEL2_DIV_TIME = 6
        private const val LEVEL2_DIV_TASKS = 10

        private const val LEVEL3_DIV_TIME = 4
        private const val LEVEL3_DIV_TASKS = 12

        //Квадрат числа (настройка распространяется и на куб числа)
        private const val LEVEL1_SQUARED_TIME = 10
        private const val LEVEL1_SQUARED_TASKS = 8

        private const val LEVEL2_SQUARED_TIME = 5
        private const val LEVEL2_SQUARED_TASKS = 10

        private const val LEVEL3_SQUARED_TIME = 30
        private const val LEVEL3_SQUARED_TASKS = 12

        //Сумма (настройка распространяется и на вычитание)
        private const val LEVEL_SUM_TIME = 10

        private const val LEVEL1_SUM_TASKS = 8
        private const val LEVEL2_SUM_TASKS = 10
        private const val LEVEL3_SUM_TASKS = 12

        //Типы заданий
        const val TYPE_MULTIPLY = 0
        const val TYPE_SUM = 1
        const val TYPE_DIF = 2
        const val TYPE_DIV = 3
        const val TYPE_SQUARED = 4
        const val TYPE_CUBED = 5
        const val TYPE_RANDOM = 6

        const val LEVEL_SIMPLE = 0
        const val LEVEL_MEDIUM = 1

        //Метод возвращает время для конкретного типа и уровня
        fun getTime(type : Int, level : Int) : Int{
            when(type){
                TYPE_MULTIPLY -> {
                    when(level){
                        1 -> return LEVEL1_MULTIPLY_TIME
                        2 -> return LEVEL2_MULTIPLY_TIME
                        3 -> return LEVEL3_MULTIPLY_TIME
                    }
                }
                TYPE_DIV -> {
                    when(level){
                        1 -> return LEVEL1_DIV_TIME
                        2 -> return LEVEL2_DIV_TIME
                        3 -> return LEVEL3_DIV_TIME
                    }
                }
                TYPE_SQUARED, TYPE_CUBED -> {
                    when(level){
                        1 -> return LEVEL1_SQUARED_TIME
                        2 -> return LEVEL2_SQUARED_TIME
                        3 -> return LEVEL3_SQUARED_TIME
                    }
                }
                else -> {
                    return LEVEL_SUM_TIME
                }
            }
            return 0
        }

        //Метод возвращает количество заданий для конкретного типа и уровня
        fun getTasks(type : Int, level : Int) : Int{
            when(type){
                TYPE_MULTIPLY -> {
                    when(level){
                        1 -> return LEVEL1_MULTIPLY_TASKS
                        2 -> return LEVEL2_MULTIPLY_TASKS
                        3 -> return LEVEL3_MULTIPLY_TASKS
                    }
                }
                TYPE_DIV -> {
                    when(level){
                        1 -> return LEVEL1_DIV_TASKS
                        2 -> return LEVEL2_DIV_TASKS
                        3 -> return LEVEL3_DIV_TASKS
                    }
                }
                TYPE_SQUARED, TYPE_CUBED -> {
                    when(level){
                        1 -> return LEVEL1_SQUARED_TASKS
                        2 -> return LEVEL2_SQUARED_TASKS
                        3 -> return LEVEL3_SQUARED_TASKS
                    }
                }
                else -> {
                    when(level){
                        1 -> return LEVEL1_SUM_TASKS
                        2 -> return LEVEL2_SUM_TASKS
                        3 -> return LEVEL3_SUM_TASKS
                    }
                }
            }
            return 0
        }

        //Метод возвращает знак для определенного типа
        fun getChar(type : Int) : String{
            when(type){
                TYPE_MULTIPLY -> {
                    return " * "
                }
                TYPE_DIV -> {
                    return " / "
                }
                TYPE_SUM -> {
                    return " + "
                }
                TYPE_DIF -> {
                    return " - "
                }
                TYPE_SQUARED, TYPE_CUBED -> {
                    return ""
                }
            }
            return ""
        }
    }
}

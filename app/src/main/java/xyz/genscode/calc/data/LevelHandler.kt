package xyz.genscode.calc.data

import xyz.genscode.calc.models.Task

class LevelHandler private constructor() {
    var currentLevel : Int = 0
    var tasks = ArrayList<Task>()

    companion object {
        val instance = LevelHandler()

        const val LEVEL1_MULTIPLY_TIME = 10
        const val LEVEL1_MULTIPLY_TASKS = 8

        const val LEVEL2_MULTIPLY_TIME = 6
        const val LEVEL2_MULTIPLY_TASKS = 10

        const val LEVEL3_MULTIPLY_TIME = 4
        const val LEVEL3_MULTIPLY_TASKS = 12

        const val LEVEL_SUM_TIME = 10

        const val LEVEL1_SUM_TASKS = 8
        const val LEVEL2_SUM_TASKS = 10
        const val LEVEL3_SUM_TASKS = 12

        const val TYPE_MULTIPLY = 0
        const val TYPE_SUM = 1
        const val TYPE_DIF = 2

        fun getTime(type : Int, level : Int) : Int{
            when(type){
                TYPE_MULTIPLY -> {
                    when(level){
                        1 -> return LEVEL1_MULTIPLY_TIME
                        2 -> return LEVEL2_MULTIPLY_TIME
                        3 -> return LEVEL3_MULTIPLY_TIME
                    }
                }
                else -> {
                    return LEVEL_SUM_TIME
                }
            }
            return 0
        }

        fun getTasks(type : Int, level : Int) : Int{
            when(type){
                TYPE_MULTIPLY -> {
                    when(level){
                        1 -> return LEVEL1_MULTIPLY_TASKS
                        2 -> return LEVEL2_MULTIPLY_TASKS
                        3 -> return LEVEL3_MULTIPLY_TASKS
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

        fun getChar(type : Int) : String{
            when(type){
                TYPE_MULTIPLY -> {
                    return "*"
                }
                TYPE_SUM -> {
                    return "+"
                }
                TYPE_DIF -> {
                    return "-"
                }
            }
            return ""
        }
    }
}

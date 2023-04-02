package xyz.genscode.calc.data

import xyz.genscode.calc.models.Task

class LevelHandler private constructor() {
    var currentLevel : Int = 0
    var tasks = ArrayList<Task>()

    companion object {
        val instance = LevelHandler()

        const val LEVEL1_TIME = 10
        const val LEVEL1_TASKS = 8

        const val LEVEL2_TIME = 6
        const val LEVEL2_TASKS = 10

        const val LEVEL3_TIME = 4
        const val LEVEL3_TASKS = 12

        const val TYPE_MULTIPLY = 0
    }
}

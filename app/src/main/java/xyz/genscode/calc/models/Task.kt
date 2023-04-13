package xyz.genscode.calc.models

import xyz.genscode.calc.data.LevelHandler

class Task(var a : Int, var b : Int, var c : Int, var d : Int, var answer : Int, var correctAnswer : Int, var type : Int, typeParts : Int, var level : Int) {
    constructor(a : Int, b : Int, answer : Int, correctAnswer : Int, type : Int) //Конструктор для simple levels
            : this(a, b, 0, 0, answer, correctAnswer, type, 0, LevelHandler.LEVEL_SIMPLE)

    constructor(a : Int, b : Int, c : Int, d : Int, answer : Int, correctAnswer : Int, type : Int, typeParts : Int) //Конструктор для medium levels
            : this(a, b, c, d, answer, correctAnswer, type, typeParts, LevelHandler.LEVEL_MEDIUM)
}
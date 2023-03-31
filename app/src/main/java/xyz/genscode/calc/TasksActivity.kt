package xyz.genscode.calc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xyz.genscode.calc.databinding.ActivityTasksBinding

class TasksActivity : AppCompatActivity() {
    lateinit var b : ActivityTasksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(b.root)


    }
}
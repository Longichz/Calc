package xyz.genscode.calc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xyz.genscode.calc.databinding.ActivityTasksBinding
import xyz.genscode.calc.utils.ui.HoverUtils

class TasksActivity : AppCompatActivity() {
    lateinit var b : ActivityTasksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(b.root)

        //Назначаем Hover
        HoverUtils().setHover(b.llTaskMultiply)

        b.llTaskMultiply.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
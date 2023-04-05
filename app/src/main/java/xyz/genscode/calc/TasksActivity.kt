package xyz.genscode.calc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xyz.genscode.calc.data.LevelHandler
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
        HoverUtils().setHover(b.llTaskDif)
        HoverUtils().setHover(b.llTaskSum)

        b.llTaskMultiply.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("type", LevelHandler.TYPE_MULTIPLY)
            startActivity(intent)
        }

        b.llTaskSum.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("type", LevelHandler.TYPE_SUM)
            startActivity(intent)
        }

        b.llTaskDif.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("type", LevelHandler.TYPE_DIF)
            startActivity(intent)
        }
    }
}
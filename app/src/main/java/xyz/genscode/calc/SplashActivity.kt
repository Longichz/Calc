package xyz.genscode.calc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import xyz.genscode.calc.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    lateinit var b : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(b.root)

        //Просто показываем логотип
        Handler().postDelayed({
            intent = Intent(this, TasksActivity :: class.java)
            startActivity(intent)
        }, 1500)
    }
}
package xyz.genscode.calc.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import androidx.core.content.ContextCompat.getSystemService

class Vibrator(val context : Context) {
    @SuppressLint("ServiceCast")
    fun vibrate(timeMillis : Int){
        val vibrator : Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(timeMillis)
    }

}
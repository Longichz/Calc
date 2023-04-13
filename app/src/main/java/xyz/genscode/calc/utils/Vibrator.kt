package xyz.genscode.calc.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import xyz.genscode.calc.data.SettingsHandler

class Vibrator(val context : Context) {
    fun vibrate(timeMillis : Long){
        if(!SettingsHandler.instance.vibro) return
        val vibrator : Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(timeMillis, 255))
        }else{
            vibrator.vibrate(timeMillis)
        }
    }

    fun vibrate(timeMillis : Long, amplitude : Int){
        if(!SettingsHandler.instance.vibro) return
        val vibrator : Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(timeMillis, amplitude))
        }else{
            vibrator.vibrate(timeMillis)
        }

    }

}
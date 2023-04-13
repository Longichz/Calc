package xyz.genscode.calc.utils.ui

import android.annotation.SuppressLint
import android.os.Handler
import android.view.MotionEvent
import android.view.View

class HoverUtils{

    @SuppressLint("ClickableViewAccessibility")
    fun setHover(obj : View){
        obj.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    view.animate().scaleX(0.98f).scaleY(0.98f).alpha(0.6f).setDuration(50).start()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->{
                    Handler().postDelayed({
                        view.animate().scaleX(1f).scaleY(1f).alpha(1f).start()
                    }, 50)
                }
            }
            return@OnTouchListener false
        })
    }

}
package xyz.genscode.calc.utils

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat


class ChangeColorUtils(var context : Context) {
    private var transitionDrawable: TransitionDrawable? = null

    fun setBackground(v: View, drawableId: Int, millis : Int) {
        val drawable = ContextCompat.getDrawable(context, drawableId)

        val layers = if (transitionDrawable != null) {
            arrayOf(transitionDrawable, drawable)
        } else {
            arrayOf(v.background, drawable)
        }

        transitionDrawable = TransitionDrawable(layers)
        transitionDrawable?.startTransition(millis)

        transitionDrawable?.setCrossFadeEnabled(true)

        transitionDrawable?.setCallback(object : Drawable.Callback {
            override fun invalidateDrawable(d: Drawable) {
                v.invalidate()
            }

            override fun scheduleDrawable(d: Drawable, what: Runnable, `when`: Long) {
                v.postDelayed(what, `when`)
            }

            override fun unscheduleDrawable(d: Drawable, what: Runnable) {
                v.removeCallbacks(what)
            }
        })

        v.background = transitionDrawable
    }

    fun setColor(v : TextView, color : Int){
        val from = v.currentTextColor
        val to = ContextCompat.getColor(context, color)

        ObjectAnimator.ofObject(v, "textColor", ArgbEvaluator(), from, to)
            .setDuration(250).start()
    }
}
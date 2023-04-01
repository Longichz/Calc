package xyz.genscode.calc.utils.ui

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import xyz.genscode.calc.MainActivity
import xyz.genscode.calc.databinding.ActivityMainBinding
import xyz.genscode.calc.interfaces.OnLevelSelectedListener

class LevelSelectUtils(private val onLevelSelectedListener: OnLevelSelectedListener) {
    var startX = 0

    @SuppressLint("ClickableViewAccessibility")
    fun setOnTouchEvent(b : ActivityMainBinding){
        b.llLevels.setOnTouchListener { view,  motionEvent ->
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    startX = motionEvent.x.toInt() - b.llPopupHeaderContent.x.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    b.llPopupHeaderContent.x = motionEvent.x - startX
                }
                MotionEvent.ACTION_UP -> {
                    //Определяем абсолютную позицию первого и второго уровня
                    val level2View = Rect()
                    val level3View = Rect()
                    b.tvPopupHeader2.getLocalVisibleRect(level2View)
                    b.tvPopupHeader3.getLocalVisibleRect(level3View)

                    //Выбрали Level1
                    if (
                        b.llPopupHeaderContent.x >= 0 //До упора влево
                        || (level2View.right > 0 &&
                                level2View.right < b.tvPopupHeader1.measuredWidth / 2)
                    ) {
                        b.llPopupHeaderContent.animate().x(0f).start()
                        onLevelSelectedListener.onLevelSelected(1)
                        return@setOnTouchListener true
                    }

                    //Выбрали level2
                    if(
                        level2View.right < b.tvPopupHeader1.measuredWidth
                        && level2View.right >= b.tvPopupHeader1.measuredWidth / 2

                        || level3View.right >= 0
                        && level3View.right <= b.tvPopupHeader1.width/2
                    ) {
                        val toX = -(b.llPopupHeaderContent.width -
                                b.tvPopupHeader1.measuredWidth*2)

                        b.llPopupHeaderContent.animate().x(toX.toFloat()).start()

                        onLevelSelectedListener.onLevelSelected(2)
                        return@setOnTouchListener true
                    }

                    //Выбрали level3
                    if(
                        b.llPopupHeaderContent.width +
                        b.llPopupHeaderContent.x - b.tvPopupHeader1.measuredWidth < 0 //До упора вправо
                        || level2View.left > b.tvPopupHeader1.measuredWidth /2

                    ) {
                        val toX = -(b.llPopupHeaderContent.width-b.tvPopupHeader1.measuredWidth)
                        b.llPopupHeaderContent.animate().x(toX.toFloat()).start()

                        onLevelSelectedListener.onLevelSelected(3)
                        return@setOnTouchListener true
                    }
                }
            }

            return@setOnTouchListener false

        }
    }
}
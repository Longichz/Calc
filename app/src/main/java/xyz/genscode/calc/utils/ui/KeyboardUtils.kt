package xyz.genscode.calc.utils.ui

import android.annotation.SuppressLint
import xyz.genscode.calc.databinding.ActivityMainBinding
import xyz.genscode.calc.utils.Vibrator

class KeyboardUtils(val b : ActivityMainBinding) {
    @SuppressLint("SetTextI18n")
    fun setKeyBoard(){
        b.keyBoard0.setOnClickListener { b.tvPopupAnswer.text = "${b.tvPopupAnswer.text}0"; Vibrator(b.ll.context).vibrate(15)}
        b.keyBoard1.setOnClickListener { b.tvPopupAnswer.text = "${b.tvPopupAnswer.text}1"; Vibrator(b.ll.context).vibrate(15)}
        b.keyBoard2.setOnClickListener { b.tvPopupAnswer.text = "${b.tvPopupAnswer.text}2"; Vibrator(b.ll.context).vibrate(15)}
        b.keyBoard3.setOnClickListener { b.tvPopupAnswer.text = "${b.tvPopupAnswer.text}3"; Vibrator(b.ll.context).vibrate(15)}
        b.keyBoard4.setOnClickListener { b.tvPopupAnswer.text = "${b.tvPopupAnswer.text}4"; Vibrator(b.ll.context).vibrate(15)}
        b.keyBoard5.setOnClickListener { b.tvPopupAnswer.text = "${b.tvPopupAnswer.text}5"; Vibrator(b.ll.context).vibrate(15)}
        b.keyBoard6.setOnClickListener { b.tvPopupAnswer.text = "${b.tvPopupAnswer.text}6"; Vibrator(b.ll.context).vibrate(15)}
        b.keyBoard7.setOnClickListener { b.tvPopupAnswer.text = "${b.tvPopupAnswer.text}7"; Vibrator(b.ll.context).vibrate(15)}
        b.keyBoard8.setOnClickListener { b.tvPopupAnswer.text = "${b.tvPopupAnswer.text}8"; Vibrator(b.ll.context).vibrate(15)}
        b.keyBoard9.setOnClickListener { b.tvPopupAnswer.text = "${b.tvPopupAnswer.text}9"; Vibrator(b.ll.context).vibrate(15)}
        b.keyBoardMinus.setOnClickListener { b.tvPopupAnswer.text = "${b.tvPopupAnswer.text}-"; Vibrator(b.ll.context).vibrate(15)}
        b.keyBoardBack.setOnClickListener { b.tvPopupAnswer.text = "${b.tvPopupAnswer.text.dropLast(1)}"; Vibrator(b.ll.context).vibrate(15) }
    }
}
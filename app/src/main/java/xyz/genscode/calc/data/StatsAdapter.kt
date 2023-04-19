package xyz.genscode.calc.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import xyz.genscode.calc.R
import xyz.genscode.calc.models.Task
import xyz.genscode.calc.utils.DrawTaskUitls
import java.util.*

class StatsAdapter(var context: Context, var tasks: ArrayList<Task>) : RecyclerView.Adapter<StatsAdapter.StatsListView>() {
    val THEME_DAY = 0
    val THEME_NIGHT = 1

    var view : View? = null
    var theme : Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsListView {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> theme = THEME_DAY
            Configuration.UI_MODE_NIGHT_YES -> theme = THEME_NIGHT
        }

        view = LayoutInflater.from(context).inflate(R.layout.item_stats, parent, false)
        return StatsListView(view!!)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StatsListView, position: Int) {
        val task = tasks[position]
        val correctAnswer = task.correctAnswer //Корректный ответ
        val answer = task.answer //Ответ пользователя
        val type = task.type //Тип
        val typeParts = task.typeParts //Тип в скобках
        val difficult = task.difficult //Сложность
        val _a = task.a //a
        val _b = task.b //b
        val _c = task.c //c
        val _d = task.d //d

        //Отображаем пример
        var instance = ""

        instance = DrawTaskUitls().taskToStr(task, true)

        if(correctAnswer != answer){
            //Красный цвет примера если ответы не сходятся
            holder.tvInstance.setTextColor(ContextCompat.getColor(context, R.color.hard))

            //В скобках ответ пользователя если ответы не сходятся
            instance = "$instance ($answer)"
        }else{
            //Восстанавливаем цвет примера если ответы сходятся
            when(theme){
                THEME_DAY -> holder.tvInstance.setTextColor(ContextCompat.getColor(context, R.color.grey_650))
                THEME_NIGHT -> holder.tvInstance.setTextColor(ContextCompat.getColor(context, R.color.white))
            }

        }

        holder.tvInstance.text = instance
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    inner class StatsListView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvInstance: TextView

        init {
            tvInstance = view!!.findViewById(R.id.tvStatsInstance)
        }

    }
}
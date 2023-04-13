package xyz.genscode.calc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import xyz.genscode.calc.R

class About : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isAboutDevShowed = false
        val tvShow = view.findViewById<TextView>(R.id.tvShowAboutDev)
        val llAboutApp = view.findViewById<View>(R.id.llSettingsAboutApp)
        val llAboutDev = view.findViewById<View>(R.id.llSettingsAboutDev)

        tvShow.setOnClickListener {
            if(isAboutDevShowed){
                llAboutDev.visibility = View.GONE
                llAboutApp.visibility = View.VISIBLE
                tvShow.text = resources.getText(R.string.app_dev_show)
            }else{
                llAboutApp.visibility = View.GONE
                llAboutDev.visibility = View.VISIBLE
                tvShow.text = resources.getText(R.string.app_dev_hide)
            }
            isAboutDevShowed = !isAboutDevShowed
        }

    }

}
package ferit.student.matijazagar.smishhunter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton


class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_settings, container, false)

        view.findViewById<ImageButton>(R.id.buttonBackSettings).setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
        }

        return view

    }

}
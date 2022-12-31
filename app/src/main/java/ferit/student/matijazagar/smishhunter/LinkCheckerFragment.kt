package ferit.student.matijazagar.smishhunter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class LinkCheckerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_link_checker, container, false)

        view.findViewById<ImageButton>(R.id.buttonBackLinkChecker)?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        return view
    }

}
package ferit.student.matijazagar.smishhunter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction


class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Toast.makeText(context,"Created", Toast.LENGTH_LONG).show()
        view?.findViewById<Button>(R.id.buttonReports)?.setOnClickListener {//does not work
            Toast.makeText(context,"Pressed", Toast.LENGTH_LONG).show()
            val reportsFragment = ReportsFragment();
            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, reportsFragment)
            fragmentTransaction?.commit()
        }


        return inflater.inflate(R.layout.fragment_main, container, false)
    }



}
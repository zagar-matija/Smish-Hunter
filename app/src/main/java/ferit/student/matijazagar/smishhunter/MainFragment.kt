package ferit.student.matijazagar.smishhunter

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction


class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_main, container, false)


        view?.findViewById<Button>(R.id.buttonReports)?.setOnClickListener {
            val reportsFragment = ReportsFragment()
            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction
                ?.replace(R.id.fragmentContainerView, reportsFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        view?.findViewById<Button>(R.id.buttonAbout)?.setOnClickListener {
            createAboutView()
        }

        view?.findViewById<Button>(R.id.buttonCheckLink)?.setOnClickListener {
            val checkLinkFragment = LinkCheckerFragment()
            val fragmentTransaction : FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction
                ?.replace(R.id.fragmentContainerView, checkLinkFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
        return view

    }

    private fun createAboutView(){
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setIcon( ResourcesCompat.getDrawable(resources, R.drawable.ic_placeholder_logo, context?.theme))
            .setTitle("About")
            .setMessage(getString(R.string.about_text))
            .setPositiveButton("OK", null)
            .show()
    }


}




















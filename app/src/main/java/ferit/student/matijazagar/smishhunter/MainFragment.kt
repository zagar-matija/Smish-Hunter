package ferit.student.matijazagar.smishhunter

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import java.util.*


class MainFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
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
        view?.findViewById<ImageButton>(R.id.buttonSettingsMain)?.setOnClickListener {
            val settingsFragment = SettingsFragment()
            val fragmentTransaction : FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction
                ?.replace(R.id.fragmentContainerView, settingsFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
        val policy = ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val encodedString: String = Base64.getEncoder().encodeToString("http://google.com".toByteArray())
        Log.d("base64 encode",encodedString)

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





















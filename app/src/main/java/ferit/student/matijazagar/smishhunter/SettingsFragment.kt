package ferit.student.matijazagar.smishhunter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.content.SharedPreferences
import android.widget.Switch
import android.widget.Toast


const val myPrefMalicious = "getMaliciousNotifications"
const val myPrefSuspicious = "getSuspiciousNotifications"
const val myPrefAdding = "addHarmlessMessagesToReports"




class SettingsFragment : Fragment() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_settings, container, false)

        view.findViewById<ImageButton>(R.id.buttonBackSettings).setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
        }
        val switchGetMaliciousNotif = view.findViewById<Switch>(R.id.switchSetting1)
        val switchGetSuspicousNotif = view.findViewById<Switch>(R.id.switchSetting2)
        val switchShowHarmlessSMS = view.findViewById<Switch>(R.id.switchSetting3)

        switchGetMaliciousNotif.isChecked = getPreferenceValue(myPrefMalicious)=="on"
        switchGetSuspicousNotif.isChecked = getPreferenceValue(myPrefSuspicious)=="on"
        switchShowHarmlessSMS.isChecked = getPreferenceValue(myPrefAdding)=="on"

        switchGetMaliciousNotif.setOnCheckedChangeListener { _, isChecked ->
            val newPreference = if(isChecked) "on" else "off"
            writeToPreference(newPreference, myPrefMalicious)
        }
        switchGetSuspicousNotif.setOnCheckedChangeListener { _, isChecked ->
            val newPreference = if(isChecked) "on" else "off"
            writeToPreference(newPreference, myPrefSuspicious)
        }
        switchShowHarmlessSMS.setOnCheckedChangeListener { _, isChecked ->
            val newPreference = if(isChecked) "on" else "off"
            writeToPreference(newPreference, myPrefAdding)
        }


        return view

    }
    private fun getPreferenceValue(preference: String ): String? {
        val sp: SharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        return sp.getString(preference, "on")
    }
    private fun writeToPreference(value: String, preference: String ){
        val editor: SharedPreferences.Editor = requireContext().getSharedPreferences("myPreferences", 0).edit()
        editor.putString(preference, value)
        editor.apply()
    }
}
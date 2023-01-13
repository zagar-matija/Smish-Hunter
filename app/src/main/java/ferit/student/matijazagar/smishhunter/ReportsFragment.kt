package ferit.student.matijazagar.smishhunter

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class ReportsFragment : Fragment() {
    private val fileName = "reports.json"
    private val dirName  = "json"
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_reports, container, false)

        val recyclerView=view.findViewById<RecyclerView>(R.id.recyclerViewReports)

        val reports : ArrayList<Report> = getReportsFromFile()


        val recyclerAdapter = ReportsRecyclerAdapter(reports)
        recyclerView.adapter=recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        view.findViewById<ImageButton>(R.id.buttonBackReports).setOnClickListener {

            activity?.supportFragmentManager?.popBackStackImmediate()
        }

        return view
    }



    private fun getReportsFromFile() : ArrayList<Report>{
        val dir = context?.getDir(dirName, Context.MODE_PRIVATE)
        val file = File(dir, fileName)
        //file.createNewFile()

        val jsonString = file.readText()
        val listReportType = object : TypeToken<ArrayList<Report>>() {}.type
        var reports = Gson().fromJson<ArrayList<Report>>(jsonString,listReportType)
        if(reports.isNullOrEmpty())
            reports = ArrayList<Report>()

        Log.d("read JSON reports", jsonString)
        return reports
    }

}
package ferit.student.matijazagar.smishhunter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class ReportsFragment : Fragment() {
    private val fileName = "reports.json"
    private val dirName  = "json"

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_reports, container, false)

        val recyclerView=view.findViewById<RecyclerView>(R.id.recyclerViewReports)

        var reports : ArrayList<Report> = getReportsFromFile()


        val recyclerAdapter = context?.let { ReportsRecyclerAdapter(reports, it) }
        recyclerView.adapter=recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val swipeLayout = view.findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipeContainer)
        swipeLayout.setOnRefreshListener {
            reports = getReportsFromFile()
            recyclerAdapter?.clear()
            recyclerAdapter?.addAll(reports)
            swipeLayout.isRefreshing = false
        }
        view.findViewById<ImageButton>(R.id.buttonBackReports).setOnClickListener {

            activity?.supportFragmentManager?.popBackStackImmediate()
        }

        view.findViewById<ImageButton>(R.id.buttonDelete).setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(context)

            alertDialogBuilder.setPositiveButton(android.R.string.ok) { _, _ ->
                deleteReportsData()
                recyclerAdapter?.clear()
            }
            alertDialogBuilder.setNegativeButton(android.R.string.cancel){ _, _ ->

            }
            alertDialogBuilder.setIcon( ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_delete_24, context?.theme))
                .setMessage("Are you sure you want to delete ALL reports?")
                .show()
        }

        return view
    }

    private fun deleteReportsData(){
        val dir = context?.getDir(dirName, Context.MODE_PRIVATE)
        val file = File(dir, fileName)
        file.createNewFile()
        file.outputStream().write("[]".toByteArray())
    }

    private fun getReportsFromFile() : ArrayList<Report>{
        val dir = context?.getDir(dirName, Context.MODE_PRIVATE)
        val file = File(dir, fileName)
        file.createNewFile()

        val jsonString = if(file.readText()=="") "[]" else file.readText()
        val listReportType = object : TypeToken<ArrayList<Report>>() {}.type
        var reports = Gson().fromJson<ArrayList<Report>>(jsonString,listReportType)
        if(reports.isNullOrEmpty())
            reports = ArrayList()

        Log.d("read JSON reports", jsonString)
        return reports
    }
}

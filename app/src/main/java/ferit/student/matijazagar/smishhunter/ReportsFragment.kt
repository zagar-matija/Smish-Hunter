package ferit.student.matijazagar.smishhunter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException

class ReportsFragment : Fragment() {
    private val fileName = "reports.json"
    private val dirName  = "json"

    @RequiresApi(Build.VERSION_CODES.N)
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

        recyclerAdapter!!.onItemClick = { analysisResult ->
            createAnalysisAlertDialog(analysisResult)
        }

        recyclerAdapter.onLongItemClick = {position ->
            val alertDialogBuilder = AlertDialog.Builder(context)

            alertDialogBuilder.setPositiveButton(android.R.string.ok) { _, _ ->
                reports.removeAt(position)

                recyclerAdapter.clear()
                recyclerAdapter.addAll(reports)

                writeFileData(requireContext(), reports)

            }
            alertDialogBuilder.setNegativeButton(android.R.string.cancel){ _, _ ->

            }
            alertDialogBuilder.setIcon( ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_delete_24, context?.theme))
                .setMessage("Are you sure you want to delete this report?")
                .show()

        }


        val swipeLayout = view.findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipeContainer)
        swipeLayout.setOnRefreshListener {
            reports = getReportsFromFile()
            recyclerAdapter.clear()
            recyclerAdapter.addAll(reports)
            swipeLayout.isRefreshing = false
        }
        view.findViewById<ImageButton>(R.id.buttonBackReports).setOnClickListener {

            activity?.supportFragmentManager?.popBackStackImmediate()
        }

        view.findViewById<ImageButton>(R.id.buttonDelete).setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(context)

            alertDialogBuilder.setPositiveButton(android.R.string.ok) { _, _ ->
                deleteReportsData()
                recyclerAdapter.clear()
            }
            alertDialogBuilder.setNegativeButton(android.R.string.cancel){ _, _ ->

            }
            alertDialogBuilder.setIcon( ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_delete_24, context?.theme))
                .setMessage("Are you sure you want to delete ALL reports?")
                .show()
        }

        return view
    }
    private fun writeFileData(context: Context, reports: ArrayList<Report>){
        val dir = context.getDir(dirName, Context.MODE_PRIVATE)
        val file = File(dir, fileName)

        var newJsonString = ""
        try{
            newJsonString = Gson().toJson(reports)}
        catch (e: IOException){
            e.printStackTrace()
        }

        file.createNewFile()
        file.outputStream().write(newJsonString.toByteArray())
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

    @RequiresApi(Build.VERSION_CODES.N)
    fun createAnalysisAlertDialog(analysisResult: AnalysisResult ){
        val alertDialogBuilder = AlertDialog.Builder(context)

        if(analysisResult.data.attributes.last_analysis_stats.malicious>=3)
        {
            alertDialogBuilder.setTitle(Html.fromHtml("<h2>Malicous link</h2>", HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setIcon( ResourcesCompat.getDrawable(resources, R.drawable.ic_report_malicious_24, context?.theme))
        }
        else if(analysisResult.data.attributes.last_analysis_stats.suspicious>=3
            ||analysisResult.data.attributes.last_analysis_stats.malicious>=1 ){
            alertDialogBuilder.setTitle(Html.fromHtml("<h2>Suspicious link</h2>", HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setIcon( ResourcesCompat.getDrawable(resources, R.drawable.ic_report_suspicious_24, context?.theme))
        }
        else
            alertDialogBuilder.setTitle(Html.fromHtml("<h2>Harmless link</h2>", HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setIcon( ResourcesCompat.getDrawable(resources, R.drawable.ic_report_harmless_24, context?.theme))

        val message = Html.fromHtml("<h3>Link : &quot " + analysisResult.data.attributes.url + "&quot </h3><br>" +
                "<h3>Analysis statistics:</h3><br>" + "<ul>" +
                "<li>" + "engines reported malicous: " + analysisResult.data.attributes.last_analysis_stats.malicious + "</li>" +
                "<li>"+  "engines reported suspicious: " + analysisResult.data.attributes.last_analysis_stats.suspicious + "</li>" +
                "<li>"+  "engines reported harmless: " + analysisResult.data.attributes.last_analysis_stats.harmless + "</li>" +
                "<li>"+  "engines reported link timeout: " + analysisResult.data.attributes.last_analysis_stats.timeout + "</li>" +
                "<li>"+ " engines reported undetected: " + analysisResult.data.attributes.last_analysis_stats.undetected +  "</li>" +
                "</ul>"+
                "<h3>Community votes: </h3>" +
                "malicous: " + analysisResult.data.attributes.total_votes.malicious +
                "<br>harmless: " + analysisResult.data.attributes.total_votes.harmless, HtmlCompat.FROM_HTML_MODE_LEGACY)

        alertDialogBuilder.setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
}

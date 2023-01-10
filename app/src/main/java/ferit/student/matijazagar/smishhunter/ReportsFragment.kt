package ferit.student.matijazagar.smishhunter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ReportsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_reports, container, false)

        val recyclerView=view.findViewById<RecyclerView>(R.id.recyclerViewReports)

        val reports : ArrayList<Report> = getData()

        val recyclerAdapter = ReportsRecyclerAdapter(reports)
        recyclerView.adapter=recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        view.findViewById<ImageButton>(R.id.buttonBackReports).setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
        }

        return view
    }

    private fun getData() : ArrayList<Report>{

        val reports : ArrayList<Report> = ArrayList<Report>()
        reports.add(Report("rep1","malicious content","2/10", "banned bcos of link"))
        reports.add(Report("rep2","malicious content","4/10", "banned bcos of link"))

        return reports
    }


}
package ferit.student.matijazagar.smishhunter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReportsRecyclerAdapter(private val dataSet: ArrayList<Report>)
    : RecyclerView.Adapter<ReportsRecyclerAdapter.ViewHolder>()
    {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textViewTitle: TextView
        val textViewContent: TextView
        val textViewRating: TextView

        init {
            // Define click listener for the ViewHolder's View
            textViewTitle = view.findViewById(R.id.textViewReportTitle)
            textViewContent = view.findViewById(R.id.textViewReportContent)
            textViewRating = view.findViewById(R.id.textViewReportRating)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_report, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTitle.text = dataSet[position].title
        holder.textViewContent.text = dataSet[position].content
        holder.textViewRating.text = dataSet[position].rating
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}
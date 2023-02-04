package ferit.student.matijazagar.smishhunter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReportsRecyclerAdapter(private val dataSet: ArrayList<Report>, val context: Context)
    : RecyclerView.Adapter<ReportsRecyclerAdapter.ViewHolder>()
    {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textViewTitle: TextView
        val textViewContent: TextView
        val textViewRating: TextView
        val icon : ImageView

        init {
            textViewTitle = view.findViewById(R.id.textViewReportTitle)
            textViewContent = view.findViewById(R.id.textViewReportContent)
            textViewRating = view.findViewById(R.id.textViewReportRating)
            icon = view.findViewById(R.id.reportIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_report, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textViewTitle.text = dataSet[position].sender
        holder.textViewContent.text = dataSet[position].content
        holder.textViewRating.text = dataSet[position].rating

        if(dataSet[position].rating == "10/10")
            holder.icon.setImageDrawable(context.resources.getDrawable(R.drawable.ic_report_malicious_24,context.theme))
        else if ( dataSet[position].rating == "7/10")
            holder.icon.setImageDrawable(context.resources.getDrawable(R.drawable.ic_report_suspicious_24,context.theme))
        else
            holder.icon.setImageDrawable(context.resources.getDrawable(R.drawable.ic_report_harmless_24,context.theme))
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun clear() {
        dataSet.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(reports: ArrayList<Report>) {
        dataSet.addAll(reports)
        notifyDataSetChanged()
    }
}
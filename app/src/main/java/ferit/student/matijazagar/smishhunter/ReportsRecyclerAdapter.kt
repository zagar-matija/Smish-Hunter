package ferit.student.matijazagar.smishhunter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReportsRecyclerAdapter( val dataSet: ArrayList<Report>, val context: Context)
    : RecyclerView.Adapter<ReportsRecyclerAdapter.ViewHolder>()
    {
        var onItemClick: ((AnalysisResult) -> Unit)? = null
        var onLongItemClick: ((Int) -> Unit)? = null

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textViewTitle: TextView
            val textViewContent: TextView
            val textViewRating: TextView
            val icon: ImageView

            init {
                textViewTitle = view.findViewById(R.id.textViewReportTitle)
                textViewContent = view.findViewById(R.id.textViewReportContent)
                textViewRating = view.findViewById(R.id.textViewReportRating)
                icon = view.findViewById(R.id.reportIcon)

                view.setOnClickListener {
                    onItemClick?.invoke(dataSet[adapterPosition].analysisResult)
                }
                view.setOnLongClickListener {
                    onLongItemClick?.invoke(adapterPosition)
                    return@setOnLongClickListener true
                }

            }
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_report, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTitle.text = dataSet[position].sender
        holder.textViewContent.text = dataSet[position].content
        holder.textViewRating.text = "${dataSet[position].rating}/10"

        when (dataSet[position].rating) {
            context.resources.getString(R.string.level_malicious) -> holder.icon.setImageDrawable(context.resources.getDrawable(R.drawable.ic_report_malicious_24,context.theme))
            context.resources.getString(R.string.level_suspicious) -> holder.icon.setImageDrawable(context.resources.getDrawable(R.drawable.ic_report_suspicious_24,context.theme))
            else -> holder.icon.setImageDrawable(context.resources.getDrawable(R.drawable.ic_report_harmless_24,context.theme))
        }
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
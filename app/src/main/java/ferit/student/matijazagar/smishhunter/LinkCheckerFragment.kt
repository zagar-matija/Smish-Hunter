package ferit.student.matijazagar.smishhunter

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import kotlinx.coroutines.*

//todo add link reporting to vt
class LinkCheckerFragment : Fragment() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_link_checker, container, false)

        view.findViewById<ImageButton>(R.id.buttonBackLinkChecker)?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        val textInput = view.findViewById<EditText>(R.id.editTextLink)



        view.findViewById<Button>(R.id.buttonCheckLink).setOnClickListener {
            val text = textInput.text.toString()
            Log.d("Link", text)

            val links = URLAnalyser.extractURLs(text)
            if(links.size==1){
                val link = links[0]

                GlobalScope.launch(Dispatchers.IO + CoroutineExceptionHandler{_, throwable ->
                    throwable.printStackTrace()
                }) {
                    Log.d("corutine launch","launched")

                    val analysisResult = URLAnalyser.getLinkAnalysis(link)
                    Log.d("corutine launch","got analysis: "+ analysisResult.data.attributes.url)
                    launch(Dispatchers.Main){
                        createAnalysisAlertDialog(analysisResult)
                    }

                }

            }
            else{
                Toast.makeText(context,"Entered text is not a link!", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
     fun createAnalysisAlertDialog(analysisResult: AnalysisResult ){
        val alertDialogBuilder = AlertDialog.Builder(context)

        if(analysisResult.data.attributes.last_analysis_stats.malicious>=3)
        {
            alertDialogBuilder.setTitle(Html.fromHtml("<h2>Malicous link</h2>",HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setIcon( ResourcesCompat.getDrawable(resources, R.drawable.ic_report_malicious_24, context?.theme))
        }
        else if(analysisResult.data.attributes.last_analysis_stats.suspicious>=3
            ||analysisResult.data.attributes.last_analysis_stats.malicious>=1 ){
            alertDialogBuilder.setTitle(Html.fromHtml("<h2>Suspicious link</h2>",HtmlCompat.FROM_HTML_MODE_LEGACY))
                .setIcon( ResourcesCompat.getDrawable(resources, R.drawable.ic_report_suspicious_24, context?.theme))
        }
        else
            alertDialogBuilder.setTitle(Html.fromHtml("<h2>Harmless link</h2>",HtmlCompat.FROM_HTML_MODE_LEGACY))
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
                "<br>harmless: " + analysisResult.data.attributes.total_votes.harmless,HtmlCompat.FROM_HTML_MODE_LEGACY)

        alertDialogBuilder.setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

}
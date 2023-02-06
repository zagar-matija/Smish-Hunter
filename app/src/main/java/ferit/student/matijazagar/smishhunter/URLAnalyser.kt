package ferit.student.matijazagar.smishhunter

import android.os.Build
import android.util.Log
import android.util.Patterns
import androidx.annotation.RequiresApi
import java.util.*
import kotlin.collections.ArrayList

object URLAnalyser {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getLinkAnalysis(url: String) : AnalysisResult{

        var analysis = AnalysisResult(Data(
            Attributes(AnalysisStats(0,0,0,0,0),
                0,Votes(0, 0),"placeholder")))

        val encodedURL: String = Base64.getEncoder().encodeToString(url.toByteArray()).trimEnd('=')

        val request = ServiceBuilder.buildService(AnalysisAPIEndpoints::class.java)
        val call = request.getAnalysis("https://www.virustotal.com/api/v3/urls/$encodedURL")



        val response = call.execute()
        if(response.isSuccessful) {
            Log.d("API-response", "success "+response.message() + response.body().toString())
            analysis = response.body()!!
        }
        else
            Log.d("API-response", "fail \n message: " + response.message()
                    + "\nbody: "+response.body()
                    + "\nerrorbody: "+response.errorBody()
                    + "\nheaders of response: "+response.headers()
                    + "\ncall is executed: "+ call.isExecuted
                    + "\ncode: " + response.code()
                    +"\n call: "+ call.toString()
                    +"\n request: "+ request.toString())



        return analysis
    }


    fun extractURLs(message: String): ArrayList<String> {
        @Suppress("LocalVariableName") val URLs = ArrayList<String>()

        val urlMatcher = Patterns.WEB_URL.matcher(message)

        while (urlMatcher.find()) {
            val matchStart = urlMatcher.start(1)
            val matchEnd = urlMatcher.end()
            val url = message.substring(matchStart, matchEnd)
            if (matchStart < 0 || matchEnd > message.length) {
                Log.d(
                    "inspectURLs","Found URL outside of message body (from $matchStart to ${matchEnd}))")
            } else {
                Log.d("inspectURLs", "Found URL in message body: \"${url}\"")
                URLs.add(url)
            }
        }
        return URLs
    }

}
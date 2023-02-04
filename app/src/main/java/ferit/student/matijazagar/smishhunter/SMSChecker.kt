package ferit.student.matijazagar.smishhunter

import android.content.Context
import android.os.Build
import android.telephony.SmsMessage
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


object SMSChecker {

    private const val fileName  = "reports.json"
    private const val dirName  = "json"


    @RequiresApi(Build.VERSION_CODES.O)
    fun handleSMS(context: Context, sms : SmsMessage){
        val text = sms.displayMessageBody
        val sender = sms.originatingAddress
        var rating = "1/10"
        var explanation = "Harmless message"

        val links = URLAnalyser.extractURLs(sms.displayMessageBody)

        links.forEach {
            val analysis = URLAnalyser.getLinkAnalysis(it)

            if(analysis.data.attributes.last_analysis_stats.malicious>=3){
                explanation = "Message contains a malicious link."
                rating = "10/10"
            }
            else if(analysis.data.attributes.last_analysis_stats.suspicious>=3
                || analysis.data.attributes.last_analysis_stats.malicious>=1){
                explanation = "Message contains a suspicious link."
                rating = "7/10"
            }
        }


        val report = Report(sender,text,rating,explanation)

        val jsonString = readFileData(context)

        val listReportType = object : TypeToken<ArrayList<Report>>() {}.type
        val reports = ArrayList<Report>()
        reports.add(report)
        reports.addAll(Gson().fromJson<ArrayList<Report>>(jsonString,listReportType))


        var newJsonString = ""
        try{
            newJsonString = Gson().toJson(reports)
        }
        catch (e: IOException){
            e.printStackTrace()
        }

        writeFileData(context,newJsonString)


    }



    private fun readFileData(context: Context) : String{
        val dir = context.getDir(dirName, Context.MODE_PRIVATE)
        val file = File(dir, fileName)
        file.createNewFile()
        if(file.readText()=="")
            return "[]"
        return file.readText()
    }

    private fun writeFileData(context: Context, json: String){
        val dir = context.getDir(dirName, Context.MODE_PRIVATE)
        val file = File(dir, fileName)
        file.createNewFile()
        file.outputStream().write(json.toByteArray())
    }


}


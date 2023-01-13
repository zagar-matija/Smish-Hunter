package ferit.student.matijazagar.smishhunter

import android.content.Context
import android.telephony.SmsMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException
import kotlin.random.Random


object SMSChecker {

    private const val fileName  = "reports.json"
    private const val dirName  = "json"


    fun handleSMS(context: Context,sms : SmsMessage){
        val text = sms.displayMessageBody
        val sender = sms.originatingAddress
        val rating = Random(5).nextInt(10).toString() + "/10"
        val explanation = "Explanation as to why"

        val report = Report(sender,text,rating,explanation)

        val jsonString = readFileData(context)

        val listReportType = object : TypeToken<ArrayList<Report>>() {}.type
        var reports = Gson().fromJson<ArrayList<Report>>(jsonString,listReportType)
        if(reports.isNullOrEmpty())
            reports = ArrayList()
        reports.add(report)

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
         return file.readText()
    }

    private fun writeFileData(context: Context, json: String){
        val dir = context.getDir(dirName, Context.MODE_PRIVATE)
        val file = File(dir, fileName)
        file.createNewFile()
        file.outputStream().write(json.toByteArray())
    }
}


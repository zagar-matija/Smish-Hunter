package ferit.student.matijazagar.smishhunter

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


object SMSChecker {

    private const val fileName  = "reports.json"
    private const val dirName  = "json"
    private const val CHANNEL_ID = "REPORT"
    private const val CHANNEL_NAME = "Smishing alerts"
    private const val NOTIF_ID = 0


    @RequiresApi(Build.VERSION_CODES.O)
    fun handleSMS(context: Context, sms : SmsMessage){
        val text = sms.displayMessageBody
        val sender = sms.originatingAddress
        var rating = context.resources.getString(R.string.level_harmless)
        var explanation = "Harmless message"

        val links = URLAnalyser.extractURLs(sms.displayMessageBody)

        links.forEach {
            val analysis = URLAnalyser.getLinkAnalysis(it)

            if(analysis.data.attributes.last_analysis_stats.malicious>=3){
                explanation = "Message contains a malicious link."
                rating = context.resources.getString(R.string.level_malicious)
            }
            else if(analysis.data.attributes.last_analysis_stats.suspicious>=3
                || analysis.data.attributes.last_analysis_stats.malicious>=1){
                explanation = "Message contains a suspicious link."
                rating = context.resources.getString(R.string.level_suspicious)
            }
        }

        val report = Report(sender,text,rating,explanation)
        //TODO if malicious/suspicious send notification

        if(getPreferenceValue(myPrefAdding,context)=="off"
            && rating==context.resources.getString(R.string.level_harmless))
            return

        if(getPreferenceValue(myPrefMalicious,context)=="on" && rating==context.resources.getString(R.string.level_malicious)
            ||getPreferenceValue(myPrefSuspicious,context)=="on" && rating==context.resources.getString(R.string.level_suspicious)){
            Log.d("Notif", "sending notif: "+report.explanation)
            createNotifChannel(context)
            generateNotification(report,context)
        }

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

    private fun getPreferenceValue(preference: String, context: Context ): String? {
        val sp: SharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        return sp.getString(preference, "on")
    }

    private fun generateNotification(report: Report, context: Context){
        val notif = NotificationCompat.Builder(context,CHANNEL_ID)
            .setContentTitle(report.explanation)
            .setContentText(report.content)//recimo prvih 20 znakova
            .setSmallIcon(R.drawable.ic_placeholder_logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()


        val notifManager = NotificationManagerCompat.from(context)

        notifManager.notify(NOTIF_ID,notif)

    }

    private fun createNotifChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.BLUE
                enableLights(true)
            }
            val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}


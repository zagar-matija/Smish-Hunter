package ferit.student.matijazagar.smishhunter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.util.Log
import androidx.annotation.RequiresApi


class SMSReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        if (!intent.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) return

        val extractMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        extractMessages.forEach { smsMessage ->
            SMSChecker.handleSMS(context, smsMessage)}
        extractMessages.forEach { smsMessage ->
            Log.d("SMSReceiver", smsMessage.displayMessageBody)}


    }
}
package ferit.student.matijazagar.smishhunter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log


class SMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (!intent.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) return

        val extractMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        extractMessages.forEach { smsMessage ->
            SMSChecker.handleSMS(context, smsMessage)}
        extractMessages.forEach { smsMessage ->
            Log.d("SMSReceiver", smsMessage.displayMessageBody)}


    }
}
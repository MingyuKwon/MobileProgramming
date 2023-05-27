package com.example.broadcastreciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyReceiver : BroadcastReceiver() {

    val pattern1 = Regex("""^\d{2}/\d{2} \d{2}:\d{2} \d{1,3}(,\d{3})*원$""")
    val scope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync() // 이걸 달고 있다면 강제 종료가 안된다

        scope.launch {
            if(intent.action.equals("android.provider.Telephony.SMS_RECEIVED")){
                val msg = Telephony.Sms.Intents.getMessagesFromIntent(intent) // 배열로 들어옴
//            for(smsMessage in message){
//                Log.i("msg", smsMessage.displayMessageBody)
//            }

                val message = msg[0].messageBody
                if(message.contains("건국카드")){
                    val tempstr = message.split("\n")

                    var result = false
                    for(str in tempstr.subList(1, tempstr.size)){
                        if(pattern1.containsMatchIn(str))
                        {
                            result = true
                            break
                        }
                    }

                    if(result)
                    {
                        val newIntent  = Intent(context, MainActivity::class.java)
                        newIntent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        // 실행 중이지 않을 때는 task하나 만들어서 실행, 기존에 top에 있었다면 , 실행 중이긴 한데 background(stack의 아래)에 있다면
                        newIntent.putExtra("msgSender", msg[0].originatingAddress)
                        newIntent.putExtra("msgBody", msg[0].messageBody)
                        context.startActivity(newIntent)
                    }

                }

            }
        }

        pendingResult.finish()
    }
}
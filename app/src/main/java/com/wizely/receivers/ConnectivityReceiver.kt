package com.wizely.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.wizely.services.SurveyUploadService
import com.wizely.utils.logi

class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        logi("connectivity changed")
        context.startService(Intent(context, SurveyUploadService::class.java))
    }
}

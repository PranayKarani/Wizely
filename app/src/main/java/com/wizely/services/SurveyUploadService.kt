package com.wizely.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.wizely.repos.SurveyRepo
import com.wizely.utils.logi

class SurveyUploadService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        SurveyRepo.uploadBatchedSurveys{
            stopSelf()
        }

        return START_NOT_STICKY

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}

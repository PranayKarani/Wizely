package com.wizely.repos

import android.annotation.SuppressLint
import android.os.AsyncTask
import com.google.gson.Gson
import com.wizely.MyApp
import com.wizely.entitiies.SimpleLocation
import com.wizely.entitiies.Survey
import com.wizely.utils.NetworkManager
import com.wizely.utils.logi
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import kotlin.collections.ArrayList

object SurveyRepo {

    private val gson = Gson()
    private val surveyFolder = File(MyApp.get().filesDir, "surveyFolder")
    private val uploadSurveysBatchFolder = File(MyApp.get().filesDir, "uploadBatch")

    init {
        if (!surveyFolder.exists()) {
            surveyFolder.mkdir()
        }
        if (!uploadSurveysBatchFolder.exists()) {
            uploadSurveysBatchFolder.mkdir()
        }
    }

    fun saveNewSurvey(survey: Survey) {

        survey.area = calculateArea(survey.locations)


        AsyncTask.execute {
            val json = gson.toJson(survey)

            val fileName = "${survey.name}_${Date().time}"
            val surveyFile = File(surveyFolder, fileName)
            if (surveyFile.createNewFile()) {
                val fileWriter = FileWriter(surveyFile)
                fileWriter.write(json)
                fileWriter.close()
            }

            if (NetworkManager.isConnected()) {
                SpreadsheetDatabase.uploadSurvey(survey)
            } else {

                val batchSurveyFile = File(uploadSurveysBatchFolder, fileName)
                if (batchSurveyFile.createNewFile()) {
                    val fileWriter = FileWriter(batchSurveyFile)
                    fileWriter.write(json)
                    fileWriter.close()
                }
            }

        }

    }

    @SuppressLint("StaticFieldLeak")
    fun getAllSurveys(fetchCallback: (ArrayList<Survey>?) -> Unit = {}) {

        val surveys = ArrayList<Survey>()

        val surveyFiles = surveyFolder.listFiles()

        for (file in surveyFiles) {
            val reader = FileReader(file)
            val builder = StringBuilder()
            reader.forEachLine {
                builder.append(it)
            }
            val survey = gson.fromJson(builder.toString(), Survey::class.java)
            surveys.add(survey)
        }

        fetchCallback(surveys)

    }

    fun uploadBatchedSurveys(uploadFinishCallback: () -> Unit = {}) {


        if (NetworkManager.isConnected()) {
            logi("start uploading")
            val files = uploadSurveysBatchFolder.listFiles()

            for (file in files) {
                val reader = FileReader(file)
                val builder = StringBuilder()
                reader.forEachLine {
                    builder.append(it)
                }
                val survey = gson.fromJson(builder.toString(), Survey::class.java)
                SpreadsheetDatabase.uploadSurvey(survey)
                file.delete()
            }
        }

        uploadFinishCallback()

    }

    private fun calculateArea(locations: List<SimpleLocation>): Double {

        var area = 0.0

        for (i in 0..locations.size - 2) {
            val l1 = locations[i]
            val l2 = locations[i + 1]
            val x = (l1.lat * l2.lng) - (l1.lng * l2.lat)
            area += x
        }

        return area

    }

}
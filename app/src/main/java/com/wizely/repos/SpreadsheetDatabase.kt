package com.wizely.repos

import android.accounts.Account
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.AsyncTask
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.SpreadsheetProperties
import com.wizely.MyApp
import com.wizely.R
import com.wizely.entitiies.Survey
import java.io.File
import java.io.InputStreamReader
import java.util.*


// Incomplete
object SpreadsheetDatabase {

    val SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS)
    val SPREADSHEET_ID = "1cHbZXP9ZxjcWESy12UsnliAFl0C1zFEKxCbNGCdSoJ4"
    val JSON_FACTORY = JacksonFactory.getDefaultInstance()
    val HTTP_TRANSPORT = NetHttpTransport()


    fun createNewSS() {

        AsyncTask.execute {
            val secrets = GoogleClientSecrets.load(
                JSON_FACTORY,
                InputStreamReader(MyApp.get().resources.openRawResource(R.raw.credentials))
            )

            val creds = GoogleAccountCredential.usingOAuth2(MyApp.get(), SCOPES)

            val service = Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, creds).build()

            var ss = Spreadsheet().setProperties(
                SpreadsheetProperties().setTitle("Surveys")
            )

            service.spreadsheets().create(ss)
                .setFields(SPREADSHEET_ID)
                .execute()
        }

    }

    fun uploadSurvey(survey: Survey) {

        // get user data from UserRepo

        // upload data to remote server from here

    }

}
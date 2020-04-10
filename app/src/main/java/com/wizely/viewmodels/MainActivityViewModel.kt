package com.wizely.viewmodels

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wizely.entitiies.SimpleLocation
import com.wizely.entitiies.Survey
import com.wizely.entitiies.User
import com.wizely.repos.SpreadsheetDatabase
import com.wizely.repos.SurveyRepo

class MainActivityViewModel : ViewModel() {

    lateinit var user: User
    var currentSurvey: Survey? = null
    val surveyLiveData = MutableLiveData<ArrayList<Survey>>()

    fun addLocation(location: Location) {

        val loc = SimpleLocation(location.latitude, location.longitude)

        currentSurvey?.locations?.add(loc)

    }

    fun loadSurveys() {

        SurveyRepo.getAllSurveys{
                surveyLiveData.value = it
        }

    }

    fun initSurvey(name: String) {
        currentSurvey = Survey(name)
    }

    fun saveSurvey() {

        surveyLiveData.value?.add(currentSurvey!!)
        surveyLiveData.value = surveyLiveData.value

        SurveyRepo.saveNewSurvey(currentSurvey!!)

        currentSurvey = null

    }

}
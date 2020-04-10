package com.wizely.views

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.wizely.R
import com.wizely.adapters.SurveyListAdapter
import com.wizely.databinding.ActivityMainBinding
import com.wizely.entitiies.Survey
import com.wizely.receivers.ConnectivityReceiver
import com.wizely.repos.UserRepo
import com.wizely.utils.logi
import com.wizely.viewmodels.MainActivityViewModel


class MainActivity : AppCompatActivity() {

    private val REQUEST_CHECK_SETTINGS = 24
    private val MY_PERMISSIONS_REQUEST_LOCATION = 32

    private lateinit var vm: MainActivityViewModel
    private lateinit var b: ActivityMainBinding
    private lateinit var adapter: SurveyListAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var handler: Handler

    private val DELAY = 3000L
    private val locationRunnable = object : Runnable {

        override fun run() {

            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    logi("lat ${it.latitude} & lng: ${it.longitude}")
                    vm.addLocation(it)
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }

            handler.postDelayed(this, DELAY)

        }

    }

    val surveyListObserver = Observer<ArrayList<Survey>> {
        adapter.surveys = it
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView(this, R.layout.activity_main)

        vm = ViewModelProvider(this)[MainActivityViewModel::class.java]

        verifyUserSignIn()

        adapter = SurveyListAdapter(this)
        vm.surveyLiveData.observe(this, surveyListObserver)

        vm.loadSurveys()

        b.surveyList.adapter = adapter
        handler = Handler()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        try {
            registerReceiver(ConnectivityReceiver(), filter)
        }catch (e: Exception){

        }

    }

    private fun verifyUserSignIn() {

        val user = UserRepo.getUserData()
        if (user == null) {
            startActivity(Intent(this, SignInActivity::class.java))
        } else {
            vm.user = user
        }

    }

    public fun onAddSurveyBtnClick(v: View) {

        checkForLocationPermission {
            checkForLocationSettings {
                showNewSurveyDialog()
            }
        }
    }

    public fun onSurveyStopPressed(v: View) {

        b.stopSurveyBtn.visibility = View.GONE
        b.startSurveyFab.visibility = View.VISIBLE
        handler.removeCallbacks(locationRunnable)

        vm.saveSurvey()

    }

    private fun showNewSurveyDialog() {


        val view = layoutInflater.inflate(R.layout.add_survey_dialog, null)
        AlertDialog.Builder(this)
            .setCancelable(true)
            .setView(view)
            .setPositiveButton("start") { _, _ ->
                b.startSurveyFab.visibility = View.GONE
                b.stopSurveyBtn.visibility = View.VISIBLE
                val name = view.findViewById<EditText>(R.id.new_survey_name).text.toString()
                vm.initSurvey(name)
                handler.post(locationRunnable)

            }
            .create()
            .show()
    }

    private fun checkForLocationPermission(onGranted: () -> Unit = {}) {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
        } else {
            onGranted()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {

            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                showAlert("Location permission is required to detect nearby places.") {
                    requestLocationPermission()
                }

            }

        }

    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    private fun checkForLocationSettings(onSuccess: () -> Unit = {}) {

        val request =
            LocationSettingsRequest.Builder().addLocationRequest(getLocationRequest()).build()

        LocationServices.getSettingsClient(this).checkLocationSettings(request)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                if (it is ResolvableApiException) {
                    try {
                        it.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                    } catch (ignore: SendIntentException) {
                    }
                }
            }


    }

    private fun getLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        return locationRequest
    }


    private fun showAlert(message: String?, cancelable: Boolean = true, onClick: () -> Unit = {}) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK")
            { dialog, which -> onClick() }
            .setCancelable(cancelable)
            .create()
            .show()
    }

}

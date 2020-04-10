package com.wizely

import android.app.Application

class MyApp : Application() {

    companion object {
        private lateinit var myapp: MyApp
        fun get() = myapp
    }

    override fun onCreate() {
        super.onCreate()

        myapp = this

    }

}
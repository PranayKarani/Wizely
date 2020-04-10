package com.wizely.repos

import com.wizely.entitiies.User
import com.wizely.utils.readFromPrefs
import com.wizely.utils.writeToPrefs

object UserRepo {

    private val PREF_KEY_USERNAME = "username"
    private val PREF_KEY_PASSWORD = "password"

    public fun getUserData() : User? {

        val userName = readFromPrefs(PREF_KEY_USERNAME)
        val userPassword = readFromPrefs(PREF_KEY_PASSWORD)

        if (userName != null && userName.isNotEmpty()) {

            return User(userName, userPassword)

        }

        return null
    }

    public fun saveUserData(username: String, password: String) {

        writeToPrefs(PREF_KEY_USERNAME, username)
        writeToPrefs(PREF_KEY_PASSWORD, password)

    }

}
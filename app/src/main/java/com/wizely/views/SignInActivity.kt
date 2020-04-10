package com.wizely.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.wizely.R
import com.wizely.databinding.ActivitySignInBinding
import com.wizely.repos.UserRepo
import com.wizely.utils.NetworkManager

class SignInActivity : AppCompatActivity() {

    lateinit var b: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
    }

    public fun onSignUpPress(v: View) {

        if (!NetworkManager.isConnected()) {
            Toast.makeText(this, "Connect to the internet.", Toast.LENGTH_LONG).show()
            return
        }

        val username = b.username.text.toString()
        val password = b.password.text.toString()

        val validationResult = validateUserInput(username, password)

        if (validationResult == null) {
            UserRepo.saveUserData(username, password)
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Toast.makeText(this, validationResult, Toast.LENGTH_LONG).show()
        }

    }

    public fun onSignInPress(v: View) {

        if (!NetworkManager.isConnected()) {
            Toast.makeText(this, "Connect to the internet.", Toast.LENGTH_LONG).show()
            return
        }

        val user = UserRepo.getUserData()

        if (user != null) {
            val username = b.username.text.toString()
            val password = b.password.text.toString()

            val validationResult = validateUserInput(username, password)

            if (validationResult == null) {
                if (username == user.userName && password == user.password) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, "Incorrect Details.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, validationResult, Toast.LENGTH_LONG).show()
            }

        } else {

            Toast.makeText(this, "No user found. Please Sign up.", Toast.LENGTH_LONG).show()

        }

    }

    private fun validateUserInput(username: String, password: String): String? {
        if (username.isEmpty()) {
            return "Username is empty"
        }
        if (password.isEmpty()) {
            return "Password is empty"
        }

        return null
    }

}

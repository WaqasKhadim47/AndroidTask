package com.example.androidproject.Authentication

import SignInModel
import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.androidproject.Api.ApiInterface
import com.example.androidproject.MainActivity
import com.example.androidproject.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginScreen : AppCompatActivity(), View.OnClickListener {
    val BASE_URL = "https://red-rock.quickxi-food.invo.zone/api/";

    lateinit var signInButton: CardView
    lateinit var signUpButton: TextView
    lateinit var phoneNumberField: TextInputEditText
    lateinit var passwordField: TextInputEditText
    lateinit var progressBar: ProgressBar
    private val NOTIFICATION_PERMISSION_CODE = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        // hide appbar
        supportActionBar?.hide();

        println(
            "Firebase Data ===> ${FirebaseApp.getInstance().toString()}"
        )

        // linking resources
        signInButton = findViewById(R.id.signInBtn)
        signUpButton = findViewById(R.id.signUpBtn)
        phoneNumberField = findViewById(R.id.phoneTextField)
        passwordField = findViewById(R.id.passwordTextField)
        progressBar = findViewById(R.id.pBar)

        signUpButton.setOnClickListener(this)
        signInButton.setOnClickListener(this)

        requestNotificationPermission()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            println("Fetching FCM registration token: $token")
            //Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })

        signUpButton.paintFlags = signUpButton.paintFlags or Paint.UNDERLINE_TEXT_FLAG

    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY
            ) == PackageManager.PERMISSION_GRANTED
        ) return
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY
            )
        ) {
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY),
            NOTIFICATION_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Checking the request code of our request
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {

            // If permission is granted
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Displaying a toast
                Toast.makeText(
                    this,
                    "Permission granted now you can read the storage",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }


    fun signIn() {
        if (phoneNumberField.text!!.trim().isEmpty()) {
            phoneNumberField.error = "Please enter a phone number"
            phoneNumberField.requestFocus();
        } else if (passwordField.text!!.trim().isEmpty()) {
            passwordField.error = "Please enter a password"
            passwordField.requestFocus()
        } else {
            signInButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            val retrofitBuilder = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)

            val response = retrofitBuilder.login(
                "+" + phoneNumberField.text!!.trim().toString(),
                passwordField.text!!.trim().toString(),
                "user"
            )

            response.enqueue(object : Callback<SignInModel> {
                override fun onResponse(call: Call<SignInModel>, response: Response<SignInModel>) {
                    println(response.body())
                    if (response.isSuccessful && response.body()!!.code == "SUCCESS") {
                        signInButton.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        val intent = Intent(this@LoginScreen, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        signInButton.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@LoginScreen,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<SignInModel>, t: Throwable) {

                    signInButton.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
            )
        }

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.signInBtn -> signIn()
            R.id.signUpBtn -> {
                val intent = Intent(this, SignupScreen::class.java)
                this.startActivity(intent)
            }
        }
    }


}
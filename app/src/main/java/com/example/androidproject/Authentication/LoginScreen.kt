package com.example.androidproject.Authentication

import SignInModel
import android.Manifest
import android.app.PendingIntent
import android.app.ProgressDialog.show
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
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
import com.example.androidproject.MessageShowScreen
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


    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) return
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
        ) {
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            NOTIFICATION_PERMISSION_CODE
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        // hide appbar
        supportActionBar?.hide();

        println(
            "Firebase Data ===> ${FirebaseApp.getInstance().toString()}"
        )

        if (intent.hasExtra("greeting")) {
            val notificationIntent = Intent(this@LoginScreen, MessageShowScreen::class.java).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val list = arrayListOf<String>()
            for (key in intent?.extras?.keySet()!!) {
                intent?.extras?.getString(key)?.let { list.add(it) }
            }
            notificationIntent.putExtra("notificationData", list)
            startActivity(notificationIntent)
        }

        // linking resources
        signInButton = findViewById(R.id.signInBtn)
        signUpButton = findViewById(R.id.signUpBtn)
        phoneNumberField = findViewById(R.id.phoneTextField)
        passwordField = findViewById(R.id.passwordTextField)
        progressBar = findViewById(R.id.pBar)

        signUpButton.setOnClickListener(this)
        signInButton.setOnClickListener(this)



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


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            println("Permission Granted")
        } else {
            // Permission is missing and must be requested.
            requestNotificationPermission()
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        println("Working is function ====> ")
        // Checking the request code of our request
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {

            // If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Displaying a toast

            } else {
                // Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG)
                    .show()
                requestNotificationPermission()
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
                override fun onResponse(
                    call: Call<SignInModel>,
                    response: Response<SignInModel>
                ) {
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val notificationIntent = Intent(this@LoginScreen, MessageShowScreen::class.java).apply {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val list = arrayListOf<String>()
        for (key in intent?.extras?.keySet()!!) {
            intent?.extras?.getString(key)?.let { list.add(it) }
        }
        notificationIntent.putExtra("notificationData", list)
        startActivity(notificationIntent)

        // testing
        println("Value ===> ${intent?.extras?.getString("greeting1")}")

        println("Number of extras ===> ${intent?.extras?.keySet()?.size}")
        Toast.makeText(this, "Notification Received", Toast.LENGTH_LONG)
            .show()

    }

    private fun getIntentData(intent: Intent?) {
        if (null != intent) {
            // Obtain the value in data.
            val bundle = intent.extras
            if (bundle != null) {
                for (key in bundle.keySet()) {
                    val content = bundle.getString(key)
                    Log.i(TAG, "receive data from push, key = $key, content = $content")
                }
            }
        } else {
            Log.i(TAG, "intent is null")
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
package com.example.androidproject.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.androidproject.Api.ApiInterface
import com.example.androidproject.Authentication.models.SignUpResponse
import com.example.androidproject.Authentication.models.VerifyUserResponse
import com.example.androidproject.R
import com.example.androidproject.User
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class SignupScreen : AppCompatActivity() , View.OnClickListener{

    lateinit var userNameField: TextInputEditText
    lateinit var passwordField: TextInputEditText
    lateinit var emailField: TextInputEditText
    lateinit var phoneField: TextInputEditText
    lateinit var signUpBtn : CardView
    val BASE_URL = "https://red-rock.quickxi-food.invo.zone/api/";
    lateinit var progressBar : ProgressBar
    lateinit var imageBtn : ImageButton




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_screen)

        supportActionBar?.hide();

        // linking resources

        userNameField = findViewById(R.id.userNameField)
        passwordField = findViewById(R.id.passwordTextField)
        emailField = findViewById(R.id.emailTextFIeld)
        phoneField = findViewById(R.id.phoneTextField)
        signUpBtn = findViewById(R.id.signUpBtn)
        progressBar = findViewById(R.id.pBar)
        imageBtn = findViewById(R.id.backBtn)


        // attach listeners
        signUpBtn.setOnClickListener(this)
        imageBtn.setOnClickListener(this)


    }

    fun signUp(): Boolean {
        println("run this one");
        if(userNameField.text!!.trim().isEmpty()){
            userNameField.error = "Please enter user name";
            userNameField.requestFocus();
        }else if (passwordField.text!!.trim().isEmpty()){
            passwordField.error = "Please enter password";
            passwordField.requestFocus();
        }else if (emailField.text!!.trim().isEmpty()){
            emailField.error ="Please enter email address";
            emailField.requestFocus();
        }else if (phoneField.text!!.trim()?.isEmpty()){
            phoneField.error = "Please enter phone number";
            phoneField.requestFocus();
        }else {
            signUpBtn.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            val retrofitBuilrder = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)

            val user : User

            user = User(category = "user", name = userNameField.text?.trim().toString(), password = passwordField.text?.trim().toString(), device = "android", device_type = "ios");
            val response =  retrofitBuilrder.signUp(user);
            response.enqueue(object: Callback<SignUpResponse>{
                override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                    if(response.isSuccessful && response.body()?.code =="SUCCESS"){
                        verifyUser(response.body()!!.data.id.toString())
                    }
                }

                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    signUpBtn.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    Toast.makeText(this@SignupScreen,"Something went wrong", Toast.LENGTH_SHORT).show()
                }

            })

        }
        return false;
    }


    fun verifyUser(userId: String){
        val retrofitBuilrder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
        val mobileNumber = phoneField.text?.trim().toString();
        val response =  retrofitBuilrder.verifyUser( "${userId}", "${mobileNumber}", "+92",true );
        response.enqueue(object : Callback<VerifyUserResponse>{
            override fun onResponse(
                call: Call<VerifyUserResponse>,
                response: Response<VerifyUserResponse>
            ) {

                println(response.body());
                if(response.isSuccessful && response.body()?.code =="SUCCESS"){
                    signUpBtn.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE

                    Toast.makeText(this@SignupScreen,"Signup Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SignupScreen,LoginScreen::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<VerifyUserResponse>, t: Throwable) {
                signUpBtn.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }

        })
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.signUpBtn -> signUp()
            R.id.backBtn -> finish()
        }
    }


}
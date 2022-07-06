package com.example.androidproject.Api

import SignInModel
import com.example.androidproject.Authentication.models.SignUpResponse
import com.example.androidproject.Authentication.models.VerifyUserResponse
import com.example.androidproject.User
import com.example.androidproject.UserDataItem
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    //https://red-rock.quickxi-food.invo.zone/api/

    @POST("registration")
    fun signUp(@Body user : User): Call<SignUpResponse>

    @FormUrlEncoded
    @POST("verify_verificationcode")
    fun verifyUser(@Field("user_id") userId: String, @Field("mobile_number") mobileNumber: String, @Field("country_code") country_code: String, @Field("isverified") isverified : Boolean): Call<VerifyUserResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("mobilenumber") mobilenumber: String, @Field("password") password: String, @Field("category") category: String): Call<SignInModel>

    @GET("/posts")
    fun getPosts(): Call<List<UserDataItem>>
}
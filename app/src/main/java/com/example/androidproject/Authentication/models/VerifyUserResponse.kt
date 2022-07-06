package com.example.androidproject.Authentication.models

data class VerifyUserData(
    val category: String,
    val country_code: String,
    val created_at: String,
    val email: Any,
    val email_verified_at: Any,
    val id: Int,
    val image: String,
    val isverified: Int,
    val kitchen_id: Int,
    val lat: Int,
    val licence_image: Any,
    val licence_number: Any,
    val lng: Int,
    val mobile_number: Long,
    val mobilenumber: String,
    val name: String,
    val status: String,
    val subdomain: String,
    val token: String,
    val updated_at: String,
    val vendor_id: Int
)


data class DataX(
    val count: Int,
    val `data`: VerifyUserData
)

data class VerifyUserResponse(
    val code: String,
    val `data`: DataX,
    val message: String
)
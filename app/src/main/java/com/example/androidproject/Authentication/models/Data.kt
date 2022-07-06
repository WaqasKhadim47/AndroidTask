package com.example.androidproject.Authentication.models

data class Data(
    val category: String,
    val created_at: String,
    val email: Any,
    val id: Int,
    val isverified: Int,
    val name: String,
    val status: Int,
    val updated_at: String,
    val vendor_id: Int,
    val verificationCode: Int
)
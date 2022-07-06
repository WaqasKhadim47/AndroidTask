package com.example.androidproject.Authentication.models

import com.example.androidproject.Authentication.models.Data

data class SignUpResponse(
    val code: String,
    val `data`: Data,
    val message: String
)
package com.example.storyapp__subs1.data.preferensi

data class Model(
    val name: String,
    val userId: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)
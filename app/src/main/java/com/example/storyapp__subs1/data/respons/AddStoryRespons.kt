package com.example.storyapp__subs1.data.respons

import com.google.gson.annotations.SerializedName

data class AddStoryRespons(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

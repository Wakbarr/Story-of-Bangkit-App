package com.example.storyapp__subs1.ui.story

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryModel (
    val photoUrl: String,
    val name: String,
    val desc: String,
): Parcelable

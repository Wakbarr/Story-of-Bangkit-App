package com.example.storyapp.view.utils

import androidx.test.espresso.idling.CountingIdlingResource

object LoginIdlingResource {
    private const val RESOURCE = "LOGIN"
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        countingIdlingResource.decrement()
    }
}
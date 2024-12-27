package com.example.storyapp__subs1

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.storyapp.view.utils.LoginIdlingResource
import com.example.storyapp__subs1.ui.auth.login.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(LoginIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(LoginIdlingResource.countingIdlingResource)
    }

    @Test
    fun loginSuccessTest() {
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.ed_login_email))
            .perform(scrollTo(), typeText("user@example.com"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password))
            .perform(scrollTo(), typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).perform(scrollTo(), click())

        onView(withId(R.id.main)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun loginFailTest() {
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.ed_login_email)).perform(scrollTo(), typeText("user@example.com"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password)).perform(scrollTo(), typeText("wrongpassword"), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).perform(scrollTo(), click())


        activityScenario.close()
    }
}

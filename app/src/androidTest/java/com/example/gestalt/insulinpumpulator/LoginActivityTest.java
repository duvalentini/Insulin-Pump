package com.example.gestalt.insulinpumpulator;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testGPlusLogin() {
        //call action on part of layout
        onView(withId(R.id.g_login_button)).perform(click());

        //assert view is as it should be
        onView(withId(R.id.main_page_title)).check(matches(isDisplayed()));
        onView(withId(R.id.main_page_title)).check(matches(withText("Welcome!")));
        onView(withId(R.id.insulin_pump_image)).check(matches(isDisplayed()));
        onView(withId(R.id.bCustomize)).check(matches(isDisplayed()));
        //once text is finalized, check text as well
        onView(withId(R.id.bAccountInfo)).check(matches(isDisplayed()));
        //once text is finalized, check text as well
        onView(withId(R.id.bConnections)).check(matches(isDisplayed()));
        //once text is finalized, check text as well
        onView(withId(R.id.scenario_select)).check(matches(isDisplayed()));
        //once text is finalized, check text as well
    }

    @Test
    public void testRegistrationButton() {
        //call action on part of layout
        onView(withId(R.id.g_login_button)).perform(click());
    }
}
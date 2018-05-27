package com.udacity.gradle.builditbigger;

import android.os.SystemClock;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * MainActivityTest
 */
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {

        ViewInteraction sendButton = onView(
                allOf(withId(R.id.bn_tell_joke), withText("Tell Joke"), isDisplayed()));
        sendButton.perform(click());

        // Sleep for a 1 second to receive a response and update R.id.response_view
        SystemClock.sleep(1000);

        onView(withId(R.id.tv_joke)).check(matches(not(withText(""))));

    }

}

package com.example.android.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityBasicTest {

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    // Testing if the data retrieved from the internet is valid
    @Test
    public void testDataRetrieved() {

        onView(withId(R.id.rvRecipes)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText("Brownies")).check(matches(isDisplayed()));
    }

    // Test to try a simple interaction with all the activities
    @Test
    public void testRecipeClick() {

        onView(withId(R.id.rvRecipes))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(1, click()));

        onView(withId(R.id.rvIngredients))
                .check(matches(isDisplayed()));

        onView(withId(R.id.rvSteps))
                .check(matches(isDisplayed()));

        onView(withId(R.id.rvSteps))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(1, click()));

        onView(withId(R.id.step_id))
                .check(matches(isDisplayed()));

        onView(withId(R.id.step_next_button))
                .perform(click());

        onView(withId(R.id.step_id))
                .check(matches(isDisplayed()));

        onView(withId(R.id.step_previous_button))
                .perform(click());

        onView(withId(R.id.step_id))
                .check(matches(isDisplayed()));
    }

        @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}

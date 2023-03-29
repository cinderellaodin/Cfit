package com.odin.cfit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FoodLogActivityTest {

    @Rule
    public ActivityScenarioRule<FoodLogActivity> activityScenarioRule =
            new ActivityScenarioRule<>(FoodLogActivity.class);

    @Before
    public void setUp() {
        // do any setup here
    }

    @Test
    public void testDialogOpens() {
        // click "Show Dialog" button
        onView(ViewMatchers.withId(R.id.fab_food_log)).perform(ViewActions.click());

        // check that dialog is displayed
        onView(withText("Food Diary Entry"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }


    @Rule
    public ActivityScenarioRule<FoodLogActivity> activityRule = new ActivityScenarioRule<>(FoodLogActivity.class);

    @Test
    public void testEnterFoodDetails() {
        onView(withId(R.id.fab_food_log)).perform(click());

        onView(withId(R.id.spinnerFoodType)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is("Breakfast"))).perform(click());
        onView(withId(R.id.etfood)).perform(typeText(" "));

        onView(withId(R.id.btnsaveinfo)).perform(click());

       // Espresso.onView(withText("Food Added Successfully")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }


  /*  @Test
    public void testDialogInput() {
        ActivityScenario<FoodLogActivity> activityScenario = ActivityScenario.launch(FoodLogActivity.class);
        String expectedFoodName ="breakfast ";
        // show dialog
        Espresso.onView(ViewMatchers.withId(R.id.fab_food_log)).perform(ViewActions.click());

        // find input view and enter text
        Espresso.onView(ViewMatchers.withId(R.id.spinnerFoodType))
                .perform(typeText(expectedFoodName));

        // click OK button
        Espresso.onView(withText("Save"))
                .inRoot(isDialog())
                .perform(click());

        // check that name is displayed in main activity
        Espresso.onView(ViewMatchers.withId(R.id.label))
                .check(matches(isDisplayed()))
                .check(matches(withText("Food Diary Entry ")));
    }*/


 /*   @Test
    public void testAddFoodItem() {
        // Click the "Add Food" button
        onView(withId(R.id.btnAddFood)).perform(click());

        // Enter food details into the dialog
        onView(withId(R.id.etFoodName)).perform(typeText("Pizza"));
        onView(withId(R.id.etFoodCalories)).perform(typeText("500"));
        onView(withId(R.id.btnSubmit)).perform(click());

        // Verify that the toast message appears
        onView(withText("Food Added Successfully")).inRoot(withDecorView(not(activity.getWindow().getDecorView()))).check(matches(isDisplayed()));
    }*/

}

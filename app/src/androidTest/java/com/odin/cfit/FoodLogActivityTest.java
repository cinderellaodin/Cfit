package com.odin.cfit;

import static android.icu.lang.UProperty.NAME;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.annotation.ContentView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FoodLogActivityTest {
    @Rule
    ActivityScenario activityScenario = ActivityScenario.launch(FoodLogActivity.class);
    String expectedFoodName ="lunch";



    @Test
    public void showDialogAndCaptureFoodDetails() {
        //Given

        // Execute and Verify
        Espresso.onView(withId(R.id.btnsaveinfo)).perform(click());

        Espresso.onView(withId(R.id.etfood));

        //onView(withText(R.string.text_ok)).perform(click())

        // make sure dialog is still visible (can't click ok without entering a name)
        Espresso.onView(withId(R.id.etfood)).check(matches(isDisplayed()));

        // enter a name
    /*    Espresso.onView(withId(R.id.etfood)).perform(typeText(expectedFoodName));

        Espresso.onView(withText(R.string.text_ok)).perform(click());

        // make sure dialog is gone
        Espresso.onView(withText(R.string.text_enter_name)).check(doesNotExist());

        Espresso.onView(withId(R.id.text_name)).check(matches(withText(expectedFoodName)));
*/
    }

    @Test
    public void testDialogInput() {
        // show dialog
        Espresso.onView(ViewMatchers.withId(R.id.fab_food_log)).perform(ViewActions.click());

        // find input view and enter text
        Espresso.onView(ViewMatchers.withId(R.id.etfood))
                .perform(typeText(expectedFoodName));

        // click OK button
        Espresso.onView(withText("OK"))
                .inRoot(isDialog())
                .perform(click());

        // check that name is displayed in main activity
        Espresso.onView(ViewMatchers.withId(R.id.etfood))
                .check(matches(isDisplayed()))
                .check(matches(withText("Egg and Spinach")));
    }
}

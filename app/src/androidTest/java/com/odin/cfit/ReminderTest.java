package com.odin.cfit;

import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ReminderTest {

    @Rule
    public ActivityScenarioRule<AddReminderActivity> mActivityRule =
            new ActivityScenarioRule<>(AddReminderActivity.class);

  //  ActivityScenario<AddReminderActivity> scenario = ActivityScenario.launch(AddReminderActivity.class);

    @Test
    public void saveReminderTest() {

        // Type reminder title
        onView(withId(R.id.reminder_title))
                .perform(typeText("Test Reminder Title"), closeSoftKeyboard());

       // Click the date button
        onView(withId(R.id.set_date)).perform(click());

        // Select a date
        onView(withClassName(equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2023, 4, 1));
        onView(withId(android.R.id.button1)).perform(click());

        // Click the time button
        onView(withId(R.id.set_time)).perform(click());

        // Select a time
        onView(withClassName(equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(8, 30));
        onView(withId(android.R.id.button1)).perform(click());
        // Click the repeat checkbox
        onView(withId(R.id.set_repeat)).perform(click());

        // Type repeat interval
       /* onView(withId(R.id.set_repeat_no))
                .perform(typeText("1"), closeSoftKeyboard());*/

        // Select repeat interval type
       /* onView(withId(R.id.set_repeat_type)).perform(click());
        onView(withText("hour")).perform(click());*/
        // Click the save button
        onView(withId(R.id.save_reminder)).perform(click());

        // Check if a reminder was saved

    /*    Espresso.onView(ViewMatchers.withId(R.id.label))
                .check(matches(isDisplayed()))
                .check(matches(withText("Reminder saved")));*/


       /* Espresso.onView(withText("Reminder saved"))
                .inRoot(withDecorView(not(mActivityRule.getScenario().getWindow().getDecorView())))
                .check(matches(isDisplayed()));*/
    }

    @Test
    public void testSaveReminderValidation() {
        // Enter an invalid title (empty string)
        onView(withId(R.id.reminder_title)).perform(typeText(""));
        onView(withId(R.id.save_reminder)).perform(click());
        // Check if error message is displayed
        onView(withText(R.string.error_title_empty)).check(matches(isDisplayed()));

        // Enter a valid title
        onView(withId(R.id.reminder_title)).perform(typeText("Reminder Title"));
        // Select a date and time
        onView(withId(R.id.set_date)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023, 3, 25));
        onView(withText(android.R.string.ok)).perform(click());
        onView(withId(R.id.set_time)).perform(click());
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(10, 30));
        onView(withText(android.R.string.ok)).perform(click());
        // Save the reminder
        onView(withId(R.id.save_reminder)).perform(click());
        // Check if success message is displayed
        onView(withText(R.string.save_reminder)).check(matches(isDisplayed()));
    }

}

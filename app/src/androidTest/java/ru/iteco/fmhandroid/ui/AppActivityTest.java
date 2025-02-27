package ru.iteco.fmhandroid.ui;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

import static ru.iteco.fmhandroid.ui.Helper.waitDisplayed;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.iteco.fmhandroid.R;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AppActivityTest {

//    @Rule
//    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
//            new ActivityScenarioRule<>(AppActivity.class);
    @Rule
    public ActivityTestRule<AppActivity> activityRule =
            new ActivityTestRule<>(AppActivity.class);

    @Test
    public void appActivityTest() {

        onView(isRoot()).perform(waitDisplayed(R.id.login_text_input_layout, 10_000));
        ViewInteraction textInputEditText = onView(withId(R.id.login_text_input_layout));
        textInputEditText.check(matches(isDisplayed()));
        textInputEditText.perform(replaceText("login2"), closeSoftKeyboard());
    }

}

package ru.iteco.fmhandroid.ui;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsEqual.equalTo;

import android.widget.Toast;

import static ru.iteco.fmhandroid.ui.Helper.waitDisplayed;
import static ru.iteco.fmhandroid.ui.Helper.waitDisplayedByText;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.iteco.fmhandroid.R;

@LargeTest
@RunWith(AndroidJUnit4.class)

public class AppActivityAuthorizationTest {

    @Rule
    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);
//    @Rule
//    public ActivityTestRule<AppActivity> activityRule =
//            new ActivityTestRule<>(AppActivity.class);
    int waitingPeriod = 20_000;
    String validLogin = "login2";
    String validPassword = "password2";
    String invalidPassword = "password";
    String textOf_nav_host_fragment = "Authorization";
    String hintOf_login_text_input_layout = "Login";
    String hintOf_password_text_input_layout = "Password";
    String textOf_container_list_news_include_on_fragment_main = "News";
    String descriptionOf_authorization_image_button = "Authorization";
    String textOf_title = "Log out";

    @Test
    //1.1 Авторизация с валидными логином и паролем зарегистрированного пользователя
    public void ValidAuthorizationTest() throws InterruptedException {
        //ждем, пока на странице появится кнопка "SIGN IN"
        onView(isRoot()).perform(waitDisplayed(R.id.enter_button, waitingPeriod));
        //убеждаемся, что на странице есть фрагмент с текстом textOf_nav_host_fragment
        ViewInteraction nameAuthText = onView(allOf(withText(textOf_nav_host_fragment), withParent(withParent(withId(R.id.nav_host_fragment)))));
        nameAuthText.check(matches(isDisplayed()));
        //вводим логин
        ViewInteraction loginText = onView(allOf(withHint(hintOf_login_text_input_layout), withParent(withParent(withId(R.id.login_text_input_layout)))));
        loginText.check(matches(isDisplayed()));
        loginText.perform(replaceText(validLogin), closeSoftKeyboard());
        //вводим пароль
        ViewInteraction passwordText = onView(allOf(withHint(hintOf_password_text_input_layout), withParent(withParent(withId(R.id.password_text_input_layout)))));
        passwordText.check(matches(isDisplayed()));
        passwordText.perform(replaceText(validPassword), closeSoftKeyboard());
        //жмем кнопку "SIGN IN"
        ViewInteraction materialButton = onView(withId(R.id.enter_button));
        materialButton.perform(click());
        //ждем, пока на странице появится контейнер с новостями
        onView(isRoot()).perform(waitDisplayed(R.id.container_list_news_include_on_fragment_main, waitingPeriod));
        //убеждаемся, что на странице есть фрагмент с текстом textOf_container_list_news_include_on_fragment_main
        ViewInteraction nameNewsText = onView(allOf(withText(textOf_container_list_news_include_on_fragment_main), withParent(withParent(withId(R.id.container_list_news_include_on_fragment_main)))));
        nameNewsText.check(matches(isDisplayed()));
        //жмем на кнопку авторизации
        ViewInteraction appCompatImageButton = onView(allOf(withId(R.id.authorization_image_button), withContentDescription(descriptionOf_authorization_image_button)));
        appCompatImageButton.check(matches(isDisplayed()));
        appCompatImageButton.perform(click());
        //жмем на "Log out"
        ViewInteraction materialTextView = onView(allOf(withId(android.R.id.title), withText(textOf_title)));
        materialTextView.check(matches(isDisplayed()));
        materialTextView.perform(click());

        //ждем, пока на странице появится кнопка "SIGN IN"
        onView(isRoot()).perform(waitDisplayed(R.id.enter_button, waitingPeriod));
        //убеждаемся, что на странице есть фрагмент с текстом "textOf_nav_host_fragment
        ViewInteraction nameAuthText2 = onView(allOf(withText(textOf_nav_host_fragment), withParent(withParent(withId(R.id.nav_host_fragment)))));
        nameAuthText2.check(matches(isDisplayed()));

        //Thread.sleep(50_000);
    }

    @Test
    //1.2 Авторизация с валидными логином и паролем незарегистрированного пользователя
    public void InvalidPasswordAuthorizationTest() throws InterruptedException {

        //ждем, пока на странице появится кнопка "SIGN IN"
        onView(isRoot()).perform(waitDisplayed(R.id.enter_button, waitingPeriod));
        //убеждаемся, что на странице есть фрагмент с текстом textOf_nav_host_fragment
        ViewInteraction nameAuthText = onView(allOf(withText(textOf_nav_host_fragment), withParent(withParent(withId(R.id.nav_host_fragment)))));
        nameAuthText.check(matches(isDisplayed()));
        //вводим логин
        ViewInteraction loginText = onView(allOf(withHint(hintOf_login_text_input_layout), withParent(withParent(withId(R.id.login_text_input_layout)))));
        loginText.check(matches(isDisplayed()));
        loginText.perform(replaceText(validLogin), closeSoftKeyboard());
        //вводим пароль
        ViewInteraction passwordText = onView(allOf(withHint(hintOf_password_text_input_layout), withParent(withParent(withId(R.id.password_text_input_layout)))));
        passwordText.check(matches(isDisplayed()));
        passwordText.perform(replaceText(invalidPassword), closeSoftKeyboard());
        //жмем кнопку "SIGN IN"
        ViewInteraction materialButton = onView(withId(R.id.enter_button));
        materialButton.perform(click());

        //так не сможем проверить, т.к. сообщение об ошибке выводится в Toast
        // onView(isRoot()).perform(waitDisplayedByText(String.valueOf(R.string.error), waitingPeriod));
       
        //убедимся, что не ушли со страницы
       //ждем, пока на странице появится кнопка "SIGN IN"
        onView(isRoot()).perform(waitDisplayed(R.id.enter_button, waitingPeriod));
        //убеждаемся, что на странице есть фрагмент с текстом "textOf_nav_host_fragment
        ViewInteraction nameAuthText2 = onView(allOf(withText(textOf_nav_host_fragment), withParent(withParent(withId(R.id.nav_host_fragment)))));
        nameAuthText2.check(matches(isDisplayed()));

    }
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

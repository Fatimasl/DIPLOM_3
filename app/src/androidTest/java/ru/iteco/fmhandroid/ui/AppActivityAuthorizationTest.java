package ru.iteco.fmhandroid.ui;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


import static ru.iteco.fmhandroid.ui.Helper.attemptOfAuthorization;
import static ru.iteco.fmhandroid.ui.Helper.invalidLogin;
import static ru.iteco.fmhandroid.ui.Helper.invalidPassword;
import static ru.iteco.fmhandroid.ui.Helper.makeRelogin;
import static ru.iteco.fmhandroid.ui.Helper.registeredLogin;
import static ru.iteco.fmhandroid.ui.Helper.registeredPassword;
import static ru.iteco.fmhandroid.ui.Helper.textOf_container_list_news_include_on_fragment_main;
import static ru.iteco.fmhandroid.ui.Helper.textOf_nav_host_fragment;
import static ru.iteco.fmhandroid.ui.Helper.unregisteredLogin;
import static ru.iteco.fmhandroid.ui.Helper.unregisteredPassword;
import static ru.iteco.fmhandroid.ui.Helper.waitDisplayed;
import static ru.iteco.fmhandroid.ui.Helper.waitingPeriod;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.Before;
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
    private static View decorView;

    @Before
    public void setUp() {
        mActivityScenarioRule.getScenario().onActivity(new ActivityScenario.ActivityAction<AppActivity>() {
            @Override
            public void perform(AppActivity activity) {
                decorView = activity.getWindow().getDecorView();
            }
        });
    }

    @Test
    //1.1 Авторизация с валидными логином и паролем зарегистрированного пользователя
    public void ValidAuthorizationTest() throws InterruptedException {
        attemptOfAuthorization(registeredLogin, registeredPassword);

        //ждем, пока на странице появится контейнер с новостями
        onView(isRoot()).perform(waitDisplayed(R.id.container_list_news_include_on_fragment_main, waitingPeriod));
        //убеждаемся, что на странице есть фрагмент с текстом textOf_container_list_news_include_on_fragment_main
        ViewInteraction nameNewsText = onView(allOf(withText(textOf_container_list_news_include_on_fragment_main), withParent(withParent(withId(R.id.container_list_news_include_on_fragment_main)))));
        nameNewsText.check(matches(isDisplayed()));
    }

    @Test
    //1.2 ААвторизация с валидными логином и паролем незарегистрированного пользователя
    public void unregisteredPasswordAuthorizationTest() {
        attemptOfAuthorization(unregisteredLogin, unregisteredPassword);
        assertionOfToast(R.string.error);
    }

    @Test
    //1.3 Авторизация с пустым логином
    public void emptyLoginAuthorizationTest() {
        attemptOfAuthorization("", registeredPassword);
        assertionOfToast(R.string.empty_login_or_password);
    }

    @Test
    //1.4 Авторизация с пустым паролем
    public void emptyPasswordAuthorizationTest() {
        attemptOfAuthorization(registeredLogin,"" );
        assertionOfToast(R.string.empty_login_or_password);
    }

    @Test
    //1.5 Авторизация с пустыми логином и паролем
    public void emptyLoginAndPasswordAuthorizationTest() {
        attemptOfAuthorization("","" );
        assertionOfToast(R.string.empty_login_or_password);
    }

    @Test
    //1.6 Авторизация с невалидным логином
    public void invalidLoginAuthorizationTest() {
        attemptOfAuthorization(invalidLogin,registeredPassword);
        assertionOfToast(R.string.error);
    }

    @Test
    //1.7 Авторизация с невалидным паролем
    public void invalidPasswordAuthorizationTest() {
        attemptOfAuthorization(registeredLogin,invalidPassword);
        assertionOfToast(R.string.error);
    }

    @Test
    //1.8 Выход из приложения после успешной авторизации
    public void successExitProcessTest() {
        attemptOfAuthorization(registeredLogin,registeredPassword);
        //ждем, пока на странице появится контейнер с новостями
        onView(isRoot()).perform(waitDisplayed(R.id.container_list_news_include_on_fragment_main, waitingPeriod));
        //убеждаемся, что на странице есть фрагмент с текстом textOf_container_list_news_include_on_fragment_main
        ViewInteraction nameNewsText = onView(allOf(withText(textOf_container_list_news_include_on_fragment_main), withParent(withParent(withId(R.id.container_list_news_include_on_fragment_main)))));
        nameNewsText.check(matches(isDisplayed()));

        makeRelogin();

        //ждем, пока на странице появится кнопка "SIGN IN"
        onView(isRoot()).perform(waitDisplayed(R.id.enter_button, waitingPeriod));
        //убеждаемся, что на странице есть фрагмент с текстом "textOf_nav_host_fragment
        ViewInteraction nameAuthText2 = onView(allOf(withText(textOf_nav_host_fragment), withParent(withParent(withId(R.id.nav_host_fragment)))));
        nameAuthText2.check(matches(isDisplayed()));
    }
    public static void assertionOfToast(int textOfToast) {
        //Убедимся, что видно сообщение c определенным текстом об ошибке в Toast
        onView(withText(textOfToast))
                .inRoot(withDecorView(Matchers.not(decorView)))
                .check(matches(isDisplayed()));
    }
//    private static Matcher<View> childAtPosition(
//            final Matcher<View> parentMatcher, final int position) {
//
//        return new TypeSafeMatcher<View>() {
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("Child at position " + position + " in parent ");
//                parentMatcher.describeTo(description);
//            }
//
//            @Override
//            public boolean matchesSafely(View view) {
//                ViewParent parent = view.getParent();
//                return parent instanceof ViewGroup && parentMatcher.matches(parent)
//                        && view.equals(((ViewGroup) parent).getChildAt(position));
//            }
//        };
//    }
}

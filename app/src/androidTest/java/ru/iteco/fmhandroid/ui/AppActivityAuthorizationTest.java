package ru.iteco.fmhandroid.ui;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
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

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Severity;
import io.qameta.allure.kotlin.SeverityLevel;
import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)

public class AppActivityAuthorizationTest {

    @Rule
    public ActivityTestRule<AppActivity> mActivityRule =
            new ActivityTestRule<>(AppActivity.class);
    private static View popupDecorView;

    @Before
    public void setUp() throws InterruptedException {

        AppActivity activity = mActivityRule.getActivity();
        popupDecorView = activity.getWindow().getDecorView();
    }

    @Severity(SeverityLevel.BLOCKER)
    @Test
    @Description("1.1 Авторизация с валидными логином и паролем зарегистрированного пользователя")
    public void ValidAuthorizationTest() throws InterruptedException {
        attemptOfAuthorization(registeredLogin, registeredPassword);

        //ждем, пока на странице появится контейнер с новостями
        onView(isRoot()).perform(waitDisplayed(R.id.container_list_news_include_on_fragment_main, waitingPeriod));
        //убеждаемся, что на странице есть фрагмент с текстом textOf_container_list_news_include_on_fragment_main
        ViewInteraction nameNewsText = onView(allOf(withText(textOf_container_list_news_include_on_fragment_main), withParent(withParent(withId(R.id.container_list_news_include_on_fragment_main)))));
        nameNewsText.check(matches(isDisplayed()));
    }

    @Severity(SeverityLevel.BLOCKER)
    @Test
    @Description("1.2 Авторизация с валидными логином и паролем незарегистрированного пользователя")
    public void unregisteredPasswordAuthorizationTest() {
        attemptOfAuthorization(unregisteredLogin, unregisteredPassword);
        assertionOfToast(R.string.error);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test
    @Description("1.3 Авторизация с пустым логином")
    public void emptyLoginAuthorizationTest() {
        attemptOfAuthorization("", registeredPassword);
        assertionOfToast(R.string.empty_login_or_password);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test
    @Description("1.4 Авторизация с пустым паролем")
    public void emptyPasswordAuthorizationTest() {
        attemptOfAuthorization(registeredLogin,"" );
        assertionOfToast(R.string.empty_login_or_password);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test
    @Description("1.5 Авторизация с пустыми логином и паролем")
    public void emptyLoginAndPasswordAuthorizationTest() {
        attemptOfAuthorization("","" );
        assertionOfToast(R.string.empty_login_or_password);
    }

    @Severity(SeverityLevel.MINOR)
    @Test
    @Description("1.6 Авторизация с невалидным логином")
    public void invalidLoginAuthorizationTest() {
        attemptOfAuthorization(invalidLogin,registeredPassword);
        assertionOfToast(R.string.error);
    }

    @Severity(SeverityLevel.MINOR)
    @Test
    @Description("1.7 Авторизация с невалидным паролем")
    public void invalidPasswordAuthorizationTest() {
        attemptOfAuthorization(registeredLogin,invalidPassword);
        assertionOfToast(R.string.error);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test
    @Description("1.8 Выход из приложения после успешной авторизации")
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

    @Step("Убедаемся, что видно сообщение c определенным текстом об ошибке в Toast")
    public static void assertionOfToast(int textOfToast) {
        onView(withText(textOfToast))
                .inRoot(withDecorView(Matchers.not(popupDecorView)))
                .check(matches(isDisplayed()));
    }
}

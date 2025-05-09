package ru.iteco.fmhandroid.ui.tests;

import static ru.iteco.fmhandroid.ui.data.DataHelper.invalidLogin;
import static ru.iteco.fmhandroid.ui.data.DataHelper.invalidPassword;
import static ru.iteco.fmhandroid.ui.data.DataHelper.registeredLogin;
import static ru.iteco.fmhandroid.ui.data.DataHelper.registeredPassword;
import static ru.iteco.fmhandroid.ui.data.DataHelper.unregisteredLogin;
import static ru.iteco.fmhandroid.ui.data.DataHelper.unregisteredPassword;

import android.view.View;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Severity;
import io.qameta.allure.kotlin.SeverityLevel;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)

public class AppActivityAuthorizationTest {

    @Rule
    public ActivityTestRule<AppActivity> mActivityRule =
            new ActivityTestRule<>(AppActivity.class);
    private static View popupDecorView;
    private AuthorizationPage authorizationPage;

    @Before
    public void setUp() {

        AppActivity activity = mActivityRule.getActivity();
        popupDecorView = activity.getWindow().getDecorView();

        authorizationPage = new AuthorizationPage();
    }

    @Severity(SeverityLevel.BLOCKER)
    @Test
    @Description("1.1 Авторизация с валидными логином и паролем зарегистрированного пользователя")
    public void ValidAuthorizationTest() {
        //AuthorizationPage authorizationPage = new AuthorizationPage();
        authorizationPage.attemptOfSuccessAuthorization(registeredLogin, registeredPassword);
    }

    @Severity(SeverityLevel.BLOCKER)
    @Test
    @Description("1.2 Авторизация с валидными логином и паролем незарегистрированного пользователя")
    public void unregisteredPasswordAuthorizationTest() {
        //AuthorizationPage authorizationPage = new AuthorizationPage();
        authorizationPage.attemptOfSadAuthorization(unregisteredLogin, unregisteredPassword);
        authorizationPage.assertionOfToast(popupDecorView, R.string.error);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test
    @Description("1.3 Авторизация с пустым логином")
    public void emptyLoginAuthorizationTest() {
        //AuthorizationPage authorizationPage = new AuthorizationPage();
        authorizationPage.attemptOfSadAuthorization("", registeredPassword);
        authorizationPage.assertionOfToast(popupDecorView, R.string.empty_login_or_password);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test
    @Description("1.4 Авторизация с пустым паролем")
    public void emptyPasswordAuthorizationTest() {
        //AuthorizationPage authorizationPage = new AuthorizationPage();
        authorizationPage.attemptOfSadAuthorization(registeredLogin,"" );
        authorizationPage.assertionOfToast(popupDecorView, R.string.empty_login_or_password);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test
    @Description("1.5 Авторизация с пустыми логином и паролем")
    public void emptyLoginAndPasswordAuthorizationTest() {
        //AuthorizationPage authorizationPage = new AuthorizationPage();
        authorizationPage.attemptOfSadAuthorization("","" );
        authorizationPage.assertionOfToast(popupDecorView, R.string.empty_login_or_password);
    }

    @Severity(SeverityLevel.MINOR)
    @Test
    @Description("1.6 Авторизация с невалидным логином")
    public void invalidLoginAuthorizationTest() {
        //AuthorizationPage authorizationPage = new AuthorizationPage();
        authorizationPage.attemptOfSadAuthorization(invalidLogin,registeredPassword);
        authorizationPage.assertionOfToast(popupDecorView, R.string.error);
    }

    @Severity(SeverityLevel.MINOR)
    @Test
    @Description("1.7 Авторизация с невалидным паролем")
    public void invalidPasswordAuthorizationTest() {
        //AuthorizationPage authorizationPage = new AuthorizationPage();
        authorizationPage.attemptOfSadAuthorization(registeredLogin,invalidPassword);
        authorizationPage.assertionOfToast(popupDecorView, R.string.error);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test
    @Description("1.8 Выход из приложения после успешной авторизации")
    public void successExitProcessTest() {
        //AuthorizationPage authorizationPage = new AuthorizationPage();
        MainPage mainPage = authorizationPage.attemptOfSuccessAuthorization(registeredLogin,registeredPassword);
        mainPage.makeRelogin();
    }
}

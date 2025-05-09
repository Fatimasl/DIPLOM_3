package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.objectOrIdCheckToBeDisplayedAndThenReplaceText;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.waitDisplayed;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.waitingPeriod;

import android.view.View;

import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matchers;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;

public class AuthorizationPage {
    static String hintOf_login_text_input_layout = "Login";
    static String hintOf_password_text_input_layout = "Password";
    static int enter_button = R.id.enter_button;
    static int password_text_input_layout = R.id.password_text_input_layout;
    static int login_text_input_layout = R.id.login_text_input_layout;

    public AuthorizationPage() {
        try {
            //ждем, пока на странице появится кнопка "SIGN IN"
            onView(isRoot()).perform(waitDisplayed(enter_button, waitingPeriod));
        } catch (Exception e) {
            //если не дождались страницы ввода логина/пароля, то делаем предположение, что вход уже осуществлен
            //значит надо сделать релогин
            MainPage mainPage = new MainPage();
            mainPage.makeRelogin();
            //ждем, пока на странице появится кнопка "SIGN IN"
            onView(isRoot()).perform(waitDisplayed(enter_button, waitingPeriod));
        }
    }

    @Step("Убедаемся, что видно сообщение c определенным текстом об ошибке в Toast")
    public static void assertionOfToast(View popupDecorView, int textOfToast) {
        onView(withText(textOfToast))
                .inRoot(withDecorView(Matchers.not(popupDecorView)))
                .check(matches(isDisplayed()));
    }

    private static void enterLoginPassword(String login, String password) {
        //вводим логин
        ViewInteraction loginView = onView(allOf(withHint(hintOf_login_text_input_layout), withParent(withParent(withId(login_text_input_layout)))));
        objectOrIdCheckToBeDisplayedAndThenReplaceText(loginView, 0, login);

        //вводим пароль
        ViewInteraction passwordView = onView(allOf(withHint(hintOf_password_text_input_layout), withParent(withParent(withId(password_text_input_layout)))));
        objectOrIdCheckToBeDisplayedAndThenReplaceText(passwordView, 0, password);

        //жмем кнопку "SIGN IN"
        ViewInteraction materialButton = onView(withId(enter_button));
        materialButton.perform(click());
    }

    @Step("Делаем попытку успешной авторизации по переданной паре логин/пароль")
    public static MainPage attemptOfSuccessAuthorization(String login, String password) {
        enterLoginPassword(login, password);
        return new MainPage();
    }

    @Step("Делаем попытку НЕуспешной авторизации по переданной паре логин/пароль")
    public static void attemptOfSadAuthorization(String login, String password) {
        enterLoginPassword(login, password);
    }
}

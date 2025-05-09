package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.idWaitToBeDisplayedAndThenMaybeClick;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.objectOrIdCheckToBeDisplayedAndThenClick;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.waitDisplayed;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.waitingPeriod;

import android.view.View;

import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matchers;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;

public class MainPage {
    static int authorization_image_button = R.id.authorization_image_button;
    static int titleLogOut = android.R.id.title;
    static int main_menu_image_button = R.id.main_menu_image_button;
    static int all_news_text_view = R.id.all_news_text_view;
    static int our_mission_image_button = R.id.our_mission_image_button;
    static String descriptionOf_authorization_image_button = "Authorization";
    static String textOf_menuLogOut = "Log out";
    static String textOf_menuAbout = "About";
    public MainPage() {
        //ждем, пока на странице появится кнопка "Личный кабинет"
        onView(isRoot()).perform(waitDisplayed(authorization_image_button, waitingPeriod));
    }

    @Step("Делаем релогин из приложения")
    public static AuthorizationPage makeRelogin(){
        //жмем на кнопку авторизации
        ViewInteraction appCompatImageButton = onView(allOf(withId(authorization_image_button), withContentDescription(descriptionOf_authorization_image_button)));
        objectOrIdCheckToBeDisplayedAndThenClick(appCompatImageButton, 0);

        //жмем на "Log out"
        ViewInteraction materialTextView = onView(allOf(withId(titleLogOut), withText(textOf_menuLogOut)));
        objectOrIdCheckToBeDisplayedAndThenClick(materialTextView, 0);

        return new AuthorizationPage();
    }

    @Step("Делаем попытку нажатия на гиперссылку All news и проверка, что открылась страница со всеми новостями и кнопкой сортировки")
    public static AllNewsPage attemptOfClickAllNews() {
        //ждем, пока на странице появится ссылка "All news" и нажимаем на нее
        idWaitToBeDisplayedAndThenMaybeClick(all_news_text_view, true);
        return new AllNewsPage();
    }
    @Step("Нажимаем на меню About")
    public AboutPage clickMenuAbout(View popupDecorView){
        //Жмем на кнопку МЕНЮ
        idWaitToBeDisplayedAndThenMaybeClick(main_menu_image_button, true);

        //Из всплывающего меню жмем раздел ABOUT
        onView(withText(textOf_menuAbout))
                .inRoot(withDecorView(Matchers.not(popupDecorView)))
                .check(matches(isDisplayed()))
                .perform(click());

        return new AboutPage();
    }

    @Step("Нажимаем на меню Love is all")
    public LoveIsAllPage clickMenuLoveIsAll(){
        objectOrIdCheckToBeDisplayedAndThenClick(null, our_mission_image_button);

        return new LoveIsAllPage();
    }
}

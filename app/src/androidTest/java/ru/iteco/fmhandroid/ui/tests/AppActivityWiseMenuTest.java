package ru.iteco.fmhandroid.ui.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.iteco.fmhandroid.ui.data.DataHelper.registeredLogin;
import static ru.iteco.fmhandroid.ui.data.DataHelper.registeredPassword;
import static ru.iteco.fmhandroid.ui.data.DataHelper.wiseAmount;

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
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.LoveIsAllPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class AppActivityWiseMenuTest {

    @Rule
    public ActivityTestRule<AppActivity> mActivityRule =
            new ActivityTestRule<>(AppActivity.class);
    private static View popupDecorView;
    private LoveIsAllPage loveIsAllPage;

    @Before
    public void setUp() {
        AppActivity activity = mActivityRule.getActivity();
        popupDecorView = activity.getWindow().getDecorView();
    }

    @Before
    public void EveryTimeTheseTests() {
        //Делаем при необходимости перелогин
        AuthorizationPage authorizationPage = new AuthorizationPage();
        MainPage mainPage = authorizationPage.attemptOfSuccessAuthorization(registeredLogin, registeredPassword);
        //Нажимаем на меню Love is all
        loveIsAllPage = mainPage.clickMenuLoveIsAll();

    }
    @Severity(SeverityLevel.TRIVIAL)
    @Test
    @Description("3.1 Вызов из меню Бабочка списка цитат и проверка их количества")
    public void wiseAmountTest()  {
        //определим сколько элементов в списке цитат
        int actualItemCount = loveIsAllPage.getWiseItemCount();
        //сравним с ожидаемым значением
        assertEquals(wiseAmount, actualItemCount);
    }

    @Severity(SeverityLevel.TRIVIAL)
    @Test
    @Description("3.2 Вызов из меню Бабочка списка цитат, поиск нужной цитаты по заголовку и проверка соответствия описания цитаты заголовку")
    public void wiseContentTest()  {

        loveIsAllPage.chooseRandomWiseNote();

        //Проверяем, что цитата с определенным заголовком существует и ей соответствует определенное описание (поиск по части описания)
        boolean testResult = loveIsAllPage.testIterateAllRecyclerItems(popupDecorView, DataHelper.OurWiseTitle, "Check wise"); //Проверяем наличие цитаты с определенным текстом

        if (testResult) {
            //завершаем тест с успехом
            assertTrue("This test passed intentionally", true);
        } else {
            //заваливаем тест
            assertTrue("This test is failed", false);
        }
    }
}

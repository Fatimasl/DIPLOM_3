package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.intent.matcher.UriMatchers.hasHost;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;
import static ru.iteco.fmhandroid.ui.data.DataHelper.hostName;
import static ru.iteco.fmhandroid.ui.data.DataHelper.registeredLogin;
import static ru.iteco.fmhandroid.ui.data.DataHelper.registeredPassword;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.isSortedAscending;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.isSortedDesending;
import static ru.iteco.fmhandroid.ui.pages.ControlPanelPage.textForCompairActive;
import static ru.iteco.fmhandroid.ui.pages.ControlPanelPage.textForCompairNotActive;

import android.content.Intent;
import android.view.View;

import androidx.test.espresso.intent.Intents;
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
import ru.iteco.fmhandroid.ui.data.NewsGenerator;
import ru.iteco.fmhandroid.ui.pages.AboutPage;
import ru.iteco.fmhandroid.ui.pages.AllNewsPage;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.ControlPanelPage;
import ru.iteco.fmhandroid.ui.pages.CreatingNewsPage;
import ru.iteco.fmhandroid.ui.pages.FilterNewsPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;

@LargeTest
@RunWith(AllureAndroidJUnit4.class)
public class AppActivityMainMenuTest {

    @Rule
    public ActivityTestRule<AppActivity> mActivityRule =
            new ActivityTestRule<>(AppActivity.class);
    private static View popupDecorView;
    private NewsGenerator news;
    private MainPage mainPage;

    @Before
    public void setUp() {
        AppActivity activity = mActivityRule.getActivity();
        popupDecorView = activity.getWindow().getDecorView();
    }

    @Before
    public void reloginEveryTime() {
        //Делаем при необходимости перелогин
        AuthorizationPage authorizationPage = new AuthorizationPage();
        mainPage = authorizationPage.attemptOfSuccessAuthorization(registeredLogin, registeredPassword);
    }

    @Before
    public void chooseRandomDataForNews() {
        //заполняем рандомно категорию и описание для новой новости (события)
        news = new NewsGenerator();
        NewsGenerator.chooseRandomDataForCreationNews(news, 0);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test
    @Description("2.1 Вызов из меню MAIN ссылки ALL NEWS")
    public void callAllNewsTest() {
        mainPage.attemptOfClickAllNews();
    }

    @Severity(SeverityLevel.BLOCKER)
    @Test
    @Description("2.2 Вызов из меню NEWS страницы Control panel и добавление нового события (новости)")
    public void addNewEventTest() {
        //Кликаем "все новости"
        AllNewsPage allNews = mainPage.attemptOfClickAllNews();
        //создаем новую новость
        ControlPanelPage controlPanel = allNews.attemptCreationNewEvent(true);
        CreatingNewsPage creatingNews = controlPanel.clickPlus();
        ControlPanelPage controlPanel2 = creatingNews.fillOutNews(news);
        //Проверяем, что новая новость создана с нужным описанием
        boolean testResult = controlPanel2.testIterateAllRecyclerItems(popupDecorView, news.description, "Check availability"); //Проверяем наличие новости с определенным текстом

        if (testResult) {
            //завершаем тест с успехом
            assertTrue("This test passed intentionally", true);
        } else {
            //заваливаем тест
            assertTrue("This test is failed", false);
        }
    }

    @Severity(SeverityLevel.BLOCKER)
    @Test
    @Description("2.3 Вызов из меню NEWS страницы Control panel, добавление нового события (новости) и удаление его")
    public void deleteNewEventTest() {
        //Кликаем "все новости"
        AllNewsPage allNews = mainPage.attemptOfClickAllNews();
        //создаем новую новость
        ControlPanelPage controlPanel = allNews.attemptCreationNewEvent(true);
        CreatingNewsPage creatingNews = controlPanel.clickPlus();
        ControlPanelPage controlPanel2 = creatingNews.fillOutNews(news);
        //Проверяем, что новая новость создана с нужным описанием и сразу удаляем ее
        boolean removeResult = controlPanel2.testIterateAllRecyclerItems(popupDecorView, news.description, "Remove event"); //Удаляем новость с определенным текстом

        if (removeResult) {
            //если удаление успешно, то проверяем, что новости с определенным текстом нет в списке
            boolean testResult = controlPanel2.testIterateAllRecyclerItems(popupDecorView, news.description, "Check availability"); //Проверяем наличие новости с определенным текстом
            if (testResult) {//если новость присутствует, то заваливаем тест
                assertTrue("This test passed intentionally", false);
            } else {
                //если новость отсутствует, то значит успех в удалении новости
                assertTrue("This test is failed", true);
            }
        } else {
            //если удаление НЕ успешно, то заваливаем тест
            assertTrue("This test is failed", false);
        }
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test
    @Description("2.4 Вызов из меню NEWS страницы Control panel, добавление нового события (новости), вызов удаления этого события и отмена удаления")
    public void cancelDeletionNewEventTest() {
        //Кликаем "все новости"
        AllNewsPage allNews = mainPage.attemptOfClickAllNews();
        //создаем новую новость
        ControlPanelPage controlPanel = allNews.attemptCreationNewEvent(true);
        CreatingNewsPage creatingNews = controlPanel.clickPlus();
        ControlPanelPage controlPanel2 = creatingNews.fillOutNews(news);
        //Проверяем, что новая новость создана с нужным описанием и сразу удаляем ее, но потом не подтверждаем удаления
        boolean cancelRemoveResult = controlPanel2.testIterateAllRecyclerItems(popupDecorView, news.description, "Cancel removing event"); //Удаляем новость, но не подтверждаем этого действия

        if (cancelRemoveResult) {
            //если отмена удаления успешна, то проверяем, что новость с определенным текстом по-прежнему в списке
            boolean testResult = controlPanel2.testIterateAllRecyclerItems(popupDecorView, news.description, "Check availability"); //Проверяем наличие новости с определенным текстом
            if (testResult) {
                //завершаем тест с успехом
                assertTrue("This test passed intentionally", true);
            } else {
                //заваливаем тест
                assertTrue("This test is failed", false);
            }
        } else {
            //если отмена удаления НЕ успешна, то заваливаем тест
            assertTrue("This test is failed", false);
        }
    }

    @Severity(SeverityLevel.NORMAL)
    @Test
    @Description("2.5 Вызов из меню NEWS страницы Control panel, добавление нового события (новости), вызов редактирования этого события и перевод события в неактивный режим")
    public void deactiveNewEventTest() {
        //Кликаем "все новости"
        AllNewsPage allNews = mainPage.attemptOfClickAllNews();
        //создаем новую новость
        ControlPanelPage controlPanel = allNews.attemptCreationNewEvent(true);
        CreatingNewsPage creatingNews = controlPanel.clickPlus();
        ControlPanelPage controlPanel2 = creatingNews.fillOutNews(news);
        //Проверяем, что новая новость создана с нужным описанием и сразу деактивируем ее
        boolean deactiveResult = controlPanel2.testIterateAllRecyclerItems(popupDecorView, news.description, "Deactive event"); //Деактивируем новость

        if (deactiveResult) {
            //если деактивация успешна, то проверяем, что новость с определенным текстом по-прежнему в списке и деактивирована
            boolean testResult = controlPanel2.testIterateAllRecyclerItems(popupDecorView, news.description, "Check availability and not active"); //Проверяем наличие деактивированной новости с определенным текстом
            if (testResult) {
                //завершаем тест с успехом
                assertTrue("This test passed intentionally", true);
            } else {
                //заваливаем тест
                assertTrue("This test is failed", false);
            }
        } else {
            //если отмена удаления НЕ успешна, то заваливаем тест
            assertTrue("This test is failed", false);
        }
    }

    @Severity(SeverityLevel.NORMAL)
    @Test
    @Description("2.6 Вызов из меню NEWS страницы Control panel, добавление нового события (новости), отфильтровать события по категории и убедиться, что отфильтрованные события принадлежат выбранной категории")
    public void filterNewsByCategoryTest()  {
        //Кликаем "все новости"
        AllNewsPage allNews = mainPage.attemptOfClickAllNews();
        //создаем новую новость
        ControlPanelPage controlPanel = allNews.attemptCreationNewEvent(true);
        CreatingNewsPage creatingNews = controlPanel.clickPlus();
        ControlPanelPage controlPanel2 = creatingNews.fillOutNews(news);

        //Нажимаем кнопку "Фильтровать"
        FilterNewsPage filterNewsPage = controlPanel2.clickFilter();
        //Заполняем параметры фильтрации по категории
        ControlPanelPage controlPanel3 = filterNewsPage.fillOutFilterCategory(popupDecorView, news.category);

        //Проверяем, что все новости отфильтрованы по категории новой новости
        controlPanel3.testIterateRecyclerItemsByCondition(news.category, "Filter category");
    }

    @Severity(SeverityLevel.NORMAL)
    @Test
    @Description("2.7 Вызов из меню NEWS страницы Control panel, добавление нового события (новости), отфильтровать события по дате и убедиться, что отфильтрованные события принадлежат выбранной дате")
    public void filterNewsByDateTest()  {
        //Кликаем "все новости"
        AllNewsPage allNews = mainPage.attemptOfClickAllNews();
        //создаем новую новость
        ControlPanelPage controlPanel = allNews.attemptCreationNewEvent(true);
        CreatingNewsPage creatingNews = controlPanel.clickPlus();
        ControlPanelPage controlPanel2 = creatingNews.fillOutNews(news);

        //Нажимаем кнопку "Фильтровать"
        FilterNewsPage filterNewsPage = controlPanel2.clickFilter();
        //Заполняем параметры фильтрации по дате
        ControlPanelPage controlPanel3 = filterNewsPage.fillOutFilterDate(news.date);

        //Проверяем, что все новости отфильтрованы по дате новой новости
        controlPanel3.testIterateRecyclerItemsByCondition(news.date, "Filter date");
    }

    @Severity(SeverityLevel.NORMAL)
    @Test
    @Description("2.8 Вызов из меню NEWS страницы Control panel, добавление нового события (новости), отфильтровать события по активным событиям и убедиться, что отфильтрованные события активны")
    public void filterNewsByActiveTest()  {
        //Кликаем "все новости"
        AllNewsPage allNews = mainPage.attemptOfClickAllNews();
        //создаем новую новость
        ControlPanelPage controlPanel = allNews.attemptCreationNewEvent(true);
        CreatingNewsPage creatingNews = controlPanel.clickPlus();
        ControlPanelPage controlPanel2 = creatingNews.fillOutNews(news);

        //Нажимаем кнопку "Фильтровать"
        FilterNewsPage filterNewsPage = controlPanel2.clickFilter();

        //Заполняем параметры фильтрации только по активным событиям
        ControlPanelPage controlPanel3 = filterNewsPage.fillOutFilterOnlyActive();

        //Проверяем, что все новости отфильтрованы только по активным
        controlPanel3.testIterateRecyclerItemsByCondition(textForCompairActive, "Filter active");
    }

    @Severity(SeverityLevel.MINOR)
    @Test
    @Description("2.9 Вызов из меню NEWS страницы Control panel, добавление нового события (новости), сделать его неактивным, отфильтровать события по неактивным событиям и убедиться, что отфильтрованные события не активны")
    public void filterNewsByNotActiveTest()  {
        //Кликаем "все новости"
        AllNewsPage allNews = mainPage.attemptOfClickAllNews();
        //создаем новую новость
        ControlPanelPage controlPanel = allNews.attemptCreationNewEvent(true);
        CreatingNewsPage creatingNews = controlPanel.clickPlus();
        ControlPanelPage controlPanel2 = creatingNews.fillOutNews(news);
        //Проверяем, что новая новость создана с нужным описанием и сразу деактивируем ее
        boolean deactiveResult = controlPanel2.testIterateAllRecyclerItems(popupDecorView, news.description, "Deactive event"); //Деактивируем новость

        if (deactiveResult) {
            //Нажимаем кнопку "Фильтровать"
            FilterNewsPage filterNewsPage = controlPanel2.clickFilter();

            //Заполняем параметры фильтрации только по неактивным событиям
            ControlPanelPage controlPanel3 = filterNewsPage.fillOutFilterOnlyNotActive();

            //Проверяем, что все новости отфильтрованы только по неактивным событиям
            controlPanel3.testIterateRecyclerItemsByCondition(textForCompairNotActive, "Filter active");
        } else {
            //если отмена удаления НЕ успешна, то заваливаем тест
            assertTrue("This test is failed", false);
        }
    }

    @Severity(SeverityLevel.NORMAL)
    @Test
    @Description("2.10-2.11 Вызов из меню NEWS страницы Control panel, добавление трех новых событий (новостей) на сегодня, через месяц и через год, отсортировать события и убедиться, что события в списке идут по порядку")
    public void sortNewsTest()  {

        //Кликаем "все новости"
        AllNewsPage allNews = mainPage.attemptOfClickAllNews();
        //создаем новую новость на сегодня
        ControlPanelPage controlPanel = allNews.attemptCreationNewEvent(true);
        CreatingNewsPage creatingNews = controlPanel.clickPlus();
        creatingNews.fillOutNews(news);

        //создаем новую новость в следующем месяце
        NewsGenerator news2 = new NewsGenerator();
        NewsGenerator.chooseRandomDataForCreationNews(news2, 30);
        ControlPanelPage controlPanel2 = allNews.attemptCreationNewEvent(false);
        CreatingNewsPage creatingNews2 = controlPanel2.clickPlus();
        creatingNews2.fillOutNews(news2);

        //создаем новую новость в следующем году
        NewsGenerator news3 = new NewsGenerator();
        NewsGenerator.chooseRandomDataForCreationNews(news3, 365);
        ControlPanelPage controlPanel3 = allNews.attemptCreationNewEvent(false);
        CreatingNewsPage creatingNews3 = controlPanel3.clickPlus();
        ControlPanelPage controlPanel4 = creatingNews3.fillOutNews(news3);

        //Нажимаем кнопку "Сортировать"
        ControlPanelPage controlPanel5 = controlPanel4.clickSorter();

        //Проверяем, как расположены созданные новости (получаем массив номеров в отсортированном списке)
        int[] numbers1 = controlPanel5.testIterateRecyclerItemsBySort(news, news2, news3);

        //Нажимаем кнопку "Сортировать" повторно
        ControlPanelPage controlPanel6 = controlPanel4.clickSorter();

        //Проверяем, как расположены созданные новости (получаем массив номеров в отсортированном в обратном порядке списке)
        int[] numbers2 = controlPanel6.testIterateRecyclerItemsBySort(news, news2, news3);

        if (isSortedAscending(numbers1)) {
            if (isSortedDesending(numbers2)) {
                //если в первом случае новости упорядочены по возрастанию, а во втором по убыванию, то успех
                assertTrue("This test passed intentionally", true);
            } else {
                //если в первом случае новости упорядочены по возрастанию, и во втором по возрастанию, то заваливаем тест
                assertTrue("This test is failed", false);
            }
        } else if (isSortedDesending(numbers1)) {
            if (isSortedAscending(numbers2)) {
                //если в первом случае новости упорядочены по убыванию, а во втором по возрастанию, то успех
                assertTrue("This test passed intentionally", true);
            } else {
                //если в первом случае новости упорядочены по убыванию, и во втором по убыванию, то заваливаем тест
                assertTrue("This test is failed", false);
            }
        }
    }

    @Severity(SeverityLevel.TRIVIAL)
    @Test
    @Description("2.12 Вызов из меню About первой ссылки")
    public void firstAboutLinkTest() {

        AboutPage aboutPage = mainPage.clickMenuAbout(popupDecorView);
        //включаем прослушку intents
        Intents.init();
        //кликаем на первую ссылку
        aboutPage.clickPrivacyPolicyLink();
        //проверяем, что было вызвано приложение для открытия ссылки нужного сайта
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(hasHost(hostName))
        ));
        //выключаем прослушку intents
        Intents.release();
    }

    @Severity(SeverityLevel.TRIVIAL)
    @Test
    @Description("2.13 Вызов из меню About второй ссылки")
    public void secondAboutLinkTest() {
        AboutPage aboutPage = mainPage.clickMenuAbout(popupDecorView);
        //включаем прослушку intents
        Intents.init();
        //кликаем на вторую ссылку
        aboutPage.clickTermsOfUseLink();
        //проверяем, что было вызвано приложение для открытия ссылки нужного сайта
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(hasHost(hostName))
        ));
        //выключаем прослушку intents
        Intents.release();
    }
}

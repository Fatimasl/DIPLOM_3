package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.intent.matcher.UriMatchers.hasHost;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;
import static ru.iteco.fmhandroid.ui.Helper.attemptCreationNewEvent;

import static ru.iteco.fmhandroid.ui.Helper.attemptOfAuthorization;
import static ru.iteco.fmhandroid.ui.Helper.attemptOfClickAllNews;
import static ru.iteco.fmhandroid.ui.Helper.idWaitToBeDisplayedAndThenMaybeClick;

import static ru.iteco.fmhandroid.ui.Helper.isSortedAscending;
import static ru.iteco.fmhandroid.ui.Helper.isSortedDesending;
import static ru.iteco.fmhandroid.ui.Helper.objectOrIdCheckToBeDisplayedAndThenClick;
import static ru.iteco.fmhandroid.ui.Helper.objectOrIdCheckToBeDisplayedAndThenReplaceText;
import static ru.iteco.fmhandroid.ui.Helper.registeredLogin;
import static ru.iteco.fmhandroid.ui.Helper.registeredPassword;
import static ru.iteco.fmhandroid.ui.Helper.testIterateAllRecyclerItems;
import static ru.iteco.fmhandroid.ui.Helper.testIterateRecyclerItemsByCondition;
import static ru.iteco.fmhandroid.ui.Helper.testIterateRecyclerItemsBySort;

import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.iteco.fmhandroid.R;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AppActivityMainMenuTest {
    @Rule
    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);
    private static View popupDecorView;

    private NewsData news;

    @Before
    public void setUp() {
        mActivityScenarioRule.getScenario().onActivity(new ActivityScenario.ActivityAction<AppActivity>() {
            @Override
            public void perform(AppActivity activity) {
                popupDecorView = activity.getWindow().getDecorView();
            }
        });
    }

    @Before
    public void reloginEveryTime() {
        //Делаем при необходимости перелогин
        attemptOfAuthorization(registeredLogin, registeredPassword);
    }

    @Before
    public void chooseRandomDataEveryTime() {
        //заполняем рандомно категорию и описание для новой новости (события)
        news = new NewsData();
        NewsData.chooseRandomDataForCreationNews(news, 0);
    }

    @Test
    //2.1 Вызов из меню MAIN ссылки ALL NEWS
    public void callAllNewsTest() {
        attemptOfClickAllNews();
    }

    @Test
    //2.2 Вызов из меню NEWS страницы Control panel и добавление нового события (новости)
    public void addNewEventTest() {
        //Кликаем "все новости"
        attemptOfClickAllNews();
        //создаем новую новость
        attemptCreationNewEvent(news, true);
        //Проверяем, что новая новость создана с нужным описанием
        boolean testResult = testIterateAllRecyclerItems(popupDecorView, R.id.news_list_recycler_view, news.description, "Check availability"); //Проверяем наличие новости с определенным текстом

        if (testResult) {
            //завершаем тест с успехом
            assertTrue("This test passed intentionally", true);
        } else {
            //заваливаем тест
            assertTrue("This test is failed", false);
        }
    }

    @Test
    //2.3 Вызов из меню NEWS страницы Control panel, добавление нового события (новости) и удаление его
    public void deleteNewEventTest() {
        //Кликаем "все новости"
        attemptOfClickAllNews();
        //создаем новую новость
        attemptCreationNewEvent(news, true);
        //Проверяем, что новая новость создана с нужным описанием и сразу удаляем ее
        boolean removeResult = testIterateAllRecyclerItems(popupDecorView, R.id.news_list_recycler_view, news.description, "Remove event"); //Удаляем новость с определенным текстом

        if (removeResult) {
            //если удаление успешно, то проверяем, что новости с определенным текстом нет в списке
            boolean testResult = testIterateAllRecyclerItems(popupDecorView, R.id.news_list_recycler_view, news.description, "Check availability"); //Проверяем наличие новости с определенным текстом
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

    @Test
    //2.4 Вызов из меню NEWS страницы Control panel, добавление нового события (новости), вызов удаления этого события и отмена удаления
    public void cancelDeletionNewEventTest() {
        //Кликаем "все новости"
        attemptOfClickAllNews();
        //создаем новую новость
        attemptCreationNewEvent(news, true);
        //Проверяем, что новая новость создана с нужным описанием и сразу удаляем ее, но потом не подтверждаем удаления
        boolean cancelRemoveResult = testIterateAllRecyclerItems(popupDecorView, R.id.news_list_recycler_view, news.description, "Cancel removing event"); //Удаляем новость, но не подтверждаем этого действия

        if (cancelRemoveResult) {
            //если отмена удаления успешна, то проверяем, что новость с определенным текстом по-прежнему в списке
            boolean testResult = testIterateAllRecyclerItems(popupDecorView, R.id.news_list_recycler_view, news.description, "Check availability"); //Проверяем наличие новости с определенным текстом
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

    @Test
    //2.5 Вызов из меню NEWS страницы Control panel, добавление нового события (новости), вызов редактирования этого события и перевод события в неактивный режим
    public void deactiveNewEventTest() {
        //Кликаем "все новости"
        attemptOfClickAllNews();
        //создаем новую новость
        attemptCreationNewEvent(news, true);
        //Проверяем, что новая новость создана с нужным описанием и сразу деактивируем ее
        boolean deactiveResult = testIterateAllRecyclerItems(popupDecorView, R.id.news_list_recycler_view, news.description, "Deactive event"); //Деактивируем новость

        if (deactiveResult) {
            //если деактивация успешна, то проверяем, что новость с определенным текстом по-прежнему в списке и деактивирована
            boolean testResult = testIterateAllRecyclerItems(popupDecorView, R.id.news_list_recycler_view, news.description, "Check availability and not active"); //Проверяем наличие деактивированной новости с определенным текстом
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

    @Test
    //2.6 Вызов из меню NEWS страницы Control panel, добавление нового события (новости), отфильтровать события по категории и убедиться, что отфильтрованные события принадлежат выбранной категории
    public void filterNewsByCategoryTest()  {
        //Кликаем "все новости"
        attemptOfClickAllNews();
        //создаем новую новость с определенным описанием descriptionForNewsToChoose
        attemptCreationNewEvent(news, true);

        //Нажимаем кнопку "Фильтровать"
        idWaitToBeDisplayedAndThenMaybeClick(R.id.filter_news_material_button, true);
        //Нажимаем на выбор категории
        ViewInteraction categoryChooseButton = onView(allOf(withId(R.id.text_input_end_icon), withParent(withParent(withParent(withParent(withId(R.id.news_item_category_text_input_layout)))))));
        objectOrIdCheckToBeDisplayedAndThenClick(categoryChooseButton, 0);
        //из выпавшего меню выбираем категорию созданной новости
        onView(withText(news.category))
                .inRoot(withDecorView(Matchers.not(popupDecorView)))
                .check(matches(isDisplayed()))
                .perform(click());

        //Нажимаем кнопку "FILTER"
        objectOrIdCheckToBeDisplayedAndThenClick(null, R.id.filter_button);

        //Проверяем, что все новости отфильтрованы по категории новой новости
        testIterateRecyclerItemsByCondition(R.id.news_list_recycler_view, news.category, "Filter category");
    }

    @Test
    //2.7 Вызов из меню NEWS страницы Control panel, добавление нового события (новости), отфильтровать события по дате и убедиться, что отфильтрованные события принадлежат выбранной дате
    public void filterNewsByDateTest()  {
        //Кликаем "все новости"
        attemptOfClickAllNews();
        //создаем новую новость
        attemptCreationNewEvent(news, true);

        //Нажимаем кнопку "Фильтровать"
        idWaitToBeDisplayedAndThenMaybeClick(R.id.filter_news_material_button, true);

        objectOrIdCheckToBeDisplayedAndThenReplaceText(null, R.id.news_item_publish_date_start_text_input_edit_text, news.date);
        objectOrIdCheckToBeDisplayedAndThenReplaceText(null, R.id.news_item_publish_date_end_text_input_edit_text, news.date);

        //Нажимаем кнопку "FILTER"
        objectOrIdCheckToBeDisplayedAndThenClick(null, R.id.filter_button);

        //Проверяем, что все новости отфильтрованы по дате новой новости
        testIterateRecyclerItemsByCondition(R.id.news_list_recycler_view, news.date, "Filter date");
    }

    @Test
    //2.8 Вызов из меню NEWS страницы Control panel, добавление нового события (новости), отфильтровать события по активным событиям и убедиться, что отфильтрованные события активны
    public void filterNewsByActiveTest()  {
        //Кликаем "все новости"
        attemptOfClickAllNews();
        //создаем новую новость
        attemptCreationNewEvent(news, true);

        //Нажимаем кнопку "Фильтровать"
        idWaitToBeDisplayedAndThenMaybeClick(R.id.filter_news_material_button, true);

        //сбрасываем чек-бокс "NOT ACTIVE"
        objectOrIdCheckToBeDisplayedAndThenClick(null, R.id.filter_news_inactive_material_check_box);

        //Нажимаем кнопку "FILTER"
        objectOrIdCheckToBeDisplayedAndThenClick(null, R.id.filter_button);

        //Проверяем, что все новости отфильтрованы только по активным
        testIterateRecyclerItemsByCondition(R.id.news_list_recycler_view, "ACTIVE", "Filter active");
    }

    @Test
    //2.9 Вызов из меню NEWS страницы Control panel, добавление нового события (новости), сделать его неактивным, отфильтровать события по неактивным событиям и убедиться, что отфильтрованные события не активны
    public void filterNewsByNotActiveTest()  {
        //Кликаем "все новости"
        attemptOfClickAllNews();
        //создаем новую новость
        attemptCreationNewEvent(news, true);

        //Проверяем, что новая новость создана с нужным описанием и сразу деактивируем ее
        boolean deactiveResult = testIterateAllRecyclerItems(popupDecorView, R.id.news_list_recycler_view, news.description, "Deactive event"); //Деактивируем новость

        if (deactiveResult) {
            //Нажимаем кнопку "Фильтровать"
            idWaitToBeDisplayedAndThenMaybeClick(R.id.filter_news_material_button, true);

            //сбрасываем чек-бокс "NOT ACTIVE"
            objectOrIdCheckToBeDisplayedAndThenClick(null, R.id.filter_news_active_material_check_box);

            //Нажимаем кнопку "FILTER"
            objectOrIdCheckToBeDisplayedAndThenClick(null, R.id.filter_button);

            //Проверяем, что все новости отфильтрованы только по активным
            testIterateRecyclerItemsByCondition(R.id.news_list_recycler_view, "NOT ACTIVE", "Filter active");
        } else {
            //если отмена удаления НЕ успешна, то заваливаем тест
            assertTrue("This test is failed", false);
        }
    }

    @Test
    //2.10-2.11 Вызов из меню NEWS страницы Control panel, добавление трех новых событий (новостей) на сегодня, через месяц и через год, отсортировать события и убедиться, что события в списке идут по порядку
    public void sortNewsTest()  {

        //Кликаем "все новости"
        attemptOfClickAllNews();
        //создаем новую новость на сегодня
        attemptCreationNewEvent(news, true);
        //создаем новую новость в следующем месяце
        NewsData news2 = new NewsData();
        NewsData.chooseRandomDataForCreationNews(news2, 30);
        attemptCreationNewEvent(news2, false);
        //создаем новую новость в следующем году
        NewsData news3 = new NewsData();
        NewsData.chooseRandomDataForCreationNews(news3, 365);
        attemptCreationNewEvent(news3, false);

        //Нажимаем кнопку "Сортировать"
        idWaitToBeDisplayedAndThenMaybeClick(R.id.sort_news_material_button, true);

        //Проверяем, как расположены созданные новости (получаем массив номеров в отсортированном списке)
        int[] numbers1 = testIterateRecyclerItemsBySort(R.id.news_list_recycler_view, news, news2, news3);

        //Нажимаем кнопку "Сортировать" повторно
        idWaitToBeDisplayedAndThenMaybeClick(R.id.sort_news_material_button, true);

        //Проверяем, как расположены созданные новости (получаем массив номеров в отсортированном в обратном порядке списке)
        int[] numbers2 = testIterateRecyclerItemsBySort(R.id.news_list_recycler_view, news, news2, news3);

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

    @Test
    //2.12 Вызов из меню About первой ссылки
    public void firstAboutLinkTest() throws InterruptedException {
        //ждем, пока на странице появится ссылка "All news" и не нажимаем на нее
        idWaitToBeDisplayedAndThenMaybeClick(R.id.all_news_text_view, false);

        //Жмем на кнопку МЕНЮ
        idWaitToBeDisplayedAndThenMaybeClick(R.id.main_menu_image_button, true);

        //Из всплывающего меню жмем раздел ABOUT
        onView(withText("About"))
                .inRoot(withDecorView(Matchers.not(popupDecorView)))
                .check(matches(isDisplayed()))
                .perform(click());

        //Дожидаемся, когда будет доступна первая ссылка, но не кликаем ее
        idWaitToBeDisplayedAndThenMaybeClick(R.id.about_privacy_policy_value_text_view, false);
        //включаем прослушку intents
        Intents.init();
        //кликаем на ссылку
        Espresso.onView(withId(R.id.about_privacy_policy_value_text_view))
                        .perform(click());
        //проверяем, что было вызвано приложение для открытия ссылки нужного сайта
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(hasHost("vhospice.org"))
        ));
        //выключаем прослушку intents
        Intents.release();
    }

    @Test
    //2.13 Вызов из меню About второй ссылки
    public void secondAboutLinkTest() throws InterruptedException {
        //ждем, пока на странице появится ссылка "All news" и не нажимаем на нее
        idWaitToBeDisplayedAndThenMaybeClick(R.id.all_news_text_view, false);

        //Жмем на кнопку МЕНЮ
        idWaitToBeDisplayedAndThenMaybeClick(R.id.main_menu_image_button, true);

        //Из всплывающего меню жмем раздел ABOUT
        onView(withText("About"))
                .inRoot(withDecorView(Matchers.not(popupDecorView)))
                .check(matches(isDisplayed()))
                .perform(click());

        //Дожидаемся, когда будет доступна первая ссылка, но не кликаем ее
        idWaitToBeDisplayedAndThenMaybeClick(R.id.about_terms_of_use_value_text_view, false);
        //включаем прослушку intents
        Intents.init();
        //кликаем на ссылку
        Espresso.onView(withId(R.id.about_terms_of_use_value_text_view))
                .perform(click());
        //проверяем, что было вызвано приложение для открытия ссылки нужного сайта
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(hasHost("vhospice.org"))
        ));
        //выключаем прослушку intents
        Intents.release();
    }
}

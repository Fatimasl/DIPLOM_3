package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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
//import static ru.iteco.fmhandroid.ui.Helper.categoryForNewsToChoose;
//import static ru.iteco.fmhandroid.ui.Helper.categoryValues;
//import static ru.iteco.fmhandroid.ui.Helper.chooseRandomDataForCreationNews;
import static ru.iteco.fmhandroid.ui.Helper.idWaitToBeDisplayedAndThenMaybeClick;

import static ru.iteco.fmhandroid.ui.Helper.objectOrIdCheckToBeDisplayedAndThenClick;
import static ru.iteco.fmhandroid.ui.Helper.registeredLogin;
import static ru.iteco.fmhandroid.ui.Helper.registeredPassword;
import static ru.iteco.fmhandroid.ui.Helper.testIterateAllRecyclerItems;
import static ru.iteco.fmhandroid.ui.Helper.testIterateRecyclerItemsByCondition;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import ru.iteco.fmhandroid.R;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AppActivityMainMenuTest {
    @Rule
    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);
    private static View popupDecorView;
    private String categoryForNewsToChoose;
    private String descriptionForNewsToChoose;

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
        NewsData news = new NewsData(); // создаем контейнер

        NewsData.chooseRandomDataForCreationNews(news);
        categoryForNewsToChoose = news.category;
        descriptionForNewsToChoose = news.description;

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
        //Выбираем рандомно категорию для нового события (новости) и создаем уникальное ее описание
//        String categoryForNewsToChoose = "";
//        String descriptionForNewsToChoose = "";
        //chooseRandomDataForCreationNews(categoryForNewsToChoose, descriptionForNewsToChoose);
//        Random random = new Random();
//        int randomIndex = random.nextInt(categoryValues.length); // Получаем случайный индекс
//        String categoryForNewsToChoose = categoryValues[randomIndex]; // Выбираем значение по индексу
//
//        //создаем описание для новой новости
//        String descriptionForNewsToChoose = RandomGenerator.generateRandomString();
        //создаем новую новость с определенным описанием descriptionForNewsToChoose
        attemptCreationNewEvent(popupDecorView, categoryForNewsToChoose, descriptionForNewsToChoose);
        //Проверяем, что новая новость создана с нужным описанием
        boolean testResult = testIterateAllRecyclerItems(popupDecorView, R.id.news_list_recycler_view, descriptionForNewsToChoose, "Check availability"); //Проверяем наличие новости с определенным текстом

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

        //создаем описание для новой новости
        //String descriptionForNewsToChoose = RandomGenerator.generateRandomString();
        //создаем новую новость с определенным описанием descriptionForNewsToChoose
        attemptCreationNewEvent(popupDecorView, categoryForNewsToChoose, descriptionForNewsToChoose);
        //Проверяем, что новая новость создана с нужным описанием и сразу удаляем ее
        boolean removeResult = testIterateAllRecyclerItems(popupDecorView, R.id.news_list_recycler_view, descriptionForNewsToChoose, "Remove event"); //Удаляем новость с определенным текстом

        if (removeResult) {
            //если удаление успешно, то проверяем, что новости с определенным текстом нет в списке
            boolean testResult = testIterateAllRecyclerItems(popupDecorView, R.id.news_list_recycler_view, descriptionForNewsToChoose, "Check availability"); //Проверяем наличие новости с определенным текстом
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
        //создаем описание для новой новости
        //String descriptionForNewsToChoose = RandomGenerator.generateRandomString();
        //создаем новую новость с определенным описанием descriptionForNewsToChoose
        attemptCreationNewEvent(popupDecorView, categoryForNewsToChoose, descriptionForNewsToChoose);
        //Проверяем, что новая новость создана с нужным описанием и сразу удаляем ее, но потом не подтверждаем удаления
        boolean cancelRemoveResult = testIterateAllRecyclerItems(popupDecorView, R.id.news_list_recycler_view, descriptionForNewsToChoose, "Cancel removing event"); //Удаляем новость, но не подтверждаем этого действия

        if (cancelRemoveResult) {
            //если отмена удаления успешна, то проверяем, что новость с определенным текстом по-прежнему в списке
            boolean testResult = testIterateAllRecyclerItems(popupDecorView, R.id.news_list_recycler_view, descriptionForNewsToChoose, "Check availability"); //Проверяем наличие новости с определенным текстом
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
        //создаем описание для новой новости
        //String descriptionForNewsToChoose = RandomGenerator.generateRandomString();
        //создаем новую новость с определенным описанием descriptionForNewsToChoose
        attemptCreationNewEvent(popupDecorView, categoryForNewsToChoose, descriptionForNewsToChoose);
        //Проверяем, что новая новость создана с нужным описанием и сразу деактивируем ее
        boolean deactiveResult = testIterateAllRecyclerItems(popupDecorView, R.id.news_list_recycler_view, descriptionForNewsToChoose, "Deactive event"); //Деактивируем новость

        if (deactiveResult) {
            //если деактивация успешна, то проверяем, что новость с определенным текстом по-прежнему в списке и деактивирована
            boolean testResult = testIterateAllRecyclerItems(popupDecorView, R.id.news_list_recycler_view, descriptionForNewsToChoose, "Check availability and not active"); //Проверяем наличие деактивированной новости с определенным текстом
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
        //создаем описание для новой новости
        //String descriptionForNewsToChoose = RandomGenerator.generateRandomString();
        //создаем новую новость с определенным описанием descriptionForNewsToChoose
        attemptCreationNewEvent(popupDecorView, categoryForNewsToChoose, descriptionForNewsToChoose);

        //Нажимаем кнопку "Фильтровать"
        idWaitToBeDisplayedAndThenMaybeClick(R.id.filter_news_material_button, true);
//        //Нажимаем на выбор категории
//        idWaitToBeDisplayedAndThenMaybeClick(R.id.news_item_category_text_auto_complete_text_view, true);
        ViewInteraction categoryChooseButton = onView(allOf(withId(R.id.text_input_end_icon), withParent(withParent(withParent(withParent(withId(R.id.news_item_category_text_input_layout)))))));
        objectOrIdCheckToBeDisplayedAndThenClick(categoryChooseButton, 0);
//        //из выпавшего меню выбираем категорию созданной новости
        onView(withText(categoryForNewsToChoose))
                .inRoot(withDecorView(Matchers.not(popupDecorView)))
                .check(matches(isDisplayed()))
                .perform(click());

//        closeSoftKeyboard();
//        hideKeyboard();

        //ViewInteraction categoryView = onView(withId(R.id.news_item_category_text_auto_complete_text_view));
        //objectCheckToBeDisplayedAndThenReplaceText(categoryView, categoryForNewsToChoose);

        //Нажимаем кнопку "FILTER"
        objectOrIdCheckToBeDisplayedAndThenClick(null, R.id.filter_button);

        //Проверяем, что все новости отфильтрованы по категории новой новости
        testIterateRecyclerItemsByCondition(popupDecorView, R.id.news_list_recycler_view, categoryForNewsToChoose, "Filter category");

    }


}

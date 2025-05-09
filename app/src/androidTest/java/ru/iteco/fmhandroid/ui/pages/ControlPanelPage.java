package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.getRecyclerViewItemCount;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.idWaitToBeDisplayedAndThenMaybeClick;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.objectOrIdCheckToBeDisplayedAndThenClick;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.waitDisplayed;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.waitingPeriod;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.withRecyclerView;
import static ru.iteco.fmhandroid.ui.pages.CreatingNewsPage.nav_host_fragment;
import static ru.iteco.fmhandroid.ui.pages.CreatingNewsPage.textOfSwitcherActive;

import android.view.View;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.Matchers;

import java.util.concurrent.atomic.AtomicReference;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.GetTextFromMaterialTextViewAction;
import ru.iteco.fmhandroid.ui.data.NewsGenerator;

public class ControlPanelPage {
    static int add_news_image_view = R.id.add_news_image_view;
    static int news_list_recycler_view = R.id.news_list_recycler_view;
    static int news_item_description_text_view = R.id.news_item_description_text_view;
    static int view_news_item_image_view = R.id.view_news_item_image_view;
    static int delete_news_item_image_view = R.id.delete_news_item_image_view;
    static int edit_news_item_image_view = R.id.edit_news_item_image_view;
    static int news_item_published_text_view = R.id.news_item_published_text_view;
    static int filter_news_material_button = R.id.filter_news_material_button;
    static int news_item_title_text_view = R.id.news_item_title_text_view;
    static int news_item_publication_date_text_view = R.id.news_item_publication_date_text_view;

    static String textButtonOK = "OK";
    static String textButtonCANCEL = "CANCEL";
    static String textOfSwitcherNotActive = "Not active";
    public static String textForCompairActive = "ACTIVE";
    public static String textForCompairNotActive = "NOT ACTIVE";

    public ControlPanelPage() {
        //убеждаемся, что на странице есть кнопка добавления события
        onView(isRoot()).perform(waitDisplayed(add_news_image_view, waitingPeriod));
    }

    public CreatingNewsPage clickPlus() {
        //нажимаем на кнопку добавления события
        onView(withId(add_news_image_view)).perform(click());
        return new CreatingNewsPage();
    }

    public FilterNewsPage clickFilter() {
        //нажимаем на кнопку фильтрации
        onView(withId(filter_news_material_button)).perform(click());
        return new FilterNewsPage();
    }

    public ControlPanelPage clickSorter() {
        //нажимаем на кнопку сортировки
        onView(withId(R.id.sort_news_material_button)).perform(click());
        return new ControlPanelPage();
    }

    @Step("Перебираем элементы объекта RecyclerView из списка, пока не найдем нужный по описанию. И тогда выполняем определенное в whatToDo действие")
    public static boolean testIterateAllRecyclerItems(View popupDecorView, String descriptionForNewsToChoose, String whatToDo) {
        // Создаём AtomicReference для хранения текста
        AtomicReference<String> textReference = new AtomicReference<>();
        //id элементов для перечня новостей
        int recyclerId = news_list_recycler_view;
        int idDescription = news_item_description_text_view;
        int idItemImage = view_news_item_image_view;

        int itemCount = getRecyclerViewItemCount(recyclerId);

        for (int i = 0; i < itemCount; i++) {
            // Прокручиваем к нужной позиции
            onView(ViewMatchers.withId(recyclerId))
                    .perform(RecyclerViewActions.scrollToPosition(i));

            // Проверяем отображение элемента списка
            onView(withRecyclerView(recyclerId).atPosition(i))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            try {
                // проверка видимости текста описания элемента
                onView(withRecyclerView(recyclerId)
                        .atPositionOnView(i, idDescription))
                        .check(matches(isDisplayed()));
            } catch (AssertionError e) {
                // Кликаем на кнопку открытия описания элемента
                onView(withRecyclerView(recyclerId)
                        .atPositionOnView(i, idItemImage))
                        .perform(click());
            }

            // Докручиваем к нужной позиции, чтобы она отображалась полностью, вместе с текстом элемента
            onView(ViewMatchers.withId(recyclerId))
                    .perform(RecyclerViewActions.scrollToPosition(i));

            // проверка текста описания элемента
            onView(withRecyclerView(recyclerId)
                    .atPositionOnView(i, idDescription))
                    .perform(new GetTextFromMaterialTextViewAction(textReference)); // Тут мы получаем текст из i-ого элемента

            // Извлекаем текст из AtomicReference
            String extractedText = textReference.get();

            if (descriptionForNewsToChoose.equals(extractedText)) {
                //если нашли элемент с нужным текстом, то
                if (whatToDo == "Check availability") { //проверяем наличие элемента с определенным текстом?
                    return true;

                } else if (whatToDo == "Remove event") {//Удаляем новость с определенным текстом?
                    // Кликаем на кнопку удаления новости
                    onView(withRecyclerView(recyclerId)
                            .atPositionOnView(i, delete_news_item_image_view))
                            .perform(click());
                    //Подтверждаем удаление
                    onView(withText(textButtonOK))
                            .inRoot(withDecorView(Matchers.not(popupDecorView)))
                            .check(matches(isDisplayed()))
                            .perform(click());
                    return true;

                } else if (whatToDo == "Cancel removing event") {//Удаляем новость, но не подтверждаем этого действия
                    // Кликаем на кнопку удаления новости
                    onView(withRecyclerView(recyclerId)
                            .atPositionOnView(i, delete_news_item_image_view))
                            .perform(click());
                    //Отменяем удаление
                    onView(withText(textButtonCANCEL))
                            .inRoot(withDecorView(Matchers.not(popupDecorView)))
                            .check(matches(isDisplayed()))
                            .perform(click());
                    return true;

                } else if (whatToDo == "Deactive event") {//Деактивируем новость
                    // Кликаем на кнопку редактирования новости
                    onView(withRecyclerView(recyclerId)
                            .atPositionOnView(i, edit_news_item_image_view))
                            .perform(click());
                    //Убеждаемся, что переключатель Active видим и нажимаем на него
                    ViewInteraction switcherActive = onView(allOf(withText(textOfSwitcherActive), withParent(withParent(withParent(withParent(withParent(withId(nav_host_fragment))))))));
                    objectOrIdCheckToBeDisplayedAndThenClick(switcherActive, 0);

                    //Нажимаем кнопку Сохранить
                    idWaitToBeDisplayedAndThenMaybeClick(R.id.save_button, true);

                    return true;

                } else if (whatToDo == "Check availability and not active") {//Проверяем наличие деактивированной новости с определенным текстом
                    // Проверяем, что новость NOT ACTIVE
                    onView(withRecyclerView(recyclerId)
                            .atPositionOnView(i, news_item_published_text_view))
                            .check(matches(withText(textOfSwitcherNotActive)));

                    return true;

                }
            }
        }
        //если перебрали все элементы, но не нашли нужной новости, то
        return false;
    }

    @Step("Перебираем все элементы объекта RecyclerView из списка новостей и тогда выполняем определенное в whatToDo сравнение")
    public static void testIterateRecyclerItemsByCondition(String valueForCompair, String whatToDo) {
        int recyclerId = news_list_recycler_view;
        int itemCount = getRecyclerViewItemCount(recyclerId);

        for (int i = 0; i < itemCount; i++) {
            // Прокручиваем к нужной позиции
            onView(ViewMatchers.withId(recyclerId))
                    .perform(RecyclerViewActions.scrollToPosition(i));

            // Проверяем отображение элемента
            onView(withRecyclerView(recyclerId).atPosition(i))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

            if (whatToDo == "Filter category") { //проверяем фильтр по категории
                onView(withRecyclerView(recyclerId)
                        .atPositionOnView(i, news_item_title_text_view))
                        .check(matches(withText(valueForCompair)));
            } else if (whatToDo == "Filter date") { //проверяем фильтр по дате
                onView(withRecyclerView(recyclerId)
                        .atPositionOnView(i, news_item_publication_date_text_view))
                        .check(matches(withText(valueForCompair)));
            } else if (whatToDo == "Filter active") { //проверяем фильтр по активности
                onView(withRecyclerView(recyclerId)
                        .atPositionOnView(i, news_item_published_text_view))
                        .check(matches(withText(valueForCompair)));
            } else if (whatToDo == "Sort") { //проверяем сортировку по дате публикации
                onView(withRecyclerView(recyclerId)
                        .atPositionOnView(i, news_item_publication_date_text_view))
                        .check(matches(withText(valueForCompair)));
            }
        }
    }
    @Step("Перебираем все элементы объекта RecyclerView из списка новостей и записываем в массив номера позиций новостей с переданными описаниями")
    public static int[] testIterateRecyclerItemsBySort(NewsGenerator news, NewsGenerator news2, NewsGenerator news3) {
        int recyclerId = news_list_recycler_view;
        // Создаём AtomicReference для хранения текста
        AtomicReference<String> textReference = new AtomicReference<>();

        int[] numbers = new int[3]; // создаём массив на 3 элемента для хранения номеров упорядоченных новостей

        int itemCount = getRecyclerViewItemCount(recyclerId);

        for (int i = 0; i < itemCount; i++) {
            // Прокручиваем к нужной позиции
            onView(ViewMatchers.withId(recyclerId))
                    .perform(RecyclerViewActions.scrollToPosition(i));

            // Проверяем отображение элемента
            onView(withRecyclerView(recyclerId).atPosition(i))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

            try {
                // проверка видимости текста описания новости
                onView(withRecyclerView(recyclerId)
                        .atPositionOnView(i, news_item_description_text_view))
                        .check(matches(isDisplayed()));
            } catch (AssertionError e) {
                // Кликаем на кнопку открытия описания новости
                onView(withRecyclerView(recyclerId)
                                .atPositionOnView(i, view_news_item_image_view))
                        .perform(click());
            }

            // Докручиваем к нужной позиции, чтобы она отображалась полностью, вместе с текстом новости
            onView(ViewMatchers.withId(recyclerId))
                    .perform(RecyclerViewActions.scrollToPosition(i));

            // проверка текста описания новости
            onView(withRecyclerView(recyclerId)
                    .atPositionOnView(i, news_item_description_text_view))
                    .perform(new GetTextFromMaterialTextViewAction(textReference)); // Тут мы получаем текст из i-ой новости

            // Извлекаем текст из AtomicReference
            String extractedText = textReference.get();

            if (news.description.equals(extractedText)) {
                //если нашли новость с текстом новости на сегодня, то
                numbers[0] = i;
            } else if (news2.description.equals(extractedText)) {
                //если нашли новость с текстом новости через месяц, то
                numbers[1] = i;
            } else if (news3.description.equals(extractedText)) {
                //если нашли новость с текстом новости через месяц, то
                numbers[2] = i;
            }
        }
        return numbers;
    }
}

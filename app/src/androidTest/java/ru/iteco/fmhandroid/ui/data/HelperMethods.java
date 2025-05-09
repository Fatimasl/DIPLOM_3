package ru.iteco.fmhandroid.ui.data;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.TimeoutException;

import io.qameta.allure.kotlin.Step;

public class HelperMethods {

    public static int waitingPeriod = 10_000; //период ожидания появления элемента на странице
    /**
     * Perform action of waiting for a specific view id to be displayed.
     * @param viewId The id of the view to wait for.
     * @param millis The timeout of until when to wait for.
     */
    public static ViewAction waitDisplayed(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> has been displayed during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> matchId = withId(viewId);
                final Matcher<View> matchDisplayed = isDisplayed();

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        if (matchId.matches(child) && matchDisplayed.matches(child)) {
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }

    @Step("Делаем ожидание, пока на странице не появится объект с нужным Id (thisId). Если объект появился, то если mustBeClicked = true, то кликаем по объекту")
    public static void idWaitToBeDisplayedAndThenMaybeClick(int thisId, boolean mustBeClicked) {
        //ждем, пока на странице появится нужный элемент
        onView(isRoot()).perform(waitDisplayed(thisId, waitingPeriod));

        //если нужно, то кликаем по нему
        if (mustBeClicked) {
            ViewInteraction thisElement = onView(withId(thisId));
            thisElement.perform(click());
        }
    }

    @Step("Проверяем, что объект на странице есть и тогда кликаем по нему")
    public static void objectOrIdCheckToBeDisplayedAndThenClick(ViewInteraction objectView, int ourId) {
        if (objectView == null) {
            objectView = onView(withId(ourId));
        }
        objectView.check(matches(isDisplayed()));
        objectView.perform(click());
    }

    @Step("Проверяем, что объект на странице есть и тогда вводим в него текст")
    public static void objectOrIdCheckToBeDisplayedAndThenReplaceText(ViewInteraction objectView, int ourId, String textForInput) {
        if (objectView == null) {
            objectView = onView(withId(ourId));
        }
        objectView.check(matches(isDisplayed()));
        objectView.perform(replaceText(textForInput), closeSoftKeyboard());
    }

    @Step("Получаем общее количество элементов в объекте RecyclerView")
    public static int getRecyclerViewItemCount(int recyclerViewId) {
        final int[] itemCount = {0};

        Matcher<View> matcher = new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Get item count of RecyclerView");
            }

            @Override
            protected boolean matchesSafely(View view) {
                RecyclerView recyclerView = view.findViewById(recyclerViewId);
                itemCount[0] = recyclerView.getAdapter().getItemCount();
                return true;
            }
        };

        Espresso.onView(ViewMatchers.withId(recyclerViewId))
                .check(ViewAssertions.matches(matcher));

        return itemCount[0];
    }

    @Step("Определяем объект RecyclerViewMatcher")
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Step("Проверяем, что массив значений упорядочен по возрастанию")
    public static boolean isSortedAscending(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false; // если текущее число больше следующего — массив не отсортирован
            }
        }
        return true; // если не найдено нарушений — массив отсортирован
    }
    @Step("Проверяем, что массив значений упорядочен по убыванию")
    public static boolean isSortedDesending(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] < array[i + 1]) {
                return false; // если текущее число больше следующего — массив не отсортирован
            }
        }
        return true; // если не найдено нарушений — массив отсортирован
    }

    /*@Step("Перебираем элементы объекта RecyclerView из списка, пока не найдем нужный по описанию. И тогда выполняем определенное в whatToDo действие")
    public static boolean testIterateAllRecyclerItems(View popupDecorView, int recyclerId, int idDescription, int idItemImage, String descriptionForNewsToChoose, String whatToDo) {
        // Создаём AtomicReference для хранения текста
        AtomicReference<String> textReference = new AtomicReference<>();
        //id элементов по умолчанию (для перечня новостей)
//        int idDescription = R.id.news_item_description_text_view;
//        int idItemImage = R.id.view_news_item_image_view;
//
//        if (recyclerId == R.id.our_mission_item_list_recycler_view) {
//            idDescription = R.id.our_mission_item_title_text_view;//;
//            idItemImage = R.id.our_mission_item_open_card_image_button;
//        }

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

                } else if (whatToDo == "Check wise") {//Проверяем соответствие описания заголовку
                    // Проверяем, что мудрость имеет определенное описание
                    onView(withRecyclerView(recyclerId)
                            .atPositionOnView(i, R.id.our_mission_item_description_text_view))
                            .check(matches(withText(containsString(DataHelper.ourWiseDescription))));

                    return true;
                }
            }
        }
        //если перебрали все элементы, но не нашли нужной новости, то
        return false;
    }*/
}

package ru.iteco.fmhandroid.ui;

import static androidx.core.content.res.TypedArrayUtils.getText;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;

//import static ru.iteco.fmhandroid.ui.AppActivityMainMenuTest.popupDecorView;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import ru.iteco.fmhandroid.R;

public class Helper {
    static int waitingPeriod = 10_000;
    static String registeredLogin = "login2";
    static String registeredPassword = "password2";
    static String unregisteredLogin = "login";
    static String unregisteredPassword = "password";
    static String invalidLogin = "логин";
    static String invalidPassword = "пароль";
    static String textOf_nav_host_fragment = "Authorization";
    static String textOf_container_list_news_include_on_fragment_main = "News";
    static String hintOf_login_text_input_layout = "Login";
    static String hintOf_password_text_input_layout = "Password";
    static String descriptionOf_authorization_image_button = "Authorization";
    static String textOf_title = "Log out";
    static int wiseAmount = 8;
    static String textButtonOK = "OK";
    static String textButtonCANCEL = "CANCEL";
    static String textOfSwitcherActive = "Active";
    static String textOfSwitcherNotActive = "Not active";
    public static String OurWiseTitle = "";
    public static String ourWiseDescription = "";

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
    //делает релогин из приложения
    public static void makeRelogin(){
        //жмем на кнопку авторизации
        ViewInteraction appCompatImageButton = onView(allOf(withId(R.id.authorization_image_button), withContentDescription(descriptionOf_authorization_image_button)));
        objectOrIdCheckToBeDisplayedAndThenClick(appCompatImageButton, 0);

        //жмем на "Log out"
        ViewInteraction materialTextView = onView(allOf(withId(android.R.id.title), withText(textOf_title)));
        objectOrIdCheckToBeDisplayedAndThenClick(materialTextView, 0);
    }

    //делает попытку авторизации по переданной паре логин/пароль
    public static void attemptOfAuthorization(String login, String password) {
        try {
            //ждем, пока на странице появится кнопка "SIGN IN"
            onView(isRoot()).perform(waitDisplayed(R.id.enter_button, waitingPeriod));
        } catch (Exception e) {
            //если не дождались страницы ввода логина/пароля, то делаем предположение, что вход уже осуществлен
            //значит надо сделать релогин
            //ждем, пока на странице появится кнопка "Личный кабинет"
            onView(isRoot()).perform(waitDisplayed(R.id.authorization_image_button, waitingPeriod));
            makeRelogin();
            //ждем, пока на странице появится кнопка "SIGN IN"
            onView(isRoot()).perform(waitDisplayed(R.id.enter_button, waitingPeriod));
        }

        //убеждаемся, что на странице есть фрагмент с текстом textOf_nav_host_fragment
        ViewInteraction nameAuthText = onView(allOf(withText(textOf_nav_host_fragment), withParent(withParent(withId(R.id.nav_host_fragment)))));
        nameAuthText.check(matches(isDisplayed()));

        //вводим логин
        ViewInteraction loginView = onView(allOf(withHint(hintOf_login_text_input_layout), withParent(withParent(withId(R.id.login_text_input_layout)))));
        objectOrIdCheckToBeDisplayedAndThenReplaceText(loginView, 0, login);

        //вводим пароль
        ViewInteraction passwordView = onView(allOf(withHint(hintOf_password_text_input_layout), withParent(withParent(withId(R.id.password_text_input_layout)))));
        objectOrIdCheckToBeDisplayedAndThenReplaceText(passwordView, 0, password);

        //жмем кнопку "SIGN IN"
        ViewInteraction materialButton = onView(withId(R.id.enter_button));
        materialButton.perform(click());
    }
    //попытка нажатия на гиперссылку All news и проверка, что открылась страница со всеми новостями с кнопкой сортировки
    public static void attemptOfClickAllNews() {
        //ждем, пока на странице появится ссылка "All news" и нажимаем на нее
        idWaitToBeDisplayedAndThenMaybeClick(R.id.all_news_text_view, true);

        //убеждаемся, что появилась кнопка с сортировкой новостей, и не нажимаем на нее
        idWaitToBeDisplayedAndThenMaybeClick(R.id.sort_news_material_button, false);

    }

    //Процедура ожидает, пока на странице не появится объект с нужным Id (thisId)
    //Если объект появился, то если mustBeClicked = true, то кликаем по объекту
    public static void idWaitToBeDisplayedAndThenMaybeClick(int thisId, boolean mustBeClicked) {
        //ждем, пока на странице появится нужный элемент
        onView(isRoot()).perform(waitDisplayed(thisId, waitingPeriod));

        //если нужно, то кликаем по нему
        if (mustBeClicked) {
            ViewInteraction thisElement = onView(withId(thisId));
            thisElement.perform(click());
        }
    }

    //Проверяет, что объект на странице есть и тогда кликает по нему
    public static void objectOrIdCheckToBeDisplayedAndThenClick(ViewInteraction objectView, int ourId) {
        if (objectView == null) {
            objectView = onView(withId(ourId));
        }
        objectView.check(matches(isDisplayed()));
        objectView.perform(click());
    }

    //Проверяет, что объект на странице есть и тогда вводит в него текст
    public static void objectOrIdCheckToBeDisplayedAndThenReplaceText(ViewInteraction objectView, int ourId, String textForInput) {
        if (objectView == null) {
            objectView = onView(withId(ourId));
        }
        objectView.check(matches(isDisplayed()));
        objectView.perform(replaceText(textForInput), closeSoftKeyboard());
    }

//    public static void hideKeyboard() {
//        InputMethodManager imm = (InputMethodManager) ApplicationProvider.getApplicationContext()
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null && imm.isAcceptingText()) {
//            imm.hideSoftInputFromWindow(new View(ApplicationProvider.getApplicationContext()).getWindowToken(), 0);
//        }
//    }

    //Создает новое событие (новость) с определенным описанием
    public static void attemptCreationNewEvent(NewsData news, boolean firstEvent) {
        if (firstEvent) {
            //убеждаемся, что на странице есть кнопка редактирования события и нажимаем на нее
            idWaitToBeDisplayedAndThenMaybeClick(R.id.edit_news_material_button, true);
        }
        //убеждаемся, что на странице есть кнопка добавления события и нажимаем на нее
        idWaitToBeDisplayedAndThenMaybeClick(R.id.add_news_image_view, true);
        //убеждаемся, что на странице есть текст Creating, но НЕ нажимаем на него
        idWaitToBeDisplayedAndThenMaybeClick(R.id.custom_app_bar_sub_title_text_view, false);
        //убеждаемся, что на странице есть поле Категория и вводим в него нужную категорию
        objectOrIdCheckToBeDisplayedAndThenReplaceText(null, R.id.news_item_category_text_auto_complete_text_view, news.category);
        objectOrIdCheckToBeDisplayedAndThenReplaceText(null, R.id.news_item_title_text_input_edit_text, news.category);

        //вводим дату публикации
        objectOrIdCheckToBeDisplayedAndThenReplaceText(null, R.id.news_item_publish_date_text_input_edit_text, news.date);

        //вводим время публикации
        objectOrIdCheckToBeDisplayedAndThenReplaceText(null, R.id.news_item_publish_time_text_input_edit_text, news.time);

        //вводим описание публикации
        objectOrIdCheckToBeDisplayedAndThenReplaceText(null, R.id.news_item_description_text_input_edit_text, news.description);

        //Нажимаем кнопку Сохранить
        idWaitToBeDisplayedAndThenMaybeClick(R.id.save_button, true);

        //проверяем, что текст "Control panel" виден
        idWaitToBeDisplayedAndThenMaybeClick(R.id.nav_host_fragment, false);
    }

    //Получает общее количество элементов в объекте RecyclerView
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

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    public static boolean testIterateAllRecyclerItems(View popupDecorView, int recyclerId, String descriptionForNewsToChoose, String whatToDo) {
        // Создаём AtomicReference для хранения текста
        AtomicReference<String> textReference = new AtomicReference<>();
        //id элементов по умолчанию (для перечня новостей)
        int idDescription = R.id.news_item_description_text_view;
        int idItemImage = R.id.view_news_item_image_view;

        if (recyclerId == R.id.our_mission_item_list_recycler_view) {
            idDescription = R.id.our_mission_item_title_text_view;//;
            idItemImage = R.id.our_mission_item_open_card_image_button;
        }

        int itemCount = getRecyclerViewItemCount(recyclerId);

        for (int i = 0; i < itemCount; i++) {
            // Прокручиваем к нужной позиции
            Espresso.onView(ViewMatchers.withId(recyclerId))
                    .perform(RecyclerViewActions.scrollToPosition(i));

            // Проверяем отображение элемента
            Espresso.onView(withRecyclerView(recyclerId).atPosition(i))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            try {
                // проверка видимости текста описания новости
                onView(withRecyclerView(recyclerId)
                        .atPositionOnView(i, idDescription))
                        .check(matches(isDisplayed()));
            } catch (AssertionError e) {
                // Кликаем на кнопку открытия описания новости
                Espresso.onView(withRecyclerView(recyclerId)
                                .atPositionOnView(i, idItemImage))
                        .perform(click());
            }

            // Докручиваем к нужной позиции, чтобы она отображалась полностью, вместе с текстом новости
            Espresso.onView(ViewMatchers.withId(recyclerId))
                    .perform(RecyclerViewActions.scrollToPosition(i));

            // проверка текста описания новости
            onView(withRecyclerView(recyclerId)
                    .atPositionOnView(i, idDescription))
                    .perform(new GetTextFromMaterialTextViewAction(textReference)); // Тут мы получаем текст из i-ой новости

            // Извлекаем текст из AtomicReference
            String extractedText = textReference.get();

            if (descriptionForNewsToChoose.equals(extractedText)) {
                //если нашли новость с нужным текстом, то
                if (whatToDo == "Check availability") { //проверяем наличие новости с определенным текстом
                    return true;

                } else if (whatToDo == "Remove event") {//Удаляем новость с определенным текстом
                    // Кликаем на кнопку удаления новости
                    Espresso.onView(withRecyclerView(recyclerId)
                                    .atPositionOnView(i, R.id.delete_news_item_image_view))
                            .perform(click());
                    //Подтверждаем удаление
                    onView(withText(textButtonOK))
                            .inRoot(withDecorView(Matchers.not(popupDecorView)))
                            .check(matches(isDisplayed()))
                            .perform(click());
                    return true;

                } else if (whatToDo == "Cancel removing event") {//Удаляем новость, но не подтверждаем этого действия
                    // Кликаем на кнопку удаления новости
                    Espresso.onView(withRecyclerView(recyclerId)
                                    .atPositionOnView(i, R.id.delete_news_item_image_view))
                            .perform(click());
                    //Отменяем удаление
                    onView(withText(textButtonCANCEL))
                            .inRoot(withDecorView(Matchers.not(popupDecorView)))
                            .check(matches(isDisplayed()))
                            .perform(click());
                    return true;

                } else if (whatToDo == "Deactive event") {//Деактивируем новость
                    // Кликаем на кнопку редактирования новости
                    Espresso.onView(withRecyclerView(recyclerId)
                                    .atPositionOnView(i, R.id.edit_news_item_image_view))
                            .perform(click());
                    //Убеждаемся, что переключатель Active видим и нажимаем на него
                    ViewInteraction switcherActive = onView(allOf(withText(textOfSwitcherActive), withParent(withParent(withParent(withParent(withParent(withId(R.id.nav_host_fragment))))))));
                    objectOrIdCheckToBeDisplayedAndThenClick(switcherActive, 0);

                    //Нажимаем кнопку Сохранить
                    idWaitToBeDisplayedAndThenMaybeClick(R.id.save_button, true);

                    return true;

                } else if (whatToDo == "Check availability and not active") {//Проверяем наличие деактивированной новости с определенным текстом
                    // Проверяем, что новость NOT ACTIVE
                    Espresso.onView(withRecyclerView(recyclerId)
                                    .atPositionOnView(i, R.id.news_item_published_text_view))
                            .check(matches(withText(textOfSwitcherNotActive)));

                    return true;

                } else if (whatToDo == "Check wise") {//Проверяем соответствие описания заголовку
                    // Проверяем, что мудрость имеет определенное описание
                    Espresso.onView(withRecyclerView(recyclerId)
                                    .atPositionOnView(i, R.id.our_mission_item_description_text_view))
                            .check(matches(withText(containsString(Helper.ourWiseDescription))));

                    return true;
                }
            }
        }
        //если перебрали все элементы, но не нашли нужной новости, то
        return false;
    }

    public static void testIterateRecyclerItemsByCondition(int recyclerId, String valueForCompair, String whatToDo) {

        int itemCount = getRecyclerViewItemCount(recyclerId);

        for (int i = 0; i < itemCount; i++) {
            // Прокручиваем к нужной позиции
            Espresso.onView(ViewMatchers.withId(recyclerId))
                    .perform(RecyclerViewActions.scrollToPosition(i));

            // Проверяем отображение элемента
            Espresso.onView(withRecyclerView(recyclerId).atPosition(i))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

            if (whatToDo == "Filter category") { //проверяем фильтр по категории
                Espresso.onView(withRecyclerView(recyclerId)
                        .atPositionOnView(i, R.id.news_item_title_text_view))
                        .check(matches(withText(valueForCompair)));
            } else if (whatToDo == "Filter date") { //проверяем фильтр по дате
                Espresso.onView(withRecyclerView(recyclerId)
                                .atPositionOnView(i, R.id.news_item_publication_date_text_view))
                        .check(matches(withText(valueForCompair)));
            } else if (whatToDo == "Filter active") { //проверяем фильтр по активности
                Espresso.onView(withRecyclerView(recyclerId)
                                .atPositionOnView(i, R.id.news_item_published_text_view))
                        .check(matches(withText(valueForCompair)));
            } else if (whatToDo == "Sort") { //проверяем сортировку по дате публикации
                Espresso.onView(withRecyclerView(recyclerId)
                                .atPositionOnView(i, R.id.news_item_publication_date_text_view))
                        .check(matches(withText(valueForCompair)));
            }
        }
    }

    public static int[] testIterateRecyclerItemsBySort(int recyclerId, NewsData news, NewsData news2, NewsData news3) {
        // Создаём AtomicReference для хранения текста
        AtomicReference<String> textReference = new AtomicReference<>();

        int[] numbers = new int[3]; // создаём массив на 3 элемента для хранения номеров упорядоченных новостей

        int itemCount = getRecyclerViewItemCount(recyclerId);

        for (int i = 0; i < itemCount; i++) {
            // Прокручиваем к нужной позиции
            Espresso.onView(ViewMatchers.withId(recyclerId))
                    .perform(RecyclerViewActions.scrollToPosition(i));

            // Проверяем отображение элемента
            Espresso.onView(withRecyclerView(recyclerId).atPosition(i))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

            try {
                // проверка видимости текста описания новости
                onView(withRecyclerView(recyclerId)
                        .atPositionOnView(i, R.id.news_item_description_text_view))
                        .check(matches(isDisplayed()));
            } catch (AssertionError e) {
                // Кликаем на кнопку открытия описания новости
                Espresso.onView(withRecyclerView(recyclerId)
                                .atPositionOnView(i, R.id.view_news_item_image_view))
                        .perform(click());
            }

            // Докручиваем к нужной позиции, чтобы она отображалась полностью, вместе с текстом новости
            Espresso.onView(ViewMatchers.withId(recyclerId))
                    .perform(RecyclerViewActions.scrollToPosition(i));

            // проверка текста описания новости
            onView(withRecyclerView(recyclerId)
                    .atPositionOnView(i, R.id.news_item_description_text_view))
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
    //проверяет, что массив значений упорядочен по возрастанию
    public static boolean isSortedAscending(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false; // если текущее число больше следующего — массив не отсортирован
            }
        }
        return true; // если не найдено нарушений — массив отсортирован
    }
    //проверяет, что массив значений упорядочен по убыванию
    public static boolean isSortedDesending(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] < array[i + 1]) {
                return false; // если текущее число больше следующего — массив не отсортирован
            }
        }
        return true; // если не найдено нарушений — массив отсортирован
    }
}

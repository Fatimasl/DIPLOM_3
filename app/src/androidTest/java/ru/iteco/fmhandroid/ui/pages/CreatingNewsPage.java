package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.idWaitToBeDisplayedAndThenMaybeClick;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.objectOrIdCheckToBeDisplayedAndThenReplaceText;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.NewsGenerator;

public class CreatingNewsPage {
    static int custom_app_bar_sub_title_text_view = R.id.custom_app_bar_sub_title_text_view;
    static  int news_item_category_text_auto_complete_text_view = R.id.news_item_category_text_auto_complete_text_view;
    static  int news_item_title_text_input_edit_text = R.id.news_item_title_text_input_edit_text;
    static  int news_item_publish_date_text_input_edit_text = R.id.news_item_publish_date_text_input_edit_text;
    static int news_item_publish_time_text_input_edit_text = R.id.news_item_publish_time_text_input_edit_text;
    static int news_item_description_text_input_edit_text = R.id.news_item_description_text_input_edit_text;
    public static int nav_host_fragment = R.id.nav_host_fragment;
    static int save_button = R.id.save_button;
    public static String textOfSwitcherActive = "Active";

    public CreatingNewsPage() {
        //убеждаемся, что на странице есть текст Creating
        onView(withId(custom_app_bar_sub_title_text_view)).check(matches(isDisplayed()));
    }

    public ControlPanelPage fillOutNews(NewsGenerator news) {
        //убеждаемся, что на странице есть поле Категория и вводим в него нужную категорию
        objectOrIdCheckToBeDisplayedAndThenReplaceText(null, news_item_category_text_auto_complete_text_view, news.category);
        objectOrIdCheckToBeDisplayedAndThenReplaceText(null, news_item_title_text_input_edit_text, news.category);

        //вводим дату публикации
        objectOrIdCheckToBeDisplayedAndThenReplaceText(null, news_item_publish_date_text_input_edit_text, news.date);

        //вводим время публикации
        objectOrIdCheckToBeDisplayedAndThenReplaceText(null, news_item_publish_time_text_input_edit_text, news.time);

        //вводим описание публикации
        objectOrIdCheckToBeDisplayedAndThenReplaceText(null, news_item_description_text_input_edit_text, news.description);

        //Нажимаем кнопку Сохранить
        idWaitToBeDisplayedAndThenMaybeClick(save_button, true);

        return new ControlPanelPage();
    }
}

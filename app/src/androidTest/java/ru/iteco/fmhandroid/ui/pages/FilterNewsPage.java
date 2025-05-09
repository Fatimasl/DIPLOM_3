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
import static ru.iteco.fmhandroid.ui.data.HelperMethods.objectOrIdCheckToBeDisplayedAndThenClick;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.objectOrIdCheckToBeDisplayedAndThenReplaceText;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.waitDisplayed;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.waitingPeriod;

import android.view.View;

import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matchers;

import ru.iteco.fmhandroid.R;

public class FilterNewsPage {
    static int filter_news_title_text_view = R.id.filter_news_title_text_view;
    static int text_input_end_icon = R.id.text_input_end_icon;
    static int news_item_category_text_input_layout = R.id.news_item_category_text_input_layout;
    static int news_item_publish_date_start_text_input_edit_text = R.id.news_item_publish_date_start_text_input_edit_text;
    static int news_item_publish_date_end_text_input_edit_text = R.id.news_item_publish_date_end_text_input_edit_text;
    static int filter_news_inactive_material_check_box = R.id.filter_news_inactive_material_check_box;
    static int filter_news_active_material_check_box = R.id.filter_news_active_material_check_box;
    static int filter_button = R.id.filter_button;
    public FilterNewsPage() {
        //убеждаемся, что на странице есть текст Filter news
        onView(isRoot()).perform(waitDisplayed(filter_news_title_text_view, waitingPeriod));
    }

    public ControlPanelPage fillOutFilterCategory(View popupDecorView, String categoryToChoose) {
        //Нажимаем на выбор категории
        ViewInteraction categoryChooseButton = onView(allOf(withId(text_input_end_icon), withParent(withParent(withParent(withParent(withId(news_item_category_text_input_layout)))))));
        objectOrIdCheckToBeDisplayedAndThenClick(categoryChooseButton, 0);
        //из выпавшего меню выбираем нужную категорию
        onView(withText(categoryToChoose))
                .inRoot(withDecorView(Matchers.not(popupDecorView)))
                .check(matches(isDisplayed()))
                .perform(click());

        //Нажимаем кнопку "FILTER"
        objectOrIdCheckToBeDisplayedAndThenClick(null, filter_button);
        return new ControlPanelPage();
    }

    public ControlPanelPage fillOutFilterDate(String dateToChoose) {
        objectOrIdCheckToBeDisplayedAndThenReplaceText(null, news_item_publish_date_start_text_input_edit_text, dateToChoose);
        objectOrIdCheckToBeDisplayedAndThenReplaceText(null, news_item_publish_date_end_text_input_edit_text, dateToChoose);

        //Нажимаем кнопку "FILTER"
        objectOrIdCheckToBeDisplayedAndThenClick(null, filter_button);
        return new ControlPanelPage();
    }

    public ControlPanelPage fillOutFilterOnlyActive() {
        objectOrIdCheckToBeDisplayedAndThenClick(null, filter_news_inactive_material_check_box);
        //Нажимаем кнопку "FILTER"
        objectOrIdCheckToBeDisplayedAndThenClick(null, filter_button);
        return new ControlPanelPage();
    }

    public ControlPanelPage fillOutFilterOnlyNotActive() {
        objectOrIdCheckToBeDisplayedAndThenClick(null, filter_news_active_material_check_box);
        //Нажимаем кнопку "FILTER"
        objectOrIdCheckToBeDisplayedAndThenClick(null, filter_button);
        return new ControlPanelPage();
    }
}

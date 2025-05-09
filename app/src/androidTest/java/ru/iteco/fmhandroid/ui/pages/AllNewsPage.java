package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.idWaitToBeDisplayedAndThenMaybeClick;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.waitDisplayed;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.waitingPeriod;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.NewsGenerator;

public class AllNewsPage {
    static int edit_news_material_button = R.id.edit_news_material_button;
    static int sort_news_material_button = R.id.sort_news_material_button;

    public AllNewsPage() {
        //убеждаемся, что появилась кнопка с сортировкой новостей
        onView(isRoot()).perform(waitDisplayed(sort_news_material_button, waitingPeriod));
    }

    @Step("Создаем новое событие (новость) с определенным описанием")
    public static ControlPanelPage attemptCreationNewEvent(boolean firstEvent) {
        if (firstEvent) {
            //убеждаемся, что на странице есть кнопка с "карандашом" и нажимаем на нее
            idWaitToBeDisplayedAndThenMaybeClick(edit_news_material_button, true);
        }
        return new ControlPanelPage();
    }
}

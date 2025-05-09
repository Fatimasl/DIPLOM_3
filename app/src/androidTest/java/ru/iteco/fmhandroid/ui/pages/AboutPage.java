package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.idWaitToBeDisplayedAndThenMaybeClick;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.waitDisplayed;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.waitingPeriod;

import ru.iteco.fmhandroid.R;

public class AboutPage {
    static int about_privacy_policy_label_text_view = R.id.about_privacy_policy_label_text_view;
    static int about_privacy_policy_value_text_view = R.id.about_privacy_policy_value_text_view;
    static int about_terms_of_use_value_text_view = R.id.about_terms_of_use_value_text_view;
    public AboutPage() {
        //убеждаемся, что на странице есть кнопка добавления события
        onView(isRoot()).perform(waitDisplayed(about_privacy_policy_label_text_view , waitingPeriod));
    }

    public void clickPrivacyPolicyLink(){
        idWaitToBeDisplayedAndThenMaybeClick(about_privacy_policy_value_text_view, true);
    }

    public void clickTermsOfUseLink(){
        idWaitToBeDisplayedAndThenMaybeClick(about_terms_of_use_value_text_view, true);
    }
}

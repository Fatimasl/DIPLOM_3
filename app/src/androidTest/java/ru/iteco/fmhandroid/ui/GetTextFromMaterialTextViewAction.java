package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.view.View;

import androidx.test.espresso.ViewAction;

import com.google.android.material.textview.MaterialTextView;

import org.hamcrest.Matcher;

import java.util.concurrent.atomic.AtomicReference;

public class GetTextFromMaterialTextViewAction implements ViewAction {
    private final AtomicReference<String> result;

    public GetTextFromMaterialTextViewAction(AtomicReference<String> result) {
        this.result = result;
    }

    @Override
    public Matcher<View> getConstraints() {
        return isDisplayed(); // Только если элемент отображается
    }

    @Override
    public void perform(androidx.test.espresso.UiController uiController, View view) {
        if (view instanceof MaterialTextView) {
            MaterialTextView materialTextView = (MaterialTextView) view;
            String text = materialTextView.getText().toString(); // Извлекаем текст из MaterialTextView
            result.set(text); // Сохраняем текст в AtomicReference
        }
    }

    @Override
    public String getDescription() {
        return "Get text from EditText";
    }
}

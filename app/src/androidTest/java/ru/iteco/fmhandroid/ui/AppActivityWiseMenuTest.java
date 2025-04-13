package ru.iteco.fmhandroid.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.iteco.fmhandroid.ui.Helper.attemptOfAuthorization;
import static ru.iteco.fmhandroid.ui.Helper.getRecyclerViewItemCount;
import static ru.iteco.fmhandroid.ui.Helper.idWaitToBeDisplayedAndThenMaybeClick;
import static ru.iteco.fmhandroid.ui.Helper.objectOrIdCheckToBeDisplayedAndThenClick;
import static ru.iteco.fmhandroid.ui.Helper.registeredLogin;
import static ru.iteco.fmhandroid.ui.Helper.registeredPassword;
import static ru.iteco.fmhandroid.ui.Helper.testIterateAllRecyclerItems;
import static ru.iteco.fmhandroid.ui.Helper.wiseAmount;

import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import ru.iteco.fmhandroid.R;

public class AppActivityWiseMenuTest {

    @Rule
    public ActivityScenarioRule<AppActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AppActivity.class);
    private static View popupDecorView;

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
    public void EveryTimeTheseTests() {
        //Делаем при необходимости перелогин
        attemptOfAuthorization(registeredLogin, registeredPassword);
        //ждем, пока на странице появится ссылка "All news" и не нажимаем на нее
        idWaitToBeDisplayedAndThenMaybeClick(R.id.all_news_text_view, false);

        //Жмем на кнопку "бабочка"
        objectOrIdCheckToBeDisplayedAndThenClick(null, R.id.our_mission_image_button);

        Espresso.onView(withId(R.id.our_mission_title_text_view))
                .check(matches(withText("Love is all")));
    }

    @Test
    //3.1 Вызов из меню "Бабочка" списка мудростей и проверка их количества
    public void wiseAmountTest()  {
        //определим сколько элементов с списке мудростей
        int actualItemCount = getRecyclerViewItemCount(R.id.our_mission_item_list_recycler_view);
        //сравним с ожидаемым значением
        assertEquals(wiseAmount, actualItemCount);
    }

    @Test
    //3.2 Вызов из меню "Бабочка" списка мудростей, поиск нужной мудрости по заголовку и проверка соответствия описания мудрости заголовку
    public void wiseContentTest()  {
        //выберем рандомно число от 1 до 8 и сопоставим ему текст заголовка и части описания мудрости

        Random random = new Random();
        int number = random.nextInt(8) + 1; // от 0 до 7 → +1 = от 1 до 8

        switch (number) {
            case 1:
                Helper.OurWiseTitle = "«Хоспис для меня - это то, каким должен быть мир.\"";
                Helper.ourWiseDescription = "Ну, идеальное устройство мира в моих глазах. Где никто не оценивает, никто не осудит, где говоришь";
                break;
            case 2:
                Helper.OurWiseTitle = "Хоспис в своем истинном понимании - это творчество";
                Helper.ourWiseDescription = "Нет шаблона и стандарта, есть только дух, который живет в разных домах по-разному";
                break;
            case 3:
                Helper.OurWiseTitle = "“В хосписе не работают плохие люди” В.В. Миллионщикова\"";
                Helper.ourWiseDescription = "Все сотрудники хосписа - это адвокаты пациента, его прав и потребностей";
                break;
            case 4:
                Helper.OurWiseTitle = "«Хоспис – это философия, из которой следует сложнейшая наука медицинской помощи умирающим и искусство ухода, в котором сочетается компетентность и любовь» С. Сандерс";
                Helper.ourWiseDescription = "Творчески и осознанно подойти к проектированию опыта умирания";
                break;
            case 5:
                Helper.OurWiseTitle = "Служение человеку с теплом, любовью и заботой";
                Helper.ourWiseDescription = "Если пациента нельзя вылечить, это не значит, что для него ничего нельзя сделать. То, что кажется мелочью, пустяком в жизни здорового человека - для пациента имеет огромный смысл";
                break;
            case 6:
                Helper.OurWiseTitle = "\"Хоспис продлевает жизнь, дает надежду, утешение и поддержку.\"";
                Helper.ourWiseDescription = "Хоспис - это мои новые друзья. Полная перезагрузка жизненных ценностей";
                break;
            case 7:
                Helper.OurWiseTitle = "\"Двигатель хосписа - милосердие плюс профессионализм\"\n" +
                        "А.В. Гнездилов, д.м.н., один из пионеров хосписного движения.";
                Helper.ourWiseDescription = "Делай добро... А добро заразительно. По-моему, все люди милосердны";
                break;
            case 8:
                Helper.OurWiseTitle = "Важен каждый!";
                Helper.ourWiseDescription = "Каждый, кто оказывается в стенах хосписа, имеет огромное значение в жизни хосписа";
                break;
            default:
        }

        //Проверяем, что мудрость с определенным заголовком существует и ей соответствует определенное описание (поиск по части описания)
        boolean testResult = testIterateAllRecyclerItems(popupDecorView, R.id.our_mission_item_list_recycler_view, Helper.OurWiseTitle, "Check wise"); //Проверяем наличие новости с определенным текстом

        if (testResult) {
            //завершаем тест с успехом
            assertTrue("This test passed intentionally", true);
        } else {
            //заваливаем тест
            assertTrue("This test is failed", false);
        }
    }
}

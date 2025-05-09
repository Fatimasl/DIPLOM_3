package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringContains.containsString;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.getRecyclerViewItemCount;
import static ru.iteco.fmhandroid.ui.data.HelperMethods.withRecyclerView;

import android.view.View;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.data.DataHelper;
import ru.iteco.fmhandroid.ui.data.GetTextFromMaterialTextViewAction;

public class LoveIsAllPage {
    static int our_mission_title_text_view = R.id.our_mission_title_text_view;
    static int our_mission_item_list_recycler_view = R.id.our_mission_item_list_recycler_view;
    static int our_mission_item_description_text_view = R.id.our_mission_item_description_text_view;
    public static String titleLoveIsAll = "Love is all";
    public LoveIsAllPage(){
        onView(withId(our_mission_title_text_view))
                .check(matches(withText(titleLoveIsAll)));
    }

    public int getWiseItemCount(){
        return getRecyclerViewItemCount(our_mission_item_list_recycler_view);
    }
    public static void chooseRandomWiseNote(){
        //выберем рандомно число от 1 до 8 и сопоставим ему текст заголовка и части описания цитаты

        Random random = new Random();
        int number = random.nextInt(8) + 1; // от 0 до 7 → +1 = от 1 до 8

        switch (number) {
            case 1:
                DataHelper.OurWiseTitle = "«Хоспис для меня - это то, каким должен быть мир.\"";
                DataHelper.ourWiseDescription = "Ну, идеальное устройство мира в моих глазах. Где никто не оценивает, никто не осудит, где говоришь";
                break;
            case 2:
                DataHelper.OurWiseTitle = "Хоспис в своем истинном понимании - это творчество";
                DataHelper.ourWiseDescription = "Нет шаблона и стандарта, есть только дух, который живет в разных домах по-разному";
                break;
            case 3:
                DataHelper.OurWiseTitle = "“В хосписе не работают плохие люди” В.В. Миллионщикова\"";
                DataHelper.ourWiseDescription = "Все сотрудники хосписа - это адвокаты пациента, его прав и потребностей";
                break;
            case 4:
                DataHelper.OurWiseTitle = "«Хоспис – это философия, из которой следует сложнейшая наука медицинской помощи умирающим и искусство ухода, в котором сочетается компетентность и любовь» С. Сандерс";
                DataHelper.ourWiseDescription = "Творчески и осознанно подойти к проектированию опыта умирания";
                break;
            case 5:
                DataHelper.OurWiseTitle = "Служение человеку с теплом, любовью и заботой";
                DataHelper.ourWiseDescription = "Если пациента нельзя вылечить, это не значит, что для него ничего нельзя сделать. То, что кажется мелочью, пустяком в жизни здорового человека - для пациента имеет огромный смысл";
                break;
            case 6:
                DataHelper.OurWiseTitle = "\"Хоспис продлевает жизнь, дает надежду, утешение и поддержку.\"";
                DataHelper.ourWiseDescription = "Хоспис - это мои новые друзья. Полная перезагрузка жизненных ценностей";
                break;
            case 7:
                DataHelper.OurWiseTitle = "\"Двигатель хосписа - милосердие плюс профессионализм\"\n" +
                        "А.В. Гнездилов, д.м.н., один из пионеров хосписного движения.";
                DataHelper.ourWiseDescription = "Делай добро... А добро заразительно. По-моему, все люди милосердны";
                break;
            case 8:
                DataHelper.OurWiseTitle = "Важен каждый!";
                DataHelper.ourWiseDescription = "Каждый, кто оказывается в стенах хосписа, имеет огромное значение в жизни хосписа";
                break;
            default:
        }
    }

    @Step("Перебираем элементы объекта RecyclerView из списка, пока не найдем нужный по описанию. И тогда выполняем определенное в whatToDo действие")
    public static boolean testIterateAllRecyclerItems(View popupDecorView, String descriptionForNewsToChoose, String whatToDo) {
        // Создаём AtomicReference для хранения текста
        AtomicReference<String> textReference = new AtomicReference<>();
        //id элементов для перечня новостей
        int recyclerId = our_mission_item_list_recycler_view;
        int idDescription = R.id.our_mission_item_title_text_view;
        int idItemImage = R.id.our_mission_item_open_card_image_button;

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
                if (whatToDo == "Check wise") {
                    // Проверяем, что мудрость имеет определенное описание
                    onView(withRecyclerView(recyclerId)
                            .atPositionOnView(i, our_mission_item_description_text_view))
                            .check(matches(withText(containsString(DataHelper.ourWiseDescription))));

                    return true;
                }
            }
        }
        //если перебрали все элементы, но не нашли нужной новости, то
        return false;
    }
}

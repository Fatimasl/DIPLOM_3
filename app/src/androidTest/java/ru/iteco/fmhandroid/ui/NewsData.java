package ru.iteco.fmhandroid.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import io.qameta.allure.kotlin.Step;

public class NewsData {
    public String category;
    public String description;
    public String date;
    public String time;

    // Массив значений категорий для тестирования
    private static String[] categoryValues = {"Объявление", "День рождения", "Зарплата", "Профсоюз", "Праздник", "Массаж", "Благодарность", "Нужна помощь"};

    @Step("Создаем набор значений (категория, описание, дата, время) для создания новой новости (события)")
    public static void chooseRandomDataForCreationNews(NewsData data, int amountOfDays) {
        //выбираем случайным образом категорию для новой новости
        Random random = new Random();
        int randomIndex = random.nextInt(categoryValues.length); // Получаем случайный индекс
        data.category = categoryValues[randomIndex]; // Выбираем значение по индексу

        //создаем уникальное описание для новой новости
        data.description = RandomGenerator.generateRandomString();

        //текущая дата плюс amountOfDays дней
        data.date = LocalDate.now().plusDays(amountOfDays).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        //текущее время
        data.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}

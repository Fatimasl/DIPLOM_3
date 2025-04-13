package ru.iteco.fmhandroid.ui;

import java.util.Random;

public class NewsData {
    public String category;
    public String description;
    // Массив значений категорий для тестирования
    private static String[] categoryValues = {"Объявление", "День рождения", "Зарплата", "Профсоюз", "Праздник", "Массаж", "Благодарность", "Нужна помощь"};
    public static void chooseRandomDataForCreationNews(NewsData data) {
        //выбираем случайным образом категорию для новой новости
        Random random = new Random();
        int randomIndex = random.nextInt(categoryValues.length); // Получаем случайный индекс
        data.category = categoryValues[randomIndex]; // Выбираем значение по индексу

        //создаем уникальное описание для новой новости
        data.description = RandomGenerator.generateRandomString();
    }
}

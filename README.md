# Приложение «Мобильный хоспис»: Описание процедуры запуска авто-тестов:

Запуск всех тестов: ./gradlew connectedAndroidTest

Запуск тестов только из класса тестов на аутентификацию:
./gradlew app:connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=ru.iteco.fmhandroid.ui.package.AppActivityAuthorizationTest

Запуск тестов только из класса тестов возможностей главного меню:
./gradlew app:connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=ru.iteco.fmhandroid.ui.package.AppActivityMainMenuTest

Запуск тестов только из класса тестов про цитаты:
./gradlew app:connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=ru.iteco.fmhandroid.ui.package.AppActivityWiseMenuTest

Для просмотра отчетов allure необходимо распаковать архив в папку allure-results в корне проекта и затем запустить в терминале команду 
allure serve
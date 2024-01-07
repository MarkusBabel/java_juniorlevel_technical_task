Інструкція щодо встановлення
Передумови:

Встановлений Java Development Kit (JDK) версії 8 або новішої.
Maven для збирання та управління проектом.
Кроки для встановлення:

Клонуйте репозиторій:
git clone https://github.com/yourusername/your-repository.git

Заходьте в кореневу папку проекту:
cd your-repository

Виконайте збирання проекту за допомогою Maven:
mvn clean install

Завантажте та встановіть PostgreSQL з офіційного сайту.
Створіть базу даних та користувача для цього проекту.

Заходьте до файлу application.properties у папці src/main/resources.
Змініть наступні параметри на ваші дані PostgreSQL:

spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password

Переконайтеся, що PostgreSQL запущений.

Запустіть додаток

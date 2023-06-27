# Searchengine

Поисковый движок на Java.

## Стек проекта:

• Spring Framework 
• JPA 
• JSOUP 
• SQL 
• Lucene Morphology Library 

Работает посредством индексирования сайтов с целью поиска ключевых слов на данных сайтах.

В пакет входит frontend, доступный по адресу http://localhost:8080/

## Перед использованием необходимо:

-  установить MySQL Server и создать на нем БД search_engine.

-  установить актуальный username и пароль в файле application.yaml.

-  скомпилировать проект с в IDE помощью класса Application.

-  Для доступа к управлению движком перейдите по адресу http://localhost:8080/  в вашем браузере.

##### Страница содержит три вкладки.

###### DASHBOARD
![Image alt](https://github.com/AlxndrKatz/SearchEngine/blob/main/readme/dashboard.png)

Данная вкладка отображает текущее состояние процессов.


###### MANAGEMENT
![Image alt](https://github.com/AlxndrKatz/SearchEngine/blob/main/readme/management.png)

Панель управления поисковым движком - доступны функции запуска/остановки индексации и обновления данных по отдельным страницам.

![Image alt](https://github.com/AlxndrKatz/SearchEngine/blob/main/readme/stopind.PNG)

После запуска индексации ее можно остановить - кнопка активации меняет свое состояние.



###### SEARCH
![Image alt](https://github.com/AlxndrKatz/SearchEngine/blob/main/readme/search.png)

В данной вкладке имеется поле для ввода искомых слов. Выше находится выпадающий список сайтов для поиска на конкретных сайтах из числа проиндексированных.


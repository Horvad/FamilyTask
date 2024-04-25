Приложение выполняет следующие функции:
1. Адрес - Информация о адресах, в системе
Добавление нового адреса:
Метод запроса: POST
URL запроса: http://localhost:8080/address
Параметры:
address;
number_flar;
nunber_house.
Получить страницу адресов:
Метод запроса: GET
URL запроса: http://localhost:8080/address
Получить информацию о пользователе:
Метод запроса: GET
URL запроса: http://localhost:8080/address
Параметр id
Редактировать информацию о пользователе:
Метод запроса: PUT
URL запроса: http://localhost:8080/address
Параметры: 
id;
version;
address;
number_flar;
nunber_house.
Метод запроса: DELETE
URL запроса: http://localhost:8080/address
Параметры: id, version


2. Родитель - Информация о адресах, и месте проживания родителя
Добавление нового родителя:
Метод запроса: POST
URL запроса: http://localhost:8080/parent
Параметры:
name;
id_address.
Получить страницу адресов:
Метод запроса: GET
URL запроса: http://localhost:8080/parent
Получить информацию о пользователе:
Метод запроса: GET
URL запроса: http://localhost:8080/parent
Параметр id
Редактировать информацию о пользователе:
Метод запроса: PUT
URL запроса: http://localhost:8080/parent
Параметры: 
id;
version;
name;
id_address;

3. Ребенок - Информация о детях, и и их родителях
Добавление новой ребенка:
Метод запроса: POST
URL запроса: http://localhost:8080/parent
Параметры:
name;
{id_parent}.
Получить страницу адресов:
Метод запроса: GET
URL запроса: http://localhost:8080/parent
Получить информацию о пользователе:
Метод запроса: GET
URL запроса: http://localhost:8080/parent
Параметр id
Редактировать информацию о пользователе:
Метод запроса: PUT
URL запроса: http://localhost:8080/parent
Параметры: 
id;
version;
name;
{id_parent};
Метод запроса: PUT
URL запроса: http://localhost:8080/parent
Параметры: 
id;
version;

4. Family - Информация о семье при запросе по адресу(все родители и их дети), родителя(все дите и адрес), ребенку(все родители и адреса)
Получение семьи по id родителя:
Метод запроса: GET
URL запроса: http://localhost:8080/family
Параметры:
id; parent

Получение семьи по id ребенка:
Метод запроса: GET
URL запроса: http://localhost:8080/family
Параметры:
id; children

Получение семьи по id адреса
Метод запроса: GET
URL запроса: http://localhost:8080/family
Параметры:
id; address

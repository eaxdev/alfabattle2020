Есть 1 стул и 2 входных файла JSON:

person.json - данные о клиентах, формат одной записи представлен ниже:
```json
{
     "ID":"29",
     "DocId":"702821510",
     "FIO":"Phoebe Whitehouse",
     "Birthday":"7/12/1971",
     "Salary":"201.02",
     "Gender":"F"
}
```
, где:
ID - уникальный номер клиента, <br>
DocId - документ (формат 9 цифр)<br>
FIO - ФИО<br>
Birthday - дата рождения в формате MM/dd/yyyy<br>
Salary - средний заработок клиента, который записан в сотых, т.е. 201.02 означает 20102 руб.<br>
Gender - пол

loans.json - данные о клиентах, формат одной записи представлен ниже:
```json
{
     "Loan":"631553",
     "PersonId":"68",
     "Amount":"201.02",
     "StartDate":"6/1/2019",
     "Period":"1"
}
```
, где:<br>
Loan - номер договора,<br>
PersonId  - номер клиента<br>
Amount  - сумма кредита, которая записана в сотых, т.е. 201.02 означает 20 102<br>
StartDate - дата начала кредита в формате MM/dd/yyyy<br>
Period  - срок кредита в годах<br>

### Задачи
Из указанных выше файлов прочитать информацию, преобразовать ее и положить в ElasticSearch в разные индексы.
Реализовать REST интерфейс для загрузки данных в ElasticSearch, а так же для извлечения данных. Приложение должно быть доступно на http://IP:8083

Для запуска ElasticSearch:
mkdir task4 ; cd task4
wget https://raw.githubusercontent.com/evgenyshiryaev/alfa-battle-resources/master/task4/docker-compose.yml
docker-compose up -d
ElasticSearch доступен на IP:9200

Для остановки (в папке task4):
docker-compose down

* IP - внешний IP виртуальной машины.
* Все ресурсы лежат тут: https://github.com/evgenyshiryaev/alfa-battle-resources/tree/master/task4

Уточнения
При загрузке данных в ElasticSearch должны быть произведены следующие изменения:
persons
Birthday - должна храниться в виде 1945-05-03
Salary - должна храниться в рублях, а не в сотых
 loans
PersonId - вместо это поля, должно появиться поле Document, которое будет хранить DocId из Persons.
Amount - сумма кредита должна быть в рублях, а не в сотых.
StartDate - дата начала кредта, должна храниться в виде 1945-05-03
Period - срок кредита, должен быть в месяцах
Ожидаемый интерфейс
Хелсчек
GET http://IP:8083/admin/health
200
{"status":"UP"}
Загрузка клиентов
Загрузка преобразованных данных клиентов в ElasicSearch.

POST http://IP:8083/loans/loadPersons

200
{"status":"OK"}

Загрузка договоров
Загрузка преобразованных данных клиентов в ElasicSearch.
При этом, для того чтобы на договоре проставить Document, необходимо сделать запрос в ElasticSearch для получения номер документа клиента.

POST http://IP:8083/loans/loadLoans

200
{"status":"OK"}
Вывести информацию о клиенте

GET http://IP:8083/loans/getPerson/855406656/

200
```json
{
   "docid": "855406656",
   "fio": "Celina Jackson",
   "birthday": "1961-05-22",
   "salary": 69106.0,
   "gender": "F"
}
```

400 (в случае отсутствия клиента)
{
   "status": "person not found"
}
Вывести информацию о договоре
GET http://IP:8083/loans/getLoan/692826/
200
```json
{
   "loan": "692826",
   "amount": 448900,
   "document": "027665876",
   "startdate": "2017-01-16",
   "period": 48
}
```

400 (в случае отсутствия договора)
{
   "status": "loan not found"
}
Запрос на кредитную историю клиента
Вывод информации о всех кредитных договорах клиента, с указанным номером документа - Document.

GET http://IP:8083/loans/creditHistory/737767072/
200
```json
{
   "countLoan": 4,
   "sumAmountLoans": 1058400.0,
   "loans": [
       {
           "loan": "434224",
           "amount": 7100,
           "document": "737767072",
           "startdate": "2019-09-18",
           "period": 12
       },
       {
           "loan": "917105",
           "amount": 283600,
           "document": "737767072",
           "startdate": "2019-12-22",
           "period": 12
       },
       {
           "loan": "692147",
           "amount": 300800,
           "document": "737767072",
           "startdate": "2016-08-01",
           "period": 24
       },
       {
           "loan": "145020",
           "amount": 466900,
           "document": "737767072",
           "startdate": "2017-01-16",
           "period": 36
       }
   ]
}

```

,где:
countLoan - количество договоров
sumAmountLoans - сумма всех договоров
loans - массив договоров
Получение списка кредитных договоров, которые закрыты на первое число текущего месяца
GET http://IP:8083/loans/creditClosed
200
```json
[
   {
       "loan": "222398",
       "amount": 265400,
       "document": "074658188",
       "startdate": "2017-09-22",
       "period": 12
   },
  
       "loan": "826942",
       "amount": 329400,
       "document": "788117788",
       "startdate": "2016-01-29",
       "period": 48
   },
...
]
```
Получение информации по клиентам и договорам
Ответ должен быть отсортирован по дате рождения, по убыванию (сортировку производить при запросе из ElasticSearch).

GET http://IP:8083/loans/loansSortByPersonBirthday
200
```json

[
   {
       "id": null,
       "docid": "840704451",
       "fio": "John Isaac",
       "birthday": "19.08.1989",
       "salary": 58295.0,
       "gender": "M",
       "loans": [
           {
               "loan": "771916",
               "amount": 337600,
               "document": "840704451",
               "startdate": "2019-11-09",
               "period": 48
           },
           {
               "loan": "504544",
               "amount": 358900,
               "document": "840704451",
               "startdate": "2018-06-10",
               "period": 36
           },
           {
               "loan": "699247",
               "amount": 464400,
               "document": "840704451",
               "startdate": "2018-10-30",
               "period": 36
           },
           {
               "loan": "783101",
               "amount": 139300,
               "document": "840704451",
               "startdate": "2017-02-19",
               "period": 36
           }
       ]
   },
   {
       "id": null,
       "docid": "023665566",
       "fio": "Denny Tanner",
       "birthday": "25.03.1989",
       "salary": 80713.0,
       "gender": "M",
       "loans": [
           {
               "loan": "631553",
               "amount": 403000,
               "document": "023665566",
               "startdate": "2019-06-01",
               "period": 12
           },
           {
               "loan": "598452",
               "amount": 198500,
               "document": "023665566",
               "startdate": "2015-09-28",
               "period": 36
           },
           {
               "loan": "151915",
               "amount": 13600,
               "document": "023665566",
               "startdate": "2019-06-15",
               "period": 12
           },
           {
               "loan": "368342",
               "amount": 350500,
               "document": "023665566",
               "startdate": "2017-02-06",
               "period": 48
           },
           {
               "loan": "633056",
               "amount": 482900,
               "document": "023665566",
               "startdate": "2016-07-01",
               "period": 12
           }
       ]
   },
...
]
```

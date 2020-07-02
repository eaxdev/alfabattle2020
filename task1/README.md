# Task1 

## Preparing

- Get certificate and private key from [here](https://api.alfabank.ru/start)
- Convert RSA private key from `PKCS#1` to `PKCS#8` format via `openssl`:
  ```shell script
  openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in pkcs1.key -out pkcs8.key
  ```
  
## Description

Требуется реализовать сервис для получения данных о банкоматах Альфа-Банка.
Сервис должен предоставлять REST API на http://IP:8080.

Файл с описанием API – api.json.

Для получение базовых данных необходима интеграция решения с Альфа-Банк API Сервис банкоматов.

Документация: https://api.alfabank.ru/node/238

Для доступа к API необходима регистрация и несложная настройка.

Детали: https://api.alfabank.ru/start

* IP - внешний IP виртуальной машины.

* Все ресурсы лежат тут: https://github.com/evgenyshiryaev/alfa-battle-resources/tree/master/task1

### Задачи
1. Получение данных банкомата по deviceId
Запрос: GET http://IP:8080/atms/{deviceId}

Ответ:

- 200 AtmResponse

- 404 ErrorResponse (если банкомат не найден)


Пример: GET http://IP:8080/atms/153463

200

```json
{
  "deviceId": 153463,
  "latitude": "55.6610213",
  "longitude": "37.6309405",
  "city": "Москва",
  "location": "Старокаширское ш., 4, корп. 10",
  "payments": false
}
```

Пример: GET http://IP:8080/atms/1

404
```json
{
  "status": "atm not found"
}
```
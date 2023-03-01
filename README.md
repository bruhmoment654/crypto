# Crypto

Данное приложение является импровизированной крипто-биржей, которая может хранить данные о пользователях, их кошельках, а также валюте на этих кошельках.
Обмен данными происходит с помощью REST-запросов, содержащих параметры в формате json.

Приложение защищено с помощью Spring Security и Jwt-токена, который генерируется при входе пользователя и позволяет авторизоваться в системе. У каждого пользователя есть
роль (USER или ADMIN), дающая доступ к определенным функциям приложения. Без jwt-токена доступ к функция не доступен.

jwt токен в postman указывается в authorization -> bearer token

запросы по адресу /admin/** доступны только администраторам.

##Выполненные доп. задания:

> Подключена база данных PostgreSQL, конфигурация в application.properties</br>
> Сервис может возвращать данные в формате json или xml, для этого нужно добавить заголовок **accept:application/xml**</br>
> Добавлен Spring Security</br>

### Регистрация

**Method: POST http://localhost:8080/auth/register**

request:
```
{
    "username": "new_user",
    "email": "user@mail.ru"
}
```
response:
```
{
    "secret_key": "8rkevq02iI9po20lVO6jbJp4nf2o8z9DacNgKvZ5"
}
```
При регистрации генерируется секретный ключ, по которому в дальнейшем будет происходить аутентификация, без него пользователь не сможет получить jwt токен.
По умолчанию пользователю назначается роль USER, для изменения необходимо поменять роль пользователя в бд (в поле role указать значение 1) 

По сути этот ключ нужен только для аутентификации и пополнения конкретного кошелька, в остальных случаях хватает jwt-токена. Однако, так как в задании просили использовать его в запросах, я оставил его.

### Вход

**Method: POST http://localhost:8080/auth/login**

request:
```
{
    "secret_key" : "w1AXJB7dguK9pnw5VDhnJL905455A1eDclLsqr9b"
}
```
response:
```
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjc3NjU4Njc4LCJleHAiOjE2NzgyNjM0Nzh9.svLoy8AdZLCK3OuZ2tViXRqCVCV_E2EEnFrWK50ivnE"
}
```
при аутентификации генерируется jwt-токен, по которому возможна авторизация в системе. Токен хранит в себе логин пользователя и дату генерации. Дальнейшие запросы будут производиться с этим токеном в header'е.

### Создание кошелька

**Method: POST http://localhost:8080/wallet/create**

request:
**header**
Authorization :  Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjc3NTk1Njk0LCJleHAiOjE2NzgyMDA0OTR9.JWD-hIWnV6bMvcol1r39Z6F33G07oSx-cy_nqQQpF1Y
(jwt токен для авторизации)

```
{
    "secret_key" : "AHamV6nFCzf77bWM4vvrKHMCQx4X0gXM9Fh2jbo9",
    "wallet_name" : "BTC_wallet",
    "currency" : "BTC"
}
```

response: 
```
{
    "id": 52,
    "currency": "BTC",
    "name": "BTC_wallet",
    "sum": 0.0
}
```

для создания кошелька нужно авторизоваться с помощью jwt токена, указав ключ пользователя, к которому будет привязан кошелек.

### Пополнение кошелька

**Method: POST http://localhost:8080/wallet/addCurrency**

request:
**header**
Authorization :  Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjc3NTk1Njk0LCJleHAiOjE2NzgyMDA0OTR9.JWD-hIWnV6bMvcol1r39Z6F33G07oSx-cy_nqQQpF1Y
(jwt токен для авторизации)

```
{
	"secret_key": "w1AXJB7dguK9pnw5VDhnJL905455A1eDclLsqr9b",
	"RUB_wallet": "10000"
}
```

response:

```
{
    "RUB_wallet": "210000.0"
}
```

для пополнения кошелька нужно указать токен в заголовке, указать ключ, идентифицирующий пользователя, а также указать сумму.

### Вывод средств

**Method: POST http://localhost:8080/wallet/withdraw**

request:
**header**
Authorization :  Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjc3NTk1Njk0LCJleHAiOjE2NzgyMDA0OTR9.JWD-hIWnV6bMvcol1r39Z6F33G07oSx-cy_nqQQpF1Y
(jwt токен для авторизации)

```
{
	"secret_key": "w1AXJB7dguK9pnw5VDhnJL905455A1eDclLsqr9b",
	"currency": "RUB",
	"count": "69000",
	"credit_card": "1234 5678 9012 3456"
}
```

response:

```
{
    "RUB_wallet": "141000.0"
}
```

### Подсчет средств на всех кошельках в определенной валюте

**Method: POST http://localhost:8080/admin/checkSumInCurrency**

request:
**header**
Authorization :  Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjc3NTk1Njk0LCJleHAiOjE2NzgyMDA0OTR9.JWD-hIWnV6bMvcol1r39Z6F33G07oSx-cy_nqQQpF1Y
(jwt токен для авторизации)

```
{
	"currency": "BTC"
}
```
response:

{
    "BTC": 5.075001203608029
}

в сумме на кошельках было 3BTC, около 10000TON, 141000RUB

### Подсчет количества операций за промежуток времени

**Method: POST http://localhost:8080/admin/operations**

request:
**header**
Authorization :  Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjc3NTk1Njk0LCJleHAiOjE2NzgyMDA0OTR9.JWD-hIWnV6bMvcol1r39Z6F33G07oSx-cy_nqQQpF1Y
(jwt токен для авторизации)

```
{
	"date_from": "18.02.2023",
	"date_to": "19.03.2023"
}
```

response:

```
{
    "transaction_count": 19
}
```

в операции входят пополнение кошелька и снятие денег со счета

### Перевод с одного кошелька на другой (с учетом валюты)

**Method: POST http://localhost:8080/wallet/transfer**

request:
**header**
Authorization :  Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjc3NTk1Njk0LCJleHAiOjE2NzgyMDA0OTR9.JWD-hIWnV6bMvcol1r39Z6F33G07oSx-cy_nqQQpF1Y
(jwt токен для авторизации)

```
{
	"secret_key": "w1AXJB7dguK9pnw5VDhnJL905455A1eDclLsqr9b",
	"currency_from": "TON",
	"currency_to": "RUB",
	"amount": "1000"
}
```

response:

```
{
    "currency_to": "RUB",
    "amount_from": 1000.0,
    "amount_to": 184000.0,
    "currency_from": "TON"
}
```




  


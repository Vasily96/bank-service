# TransferMoney Demo Service

Это пример простого Spring Boot приложения, реализующего систему перевода денег между пользователями с кэшированием данных через Redis и миграциями через Liquibase.

#### Функциональность:
+ Перевод денег между пользователями
+ Добавление / удаление / обновление email и телефона
+ Кэширование пользователей через Redis
+ Начисление процентов на баланс аккаунтов по расписанию (10%, но не более 207% от начального депозита)
+ Миграции через Liquibase
+ JWT авторизация
+ OpenAPI / Swagger UI для документации

### Убедитесь, что установлено:
- Docker
- Docker Compose

### Запуск и сборка:

```bash
docker-compose build && docker-compose up -d
```
или


```bash
alias dcup='docker-compose build && docker-compose up -d --force-recreate && docker-compose logs -f server'
dcup
```

### Авторизация

Для получения JWT-токена отправьте  POST-запрос:

```bash
curl -X POST "http://localhost:8080/auth/login" \
     -H "Content-Type: application/json" \
     -d '{"login": "john@example.com", "password": "password"}'
```
Используй полученный токен в заголовке:
```bash
Authorization: Bearer <your_token>
```

### Примеры запросов:

Перевод денег
```bash
curl -X POST "http://localhost:8080/api/transfers" \
     -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
     -H "Content-Type: application/json" \
     -d '{"fromUserId": 1, "toUserId": 2, "amount": 100}'
```

Получить пользователя по ID
```bash
curl -X GET "http://localhost:8080/api/users/1" \
     -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

Swagger доступен по данной  [ссылке](http://localhost:8080/swagger-ui/index.html) 



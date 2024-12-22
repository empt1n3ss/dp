
# File Upload Service

Этот проект реализует REST-сервис для работы с облачным сервисом.

## Запуск проекта

### Локальный запуск
Для запуска приложения выполните команду:

```bash
java -jar target/dp-1.0-SNAPSHOT.jar
```

### Запуск через Docker

1. Соберите Docker-образ:

```bash
docker build -t file-upload-service .
```

2. Запустите контейнер с приложением:

```bash
docker run -d -p 8080:8080 file-upload-service
```

Приложение будет доступно по адресу: `http://localhost:8080`.

### Запуск через Docker-compose

1. Соберите Docker-образ:

```bash
docker-compose build
```

2. Запустите контейнер с приложением:

```bash
docker-compose up
```

## Описание API

### 1. Список файлов
**Endpoint**: `/files`  
**Method**: `GET`  
Описание: Получить список всех загруженных файлов.

Пример запроса:

```bash
curl -X GET http://localhost:8080/files
```

Пример ответа:

```json
[
  {
    "fileName": "test.txt",
    "size": 12345
  }
]
```

### 2. Загрузка файла
**Endpoint**: `/upload`  
**Method**: `POST`  
Описание: Загружает файл на сервер.

Пример запроса:

```bash
curl -X POST http://localhost:8080/upload   -F "file=@/path/to/file.txt"
```

Пример ответа:

```json
{
  "message": "File uploaded successfully"
}
```

### 3. Удаление файла
**Endpoint**: `/delete/{fileName}`  
**Method**: `DELETE`  
Описание: Удаляет файл по имени.

Пример запроса:

```bash
curl -X DELETE http://localhost:8080/delete/test.txt
```

Пример ответа:

```json
{
  "message": "File deleted successfully"
}
```

### 4. Авторизация
**Endpoint**: `/login`  
**Method**: `POST`  
Описание: Авторизация пользователя для доступа к сервису.

Пример запроса:

```bash
curl -X POST http://localhost:8080/login   -H "Content-Type: application/json"   -d '{"username": "user", "password": "password"}'
```

Пример ответа:

```json
{
  "token": "jwt_token_here"
}
```

## Порт

По умолчанию приложение работает на порту `8080`. Чтобы изменить порт, используйте переменную среды:

```bash
docker run -d -p 9090:8080 -e SERVER_PORT=9090 file-upload-service
```

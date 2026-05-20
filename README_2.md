## Лабораторная работа №7 полная инструкция по запуску

1. Скачать образы postgres 17
```bash
docker pull postgres:17
```
2. Создать контейнер на основе образа 
```bash
docker run --name proga \
  -e POSTGRES_USER=alexa \
  -e POSTGRES_PASSWORD=123 \
  -e POSTGRES_DB=proga \
  -p 6969:5432 \
  -d postgres:17
```
3. Создать jar файлы клиента и сервера. Для этого необходимо перейти в корневую директорию проекта и выполнить следующую команду (автоматически сгенерируется так же документация)
```bash
mvn clean compile package javadoc:aggregate
```
4. Если необходима документация введите следующую команду и перейдите в выбранный браузер
```bash
#Для firefox
firefox ./target/reports/apidocs/index.html
#Для Яндекс Браузера
yandex-browser ./target/reports/apidocs/index.html
```
5. Загрузите скрипт sql на схему
```bash
docker exec -i proga psql -U alexa -d proga < 01-init.sql
```
6. Запустите сервер
```bash
java -jar server/target/server-1.0-SNAPSHOT-jar-with-dependencies.jar
```
7. Запустите клиентскую часть приложения
```bash
java -jar client/target/client-1.0-SNAPSHOT-jar-with-dependencies.jar
```
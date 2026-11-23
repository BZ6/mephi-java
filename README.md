# Практические задания ко курсу Джавы

## JDK

```
java --version
openjdk 11.0.24 2024-07-16 LTS
OpenJDK Runtime Environment Corretto-11.0.24.8.1 (build 11.0.24+8-LTS)
OpenJDK Server VM Corretto-11.0.24.8.1 (build 11.0.24+8-LTS, mixed mode, emulated-client)
```

## Калькулятор

Код простейшего [калькулятора](Calculator.java) в одном файле.

Команда для компиляции и исполнения: `java Calculator.java`.

## Сервис метеорологических данных

Код [приложения](src/main/java/WeatherApp.java) для вывода метеорологических данных.

Нужен апи ключ от яндекса, вставьте его в [конфиг](config.properties).

Команда для компиляции и исполнения: `mvn compile exec:java`.

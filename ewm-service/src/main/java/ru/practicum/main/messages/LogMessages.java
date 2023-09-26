package ru.practicum.main.messages;

public enum LogMessages {
    TRY_UPDATE("Поступил запрос на обновление: {}"),
    TRY_GET_OBJECT("Поступил запрос на получение объекта по id: {}");

    private final String textLog;

    LogMessages(String textLog) {
        this.textLog = textLog;
    }

    @Override
    public String toString() {
        return textLog;
    }
}

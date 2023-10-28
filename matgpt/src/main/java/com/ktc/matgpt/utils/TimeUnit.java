package com.ktc.matgpt.utils;

public enum TimeUnit {
    SECOND("sec", 1),
    MINUTE("min", 60),
    HOUR("hour", 60 * 60),
    DAY("day", 24 * 60 * 60),
    WEEK("week", 7 * 24 * 60 * 60),
    MONTH("month", 30 * 24 * 60 * 60),
    YEAR("year", 365 * 24 * 60 * 60);

    private final String key;
    private final long seconds;

    TimeUnit(String key, long seconds) {
        this.key = key;
        this.seconds = seconds;
    }

    public String getKey() {
        return key;
    }

    public long getSeconds() {
        return seconds;
    }

    public static TimeUnit getAppropriateUnit(long seconds) {
        for (TimeUnit unit : TimeUnit.values()) {
            if (seconds < unit.getSeconds()) {
                return unit;
            }
        }
        return YEAR;
    }
}

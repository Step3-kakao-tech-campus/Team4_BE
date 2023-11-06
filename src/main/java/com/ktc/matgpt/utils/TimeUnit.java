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
        TimeUnit[] units = TimeUnit.values();
        for (int i = units.length - 1; i >= 0; i--) {
            if (seconds >= units[i].getSeconds()) {
                return units[i];
            }
        }
        return SECOND;
    }

}
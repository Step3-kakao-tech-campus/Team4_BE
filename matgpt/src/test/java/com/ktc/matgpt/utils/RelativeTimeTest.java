package com.ktc.matgpt.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RelativeTimeTest {

    @Test
    @DisplayName("30초전 리뷰 테스트")
    void testGetRelativeTimeFor30Seconds() {
        // 현재 시간으로부터 30초 전의 시간 생성
        LocalDateTime thirtySecondsAgo = LocalDateTime.now().minus(30, ChronoUnit.SECONDS);

        String result = getRelativeTime(thirtySecondsAgo);
        assertEquals("30secs ago", result);
    }

    @Test
    @DisplayName("45초전 리뷰 테스트")
    void testGetRelativeTimeFor45Seconds() {
        // 현재 시간으로부터 30초 전의 시간 생성
        LocalDateTime thirtySecondsAgo = LocalDateTime.now().minus(45, ChronoUnit.SECONDS);

        String result = getRelativeTime(thirtySecondsAgo);
        assertEquals("45secs ago", result);
    }

    @Test
    @DisplayName("10분전 리뷰 테스트")
    void testGetRelativeTimeFor10Minutes() {
        // 현재 시간으로부터 10분 전의 시간 생성
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minus(10, ChronoUnit.MINUTES);

        String result = getRelativeTime(tenMinutesAgo);
        assertEquals("10mins ago", result);
    }

    @Test
    @DisplayName("10분 30초 전 리뷰 테스트")
    void testGetRelativeTimeFor10Minute30Seconds() {
        // 현재 시간으로부터 10분 전의 시간 생성
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minus(630, ChronoUnit.SECONDS);

        String result = getRelativeTime(tenMinutesAgo);
        assertEquals("10mins ago", result);
    }

    @Test
    @DisplayName("1시간 33분 전 리뷰 테스트")
    void testGetRelativeTimeFor99Minutes() {
        // 현재 시간으로부터 10분 전의 시간 생성
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minus(5580, ChronoUnit.SECONDS);

        String result = getRelativeTime(tenMinutesAgo);
        assertEquals("1hour ago", result);
    }

    @Test
    @DisplayName("2시간 전 리뷰 테스트")
    void testGetRelativeTimeFor122Minutes() {
        // 현재 시간으로부터 10분 전의 시간 생성
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minus(7300, ChronoUnit.SECONDS);

        String result = getRelativeTime(tenMinutesAgo);
        assertEquals("2hours ago", result);
    }

    @Test
    @DisplayName("1년 이전 리뷰 테스트")
    void testGetRelativeTimeForBefore1year() {
        // 현재 시간으로부터 10분 전의 시간 생성
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minus(31000000, ChronoUnit.SECONDS);

        String result = getRelativeTime(tenMinutesAgo);
        assertEquals("11months ago", result);
    }

    @Test
    @DisplayName("1년 이후 리뷰 테스트")
    void testGetRelativeTimeForAfter1year() {
        // 현재 시간으로부터 10분 전의 시간 생성
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minus(31539000, ChronoUnit.SECONDS);

        String result = getRelativeTime(tenMinutesAgo);
        assertEquals("1year ago", result);
    }

    // 다른 시간 단위에 대한 테스트 케이스를 추가할 수 있습니다.
    // 예를 들어, 시간, 일, 주, 월, 년에 대한 테스트 케이스 등

    // getRelativeTime 메서드는 여기에 정의합니다.
    private String getRelativeTime(LocalDateTime time) {

        Duration duration = Duration.between(time, LocalDateTime.now());
        long seconds = duration.getSeconds();
        TimeUnit unit = TimeUnit.getAppropriateUnit(seconds);
        long value = (long) Math.floor((double) seconds / unit.getSeconds());
        String unitKey = unit.getKey();

        if (value != 1) {
            unitKey += "s";
        }

        return value + unitKey + " ago";
    }
}

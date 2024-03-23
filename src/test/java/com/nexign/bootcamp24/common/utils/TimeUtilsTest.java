package com.nexign.bootcamp24.common.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilsTest {

    @Test
    void toLocalDateTime() {
        LocalDateTime localDateTime = TimeUtils.toLocalDateTime(1719481016);

        assertAll(() -> {
            assertEquals(2024, localDateTime.getYear());
            assertEquals(6, localDateTime.getMonthValue());
            assertEquals(27, localDateTime.getDayOfMonth());
        });
    }

    @Test
    void getDurationString() {
        long duration = 391;

        assertEquals("00:06:31", TimeUtils.getDurationString(duration));
    }
}
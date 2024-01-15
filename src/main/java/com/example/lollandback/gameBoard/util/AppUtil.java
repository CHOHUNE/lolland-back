package com.example.lollandback.gameBoard.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class AppUtil {
    public static String getAgo(LocalDateTime a) {
        LocalDateTime b = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        if (a.isBefore(b.minusDays(1))) {
            Period between = Period.between(a.toLocalDate(), b.toLocalDate());
            long years = between.get(ChronoUnit.YEARS);
            long months = between.get(ChronoUnit.MONTHS);
            long days = between.get(ChronoUnit.DAYS);

            if (years > 0) {
                return years + "년 전";
            }

            if (months > 0) {
                return months + "달 전";
            }

            return days + "일 전";

        } else if (a.isBefore(b.minusHours(1))) {
            Duration between = Duration.between(a, b);
            return (between.getSeconds() / 60 / 60) + "시간 전";
        } else if (a.isBefore(b.minusMinutes(1))) {
            Duration between = Duration.between(a, b);
            return (between.getSeconds() / 60) + "분 전";
        } else {
            Duration between = Duration.between(a, b);
            return between.getSeconds() + "초 전";
        }
    }
}

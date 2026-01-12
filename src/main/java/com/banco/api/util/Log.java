package com.banco.api.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
	private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static String buildPrefix(String level, Class<?> clazz) {
        String time = LocalDateTime.now().format(FORMATTER);
        String name = clazz != null ? clazz.getSimpleName() : "UnknownClass";
        return "[" + time + "] [" + level + "] [" + name + "] ";
    }

    private static String formatMessage(String message, Object... args) {
        if (message == null || args == null || args.length == 0) {
            return message;
        }
        String formatted = message;
        for (Object arg : args) {
            formatted = formatted.replaceFirst("\\{}", arg == null ? "null" : arg.toString());
        }
        return formatted;
    }

    public static void info(Class<?> clazz, String message, Object... args) {
        System.out.println(buildPrefix("INFO", clazz) + formatMessage(message, args));
    }

    public static void warn(Class<?> clazz, String message, Object... args) {
        System.out.println(buildPrefix("WARN", clazz) + formatMessage(message, args));
    }

    public static void error(Class<?> clazz, String message, Object... args) {
        System.err.println(buildPrefix("ERROR", clazz) + formatMessage(message, args));
    }

    public static void error(Class<?> clazz, String message, Throwable t, Object... args) {
        System.err.println(buildPrefix("ERROR", clazz) + formatMessage(message, args));
        if (t != null) {
            t.printStackTrace(System.err);
        }
    }
}

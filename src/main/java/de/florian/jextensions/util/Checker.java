package de.florian.jextensions.util;

import java.util.Collection;

import static java.util.Objects.requireNonNull;

public class Checker {
    public static <T> boolean requireNonNullAndNotEmpty(Collection<T> collection, String name) throws IllegalArgumentException {
        requireNonNull(collection, name + " must not be null");
        requireCondition(!collection.isEmpty(), name + " must not be empty.");
        return true;
    }

    public static void requireCondition(boolean expression, String messageFormat, Object... parameters) throws IllegalArgumentException {
        if (!expression) throw new IllegalArgumentException(String.format(messageFormat, parameters));
    }


    public static <T extends Number> T requireGreaterThanZero(T number) {
        requireCondition(!(Double.doubleToRawLongBits(number.doubleValue()) < 0), "number has to be greater than zero");
        return number;
    }
}

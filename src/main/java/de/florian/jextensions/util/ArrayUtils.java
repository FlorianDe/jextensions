package de.florian.jextensions.util;

import java.util.Collection;
import java.util.function.Predicate;

public class ArrayUtils {
    @SuppressWarnings("unchecked")
    public static <T> T[] copyFromCollection(Collection<T> values) {
        return values.toArray((T[]) new Object[values.size()]);
    }

    public static double[] normalize(double[] values) throws IllegalArgumentException {
        return normalize(values, 1.0);
    }

    public static double[] normalize(double[] values, double normalizedSum) throws IllegalArgumentException {
        Predicate<Double> invalidNumber = e -> Double.isInfinite(e) || Double.isNaN(e);

        if (invalidNumber.test(normalizedSum)) {
            throw new IllegalArgumentException("Normalize value is infinite or not a number.");
        }
        double sum = 0.0;
        final int len = values.length;
        double[] normalized = new double[len];
        for (int i = 0; i < len; i++) {
            if (invalidNumber.test(values[i])) {
                throw new IllegalArgumentException(String.format("Value for element at index %s was infinite or not a number.", i));
            }
            sum += values[i];
        }
        if (sum == 0) {
            return values;
        }
        for (int i = 0; i < len; i++) {
            normalized[i] = values[i] * normalizedSum / sum;
        }
        return normalized;
    }
}

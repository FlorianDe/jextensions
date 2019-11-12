package de.florian.jextensions.math.distribution.random.picker;

import java.util.Arrays;
import java.util.Random;

public class BinarySearchRandomSelector extends LinearSearchRandomSelector {
    public BinarySearchRandomSelector(final double[] probabilities) {
        super(probabilities);
    }

    @Override
    public int pick(Random random) {
        double nextDouble = random.nextDouble();
        int index = Arrays.binarySearch(cumulativeProbabilities, nextDouble);
        if (index < 0) {
            index = -index - 1;
        }
        if (index < normalizedProbabilities.length && nextDouble < cumulativeProbabilities[index]) {
            return index;
        }
        return normalizedProbabilities.length - 1;
    }
}

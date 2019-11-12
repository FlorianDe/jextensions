package de.florian.jextensions.math.distribution.random.picker;

import de.florian.jextensions.util.ArrayUtils;

import java.util.Random;

public class LinearSearchRandomSelector implements IRandomSelector {
    final double[] normalizedProbabilities;
    final double[] cumulativeProbabilities;

    public LinearSearchRandomSelector(final double[] probabilities) {
        normalizedProbabilities = ArrayUtils.normalize(probabilities);
        cumulativeProbabilities = new double[probabilities.length];

        double sum = 0;
        for (int i = 0; i < normalizedProbabilities.length; i++) {
            cumulativeProbabilities[i] = (sum += normalizedProbabilities[i]);
        }
    }

    @Override
    public int pick(Random random) {
        double rand = random.nextDouble();
        for (int i = 0; i < cumulativeProbabilities.length; i++) {
            if (rand < cumulativeProbabilities[i]) return i;
            rand -= cumulativeProbabilities[i];
        }
        return cumulativeProbabilities.length - 1;
    }
}

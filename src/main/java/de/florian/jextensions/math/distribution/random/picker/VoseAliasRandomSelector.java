package de.florian.jextensions.math.distribution.random.picker;

/*
 *
 * An implementation of the alias method implemented using Vose's Alias algorithm.
 * The alias method allows for efficient sampling of random values from a
 * discrete probability distribution.
 *
 * Preparation complexity O(n)
 * Random selection complexity O(1)
 *
 */

import java.util.Random;

public final class VoseAliasRandomSelector implements IRandomSelector {
    private final double[] weights;
    private final int[] alias;

    public VoseAliasRandomSelector(final double[] weights) {
        final int size = weights.length;
        int smallCounter = 0, largeCounter = 0;
        final int[] small = new int[size], large = new int[size];
        final double average = 1.0 / size;

        for (int i = 0; i < size; i++) {
            if (weights[i] < average) {
                small[smallCounter++] = i;
            } else {
                large[largeCounter++] = i;
            }
        }

        final double[] pr = new double[size];
        final int[] al = new int[size];
        this.weights = pr;
        this.alias = al;

        while (largeCounter != 0 && smallCounter != 0) {
            final int less = small[--smallCounter];
            final int more = large[--largeCounter];
            pr[less] = weights[less] * size;
            al[less] = more;
            weights[more] += weights[less] - average;
            if (weights[more] < average) {
                small[smallCounter++] = more;
            } else {
                large[largeCounter++] = more;
            }
        }
        while (smallCounter != 0) {
            pr[small[--smallCounter]] = 1.0;
        }
        while (largeCounter != 0) {
            pr[large[--largeCounter]] = 1.0;
        }
    }

    @Override
    public int pick(final Random random) {
        final int column = random.nextInt(weights.length);
        return random.nextDouble() < weights[column] ? column : alias[column];
    }
}
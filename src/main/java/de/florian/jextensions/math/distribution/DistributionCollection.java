package de.florian.jextensions.math.distribution;

import de.florian.jextensions.math.distribution.random.picker.IRandomSelector;

import java.util.Random;


public class DistributionCollection<T> {
    private final T[] elements;
    private final IRandomSelector selector;

    DistributionCollection(T[] elements, final IRandomSelector selector) {
        this.elements = elements;
        this.selector = selector;
    }

    public T next(Random random) {
        return elements[selector.pick(random)];
    }
}

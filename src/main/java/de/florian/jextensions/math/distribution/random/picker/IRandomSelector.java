package de.florian.jextensions.math.distribution.random.picker;

import java.util.Random;

@FunctionalInterface
public interface IRandomSelector {
    int pick(Random random);
}

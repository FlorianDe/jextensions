package de.florian.jextensions.math.distribution;

import de.florian.jextensions.math.distribution.random.picker.IRandomSelector;
import de.florian.jextensions.math.distribution.random.picker.VoseAliasRandomSelector;
import de.florian.jextensions.util.ArrayUtils;
import de.florian.jextensions.util.Checker;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

import static de.florian.jextensions.util.Checker.requireNonNullAndNotEmpty;
import static java.util.Objects.requireNonNull;

public class DistributionCollectionFactory {

    public static <T> DistributionCollection<T> createUniform(Collection<T> elements) throws IllegalArgumentException {
        requireNonNullAndNotEmpty(elements, "elements");

        return new DistributionCollection<>(
                ArrayUtils.copyFromCollection(elements),
                random -> random.nextInt(elements.size())
        );
    }

    public static <T> DistributionCollection<T> createWeighted(Collection<T> elements, ToDoubleFunction<? super T> weightFunc) {
        return createWeighted(elements, weightFunc, VoseAliasRandomSelector::new);
    }

    public static <T> DistributionCollection<T> createWeighted(Collection<T> elements, ToDoubleFunction<? super T> weightFunc, Function<double[], IRandomSelector> algorithmChooser) throws IllegalArgumentException {
        requireNonNullAndNotEmpty(elements, "elements");
        requireNonNull(weightFunc, "weightFunc must not be null");
        requireNonNull(algorithmChooser, "algorithmChooser must not be null");

        final T[] elementArray = ArrayUtils.copyFromCollection(elements);
        final double[] normalizedWeights = ArrayUtils.normalize(
                Arrays.stream(elementArray)
                        .mapToDouble(weightFunc)
                        .peek(Checker::requireGreaterThanZero)
                        .toArray()
        );

        return new DistributionCollection<>(
                elementArray,
                algorithmChooser.apply(normalizedWeights)
        );
    }
}

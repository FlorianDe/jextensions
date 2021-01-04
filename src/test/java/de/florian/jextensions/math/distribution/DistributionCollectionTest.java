package de.florian.jextensions.math.distribution;

import de.florian.jextensions.math.distribution.random.picker.BinarySearchRandomSelector;
import de.florian.jextensions.math.distribution.random.picker.LinearSearchRandomSelector;
import de.florian.jextensions.math.distribution.random.picker.VoseAliasRandomSelector;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

public class DistributionCollectionTest {

    private static final int SPEED_TEST_ELEMENT_SIZE = 1_000_000;
    private static final long SPEED_TEST_ROUNDS = 1_000_000L;

    private List<String> collect;
    private Map<String, Double> map;

    @Before
    public void init() {
        collect = IntStream.range(1, SPEED_TEST_ELEMENT_SIZE).mapToObj(String::valueOf).collect(Collectors.toList());
        map = IntStream.range(1, SPEED_TEST_ELEMENT_SIZE).mapToObj(String::valueOf).collect(Collectors.toMap(Function.identity(), e -> 1.0));
    }

    @Test
    public void speed_test_uniform_distribution() {
        helperMeasureRounds(() -> DistributionCollectionFactory.createUniform(collect), "speed_test_uniform_distribution");
    }

    @Test
    public void speed_test_weighted_distribution_vose_alias() {
        helperMeasureRounds(() -> DistributionCollectionFactory.createWeighted(map.keySet(), map::get), "speed_test_weighted_distribution_vose_alias");
    }

    @Test
    public void speed_test_weighted_distribution_binary_search() {
        helperMeasureRounds(() -> DistributionCollectionFactory.createWeighted(map.keySet(), map::get, BinarySearchRandomSelector::new), "speed_test_weighted_distribution_binary_search");
    }

    @Test
    public void speed_test_weighted_distribution_linear_search() {
        helperMeasureRounds(() -> DistributionCollectionFactory.createWeighted(map.keySet(), map::get, LinearSearchRandomSelector::new), "speed_test_weighted_distribution_linear_search");
    }

    @Test
    public void epsilon_count_test_for_every_element() {
        final double epsilon = 0.05;
        Map<Integer, Double> elements = Map.of(
                1, 0.4,
                2, 0.3,
                3, 0.2,
                4, 0.09,
                5, 0.009,
                6, 0.001
        );

        DistributionCollection<Integer> distributionCollection = DistributionCollectionFactory.createWeighted(
                elements.keySet(),
                elements::get,
                VoseAliasRandomSelector::new
        );

        Map<Integer, Integer> count = new HashMap<>();
        Random random = new Random();
        for (long i = 0; i < SPEED_TEST_ROUNDS; i++) {
            count.merge(distributionCollection.next(random), 1, Integer::sum);
        }

        elements.forEach((k, v) -> {
            double elementPercentage = Math.abs(count.get(k) / (double) SPEED_TEST_ROUNDS);
            double lowerBound = v / (1 + epsilon);
            double upperBound = v * (1 + epsilon);
            boolean withinError = lowerBound <= elementPercentage && elementPercentage <= upperBound;
            System.out.printf("Element: %s, percentage: %s\t->\tin range: %s for viable range: [%s, %s]\n", k, elementPercentage, withinError, lowerBound, upperBound);
            assertTrue(withinError);
        });
    }

    private void helperMeasureRounds(Supplier<DistributionCollection<?>> supplier, String method) {
        long start = System.currentTimeMillis();
        Random random = new Random();
        DistributionCollection<?> distributionCollection = supplier.get();
        for (long i = 0; i < SPEED_TEST_ROUNDS; i++) {
            distributionCollection.next(random);
        }
        System.out.printf("Ran %s %s rounds with %s elements in %s ms\n", SPEED_TEST_ROUNDS, method, SPEED_TEST_ELEMENT_SIZE, (System.currentTimeMillis() - start));
    }
}
package de.florian.jextensions.math.distribution;

import de.florian.jextensions.math.distribution.random.picker.BinarySearchRandomSelector;
import de.florian.jextensions.math.distribution.random.picker.LinearSearchRandomSelector;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        helperMeasureRounds(() -> DistributionCollectionFactory.createUniform(collect),  "speed_test_uniform_distribution");
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

    private void helperMeasureRounds(Supplier<DistributionCollection> supplier, String method) {
        long start = System.currentTimeMillis();
        Random random = new Random();
        DistributionCollection distributionCollection = supplier.get();
        for (long i = 0; i < SPEED_TEST_ROUNDS; i++) {
            distributionCollection.next(random);
        }
        System.out.printf("Ran %s %s rounds with %s elements in %s ms\n", SPEED_TEST_ROUNDS, method, SPEED_TEST_ELEMENT_SIZE, (System.currentTimeMillis() - start));
    }
}
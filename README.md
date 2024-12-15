# Custom Java Utilities Library

[![GitHub License](https://img.shields.io/github/license/floriande/jextensions)
](LICENSE)

A custom Java library that simplifies your development process by providing frequently used functions, algorithms, and utilities. Designed to reduce boilerplate code, this library is a handy toolset for any Java project. It includes utilities for file locking, random selection, assertion helpers, and array operations. 

Additionally, precompiled GitHub packages are available for seamless integration with Maven or Gradle.

---

## Features

### 1. **io.file**
   - **Single Instance Lock Checker**: Prevent multiple instances of an application or process by implementing a file-based lock mechanism.

### 2. **math.distribution**
   - **Random Picker Library**: Various strategies for selecting random items from a dataset:
     - **VoseAliasRandomSelector**: Efficient implementation of the Alias Method for weighted random selection.
     - **LinearSearchSelector**: Straightforward random selection using a linear search approach.
     - **BinarySearchRandomSelector**: Optimized for weighted random selection with binary search.

### 3. **util**
   - **Assertions**: Simplify common assertions to make your code more readable and less error-prone.
   - **Array Utilities**: A collection of helper methods to work with arrays efficiently.

---

## Getting Started

### Prerequisites
- Java 8 or higher
- Maven or Gradle for dependency management

### Installation

#### Using Maven
Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.github.floriande</groupId>
    <artifactId>jextensions</artifactId>
    <version>0.1.0</version>
</dependency>
```

#### Using Gradle
Add the following dependency to your `build.gradle`:

```gradle
implementation 'com.github.floriande:jextensions:0.1.0'
```

---

## Usage

### Single Instance Lock Checker
```java
if (new SingleInstanceLock().isSingleInstanceRunning()) {
    log.error("An instance of this application is already running!");
    System.exit(1);
} 
//TODO Try to show/open the running application!
```

### Random Pickers
```java
// VoseAliasRandomSelector example
Map<Integer, Double> elements = Map.of(
        1, 0.15,
        2, 0.6,
        3, 0.25,
);

DistributionCollection<Integer> distributionCollection = DistributionCollectionFactory.createWeighted(
        elements.keySet(),
        elements::get,
        VoseAliasRandomSelector::new
);

Random random = new Random(); // possible to add a seed
distributionCollection.next(random);
```

For more infos and benchmarks check [DistributionCollectionTest.java](src/test/java/de/florian/jextensions/math/distribution/DistributionCollectionTest.java).

### Utility Methods
#### Array Utils
The `ArrayUtils` class offers helpful methods for working with collections and arrays, providing functionality to simplify common array operations. Here's a quick overview:

1. **`copyFromCollection(Collection<T> values)`**
   - Creates a new array containing all elements from a given collection.
   - The resulting array is typed based on the collection elements.
   - Example:
     ```java
     String[] array = ArrayUtils.copyFromCollection(new String[] {"Hello", "World"});
     ```

2. **`normalize(double[] values)`**
   - Normalizes the values in an array so their sum equals `1.0`.
   - Handles cases where the array contains zeros or very small values without throwing errors.
   - Example:
     ```java
     double[] normalized = ArrayUtils.normalize(new double[] {1.0, 2.0, 3.0});
     ```

3. **`normalize(double[] values, double normalizedSum)`**
   - Similar to the above method but allows you to specify a desired sum for the normalized values.
   - Throws `IllegalArgumentException` if any value in the array or the `normalizedSum` is `NaN` or `Infinity`.
   - Example:
     ```java
     double[] normalized = ArrayUtils.normalize(new double[] {1.0, 2.0, 3.0}, 100.0);
     ```

#### Checker
The `Checker` class provides utility methods to simplify common validation (assertion) checks in your code, reducing boilerplate and improving readability. Below is a brief overview of the available methods:

1. **`requireNonNullAndNotEmpty(Collection<T> collection, String name)`**
   - Ensures that a collection is not `null` and is not empty.
   - Throws `IllegalArgumentException` if the validation fails.
   - Example:
     ```java
     Checker.requireNonNullAndNotEmpty(myList, "myList");
     ```

2. **`requireCondition(boolean expression, String messageFormat, Object... parameters)`**
   - Validates that a condition is true.
   - Throws `IllegalArgumentException` with a formatted message if the condition is false.
   - Example:
     ```java
     Checker.requireCondition(x > 0, "Value must be greater than zero: %d", x);
     ```

3. **`requireGreaterThanZero(T number)`**
   - Ensures a numeric value is greater than zero.
   - Returns the number if valid, or throws `IllegalArgumentException` otherwise.
   - Example:
     ```java
     int positiveNumber = Checker.requireGreaterThanZero(42);
     ```

---

## Contributing
Contributions are welcome! Please fork this repository and submit a pull request with your improvements or new features.

### Steps to Contribute
1. Fork the repository.
2. Create a new branch: `git checkout -b feature/branch-name`.
3. Make your changes and commit them: `git commit -m 'Add some feature'`.
4. Push to the branch: `git push origin feature/branch-name`.
5. Submit a pull request.

---

## License
This project is licensed under the **GNU General Public License v3.0 (GPL-3.0)** License - see the [LICENSE](LICENSE) file for details.

---

## Contact
If you have any questions or feedback, feel free to open an issue or contact the repository owner.

---

## Acknowledgments
Inspired by common developer needs to simplify Java project workflows.
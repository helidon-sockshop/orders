package io.helidon.examples.sockshop.orders;

/**
 * Tests need to interact with the entity backing OrderRepository
 * to reset to initial state before every test. In order to reduce
 * programming errors, the methods needed for the tests should reside
 * in test project. Since various repositories have
 * implementation-specific ways of clearing the repository, they need
 * to extend actual implementations by implementing TestOrderRepository
 * interface. The test rig then can cast the repository it obtains
 * through CDI to a TestOrderRepository, and can clear the repository
 * between the tests.
 *
 * <p>Note that for the CDI to work it is necessary to mark the test
 * repositories with @Alternative and @Priority exceeding that of the
 * implementation.
 *
 * <p>Note that for the tests to test the actual implementation it is
 * necessary that the test implementations only extend the class, but
 * do not override any methods.
 */
public interface TestOrderRepository {
    void clear();
}

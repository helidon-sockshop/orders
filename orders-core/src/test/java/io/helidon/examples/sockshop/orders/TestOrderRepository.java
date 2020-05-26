/*
 *  Copyright (c) 2020 Oracle and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.helidon.examples.sockshop.orders;

/**
 * Tests may need additional methods in repository implementations
 * in order to pre-load test data, clear the repository between the
 * test, etc.
 *
 * <p>The methods needed only for the tests should reside in the test
 * project, to avoid polluting production repository implementation
 * with methods it a) doesn't need, and b) may be downright dangerous.
 *
 * <p>Note that for the CDI to work it is necessary to mark the test
 * repositories with @Alternative and @Priority exceeding that of the
 * implementation.
 *
 * <p>Note that for the tests to test the actual implementation it is
 * necessary that the test implementations only extend the repository
 * class and add methods, but do not override any methods from the
 * repository implementation they are testing.
 *
 * @author Oleksandr Otenko  2020.03.06
 */
public interface TestOrderRepository extends OrderRepository {
    /**
     * Helper to clear this repository for testing.
     */
    void clear();
}

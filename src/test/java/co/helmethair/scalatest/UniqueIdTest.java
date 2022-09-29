package co.helmethair.scalatest;

import co.helmethair.scalatest.helper.TestHelpers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.testkit.engine.EngineTestKit;

public class UniqueIdTest implements TestHelpers {

    @ParameterizedTest
    @CsvSource({
        "[engine:scalatest], 0, 0",
        "[engine:scalatest]/[suite:tests.NestedTest], 2, 0",
        // For now, it does not support really executing the selected test, it will execute the whole suite
        "[engine:scalatest]/[suite:tests.NestedTest]/[test:nested test1], 2, 0",
        "[engine:scalatest]/[foo:tests.NestedTest], 0, 0",
        "[engine:scalatest]/[suite:tests.UnknownFoo], 0, 1",
    })
    void handleUniqueId(String id, int successEvents, int failingEvents) {
        EngineTestKit
            .engine("scalatest")
            .selectors(DiscoverySelectors.selectUniqueId(id))
            .execute()
            .testEvents()
            .assertStatistics(stats -> stats.succeeded(successEvents).failed(failingEvents));
    }
}

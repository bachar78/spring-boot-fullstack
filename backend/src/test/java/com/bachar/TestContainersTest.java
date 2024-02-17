package com.bachar;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

//@SpringBootTest don't use this annotation for unit test, because it spin up the whole context and the test will be slow, only for integration test
@Testcontainers
public class TestContainersTest extends AbstractTestcontainers {


    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}

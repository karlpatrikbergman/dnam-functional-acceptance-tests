package com.infinera.metro.test.acceptance.layer1;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testing Layer1Service...")
@Slf4j
class Layer1ServiceTest {

    private final Layer1Service layer1Service = new Layer1Service();

    @Test
    @DisplayName("addTrailingFoo(\"Bar\") should return BarFoo")
    void returnFooShouldReturnFoo() {
        assertEquals("BarFoo", layer1Service.addTrailingFoo("Bar"));

    }
}

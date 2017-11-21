package com.infinera.metro.test.acceptance.layer2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testing Layer2Service...")
@Slf4j
class Layer2ServiceTest {

    private final Layer2Service layer2Service = new Layer2Service();

    @Test
    @DisplayName("addLeadingBar(\"bapappa\") should return Barbapappa")
    void addLedingBarToBapappahouldReturnBarbapappa() {
        assertEquals("Barbapappa", layer2Service.addLeadingBar("bapappa"));
    }
}

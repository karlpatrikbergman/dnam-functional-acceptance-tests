package com.infinera.metro.test.acceptance.platform;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


@Slf4j
class SoftwareUpgradeTest {

    private final SoftwareUpgradeService softwareUpgradeService = new SoftwareUpgradeService();

    @Test
    void softwareUpgradeShouldReturnTrue() {
        assertTrue(softwareUpgradeService.upgradeNode());
    }
}

package org.moparforia.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

/** Tests that resources can be loaded */
class ResourceLoadTest {

    /** Tests that Launcher icon can be loaded */
    @Test
    void testLoadIcon() {
        Launcher launcher = new Launcher();
        assertDoesNotThrow(launcher::loadIcon);
    }
}

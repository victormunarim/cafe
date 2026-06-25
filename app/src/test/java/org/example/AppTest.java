package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test void appInitialization() {
        App app = new App();
        assertNotNull(app);
    }
}

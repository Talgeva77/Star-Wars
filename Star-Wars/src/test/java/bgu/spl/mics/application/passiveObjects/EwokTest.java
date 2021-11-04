package bgu.spl.mics.application.passiveObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {
    Ewok e;
    @BeforeEach
    void setUp() {
        e = new Ewok(1);
    }

    @AfterEach
    void tearDown() { // ** maybe we will delete this empty method
    }

    @Test
    void acquire() {
        assertTrue(e.available);
        e.acquire();
        assertFalse(e.available);
    }

    @Test
    void release() {
        e.acquire();
        assertFalse(e.available);
        e.release();
        assertTrue(e.available);
    }
}
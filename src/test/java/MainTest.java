import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void twice() {
        int x = Main.twice(4);
        assertEquals(8, x);
    }
}
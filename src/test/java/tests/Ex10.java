package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex10 {
    @Test
    public void testStringLength() {
        String string = "Этот текст более 15 символов";

        assertTrue(string.length() > 14);
    }
}
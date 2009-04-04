
package bozack.test;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;
import bozack.Note;

public class TestNote {

    private static final bozack.Note n1 = new bozack.Note(55);

    @Test
    public void note() {
        assertEquals(n1.getNote(), 55);
    }
    @Test
    public void octav() {
        assertEquals(n1.getOctav(), 4);
    }
    @Test
    public void pitch() {
        assertEquals(n1.getPitch(), 7);
    }
    @Test
    public void to_string() {
        assertEquals(n1.toString(), "note=55 octav=4 pitch=7");
    }

}


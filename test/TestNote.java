
package bozack;

import static org.testng.Assert.*;
import org.testng.annotations.Test;
import bozack.*;

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
    @Test
    public void constructWithPitchName() {
        bozack.Note c5 = new bozack.Note(PitchName.C, 5);
        assertNotNull(c5);
        assertEquals(c5.getPitch(), 0);
        assertEquals(c5.getNote(), 60);
        assertEquals(c5.getOctav(), 5);

        bozack.Note af3 = new bozack.Note(PitchName.A_FLAT, 3);
        assertNotNull(af3);
        assertEquals(af3.getPitch(), 9);
        assertEquals(af3.getNote(), 45);
        assertEquals(af3.getOctav(), 3);
    }

}


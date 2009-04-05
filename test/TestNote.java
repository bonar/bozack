
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
        assertEquals(n1.toString(), "note=55 octav=4 pitch=7 G");
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
        assertEquals(af3.getPitch(), 8);
        assertEquals(af3.getNote(), 44);
        assertEquals(af3.getOctav(), 3);
    }
    @Test
    public void equals_and_hashCode() {
        bozack.Note a30 = new bozack.Note(30);
        bozack.Note a31 = new bozack.Note(31);
        
        bozack.Note b30 = new bozack.Note(30);
        bozack.Note b31 = new bozack.Note(31);

        assertEquals(a30.equals(a30), true);
        assertEquals(a30.equals(a31), false);
        assertEquals(a31.equals(a30), false);
        assertEquals(a31.equals(a31), true);

        assertEquals(a30.equals(b30), true);
        assertEquals(a31.equals(b31), true);
        assertEquals(a30.equals(b31), false);
        assertEquals(a31.equals(b30), false);

        assertEquals(a30.hashCode(), a30.hashCode());
        assertNotSame(a30.hashCode(), a31.hashCode());
        assertNotSame(a31.hashCode(), a30.hashCode());
        assertEquals(a31.hashCode(), a31.hashCode());

        assertEquals(a30.hashCode(), b30.hashCode());
        assertEquals(a31.hashCode(), b31.hashCode());
        assertNotSame(a30.hashCode(), b31.hashCode());
        assertNotSame(a31.hashCode(), b30.hashCode());
    }

}


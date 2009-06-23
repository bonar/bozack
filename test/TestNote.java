
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
    public void chroma() {
        assertEquals(n1.getChroma(), 7);
    }
    @Test
    public void to_string() {
        assertEquals(n1.toString(), "note=55 octav=4 G");
    }
    @Test
    public void constructWithChromaName() {
        bozack.Note c5 = new bozack.Note(ChromaName.C, 5);
        assertNotNull(c5);
        assertEquals(c5.getChroma(), 0);
        assertEquals(c5.getNote(), 60);
        assertEquals(c5.getOctav(), 5);

        bozack.Note af3 = new bozack.Note(ChromaName.A_FLAT, 3);
        assertNotNull(af3);
        assertEquals(af3.getChroma(), 8);
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
    @Test
    public void staticValues() {
        // no idea to test this..
        System.out.println(bozack.Note.SEMITONE_FREQ_RATIO);
    }
    @Test
    public void frequencyCalc() {
        bozack.Note a4 = new bozack.Note(ChromaName.A, 4);
        bozack.Note a5 = new bozack.Note(ChromaName.A, 5);
        bozack.Note a6 = new bozack.Note(ChromaName.A, 6);

        assertEquals((int)a4.getFreq(), 220);
        assertEquals((int)a5.getFreq(), 440);
        assertEquals((int)a6.getFreq(), 880);
    }
    @Test
    public void dossonanceVariation() {
        bozack.Note base = new bozack.Note(48);
        for (int i = 0; i < 12; i++) {
            bozack.Note target = new bozack.Note(48 + i);
            double des = base.getDessonance(target);
            assertNotNull(des);
            System.out.printf("(plain) %d:%2.5f (%s)\n"
                , target.getNote(), des, target.toString());
        }
    }
    @Test
    public void dossonanceVariationOvertone() {
        bozack.Note base = new bozack.Note(48);
        for (int i = 0; i < 12; i++) {
            bozack.Note target = new bozack.Note(48 + i);
            double des = base.getDessonance(target);
            assertNotNull(des);
            System.out.printf("(ot) %d:%2.5f (%s)\n"
                , target.getNote(), des, target.toString());
        }
    }
}


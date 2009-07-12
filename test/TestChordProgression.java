
package bozack;

import static org.testng.Assert.*;
import org.testng.annotations.Test;
import java.util.Iterator;
import bozack.Chord;
import bozack.ChordRole;
import bozack.ChordProgression;

public class TestChordProgression {

    @Test
    public void variate() {
        ChordProgression prog = new ChordProgression();
        Chord C = new Chord(ChromaName.C, ChordType.MAJOR);
        Chord D = new Chord(ChromaName.D, ChordType.MAJOR);

        assertEquals(C, prog.getChord());
        assertNotSame(D, prog.getChord());

        prog.variate();
        Chord C7  = new Chord(ChromaName.C, ChordType.SEVEN);
        assertEquals(C7, prog.getChord());

        prog.variate();
        Chord Em7 = new Chord(ChromaName.E, ChordType.m7);
        assertEquals(Em7, prog.getChord());

        // rotation
        prog.variate();
        assertEquals(C, prog.getChord());
    }

    @Test
    public void next() {
        ChordProgression prog = new ChordProgression();
        assertEquals(prog.getChord()
            , new Chord(ChromaName.C, ChordType.MAJOR));

        prog.next();
        assertEquals(prog.getChord()
            , new Chord(ChromaName.F, ChordType.MAJOR));
        prog.next();
        assertEquals(prog.getChord()
            , new Chord(ChromaName.G, ChordType.SEVEN));
        // rotation
        prog.next();
        assertEquals(prog.getChord()
            , new Chord(ChromaName.C, ChordType.MAJOR));
    }

}


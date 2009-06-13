
package bozack;

import static org.testng.Assert.*;
import org.testng.annotations.Test;
import bozack.*;

public class TestChord {

    @Test
    public void basic() {
        Chord Em = new bozack.Chord(ChromaName.E, SpanType.ST_m);
        assertEquals(Em.getChromaName(), ChromaName.E);
        assertEquals(Em.getSpanType(), SpanType.ST_m);

        NoteHop hop = Em.getNoteHop();
        int[] hopint = hop.getHop();
        assertEquals(hopint[0], 0);
        assertEquals(hopint[1], 3);
        assertEquals(hopint[2], 7);
    }
}




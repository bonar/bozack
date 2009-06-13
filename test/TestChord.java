
package bozack;

import static org.testng.Assert.*;
import org.testng.annotations.Test;
import java.util.Iterator;
import bozack.*;

public class TestChord {

    @Test
    public void basic() {
        Chord Em = new bozack.Chord(ChromaName.E, ChordType.m);
        assertEquals(Em.getChromaName(), ChromaName.E);
        assertEquals(Em.getChordType(), ChordType.m);

        NoteHop hop = Em.getNoteHop();
        int[] hopint = hop.getHop();
        assertEquals(hopint[0], 0);
        assertEquals(hopint[1], 3);
        assertEquals(hopint[2], 7);
    }
    @Test
    public void chromaSet() {
        Chord CM = new bozack.Chord(ChromaName.C, ChordType.MAJOR);
        ChromaSet set = CM.getChromaSet();

        ChromaNameHash chromaName = bozack.Types.getChromaNameHash();
        Iterator iter = set.iterator();

        Integer first = (Integer)iter.next();
        assertEquals(chromaName.get(ChromaName.C), first);

        Integer second = (Integer)iter.next();
        assertEquals(chromaName.get(ChromaName.E), second);

        Integer third = (Integer)iter.next();
        assertEquals(chromaName.get(ChromaName.G), third);
    }
}




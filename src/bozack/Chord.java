
package bozack;

import java.util.HashMap;
import bozack.ChromaSet;
import bozack.ChordType;

/**
 * NoteHop is used to represent distance from the Note to 
 * another Note in some Chord
 */
final class NoteHop {
    private int[] span = new int[4];

    NoteHop(int a, int b, int c) {
        this.span[0] = a;
        this.span[1] = b;
        this.span[2] = c;
    }

    NoteHop(int a, int b, int c, int d) {
        this.span[0] = a;
        this.span[1] = b;
        this.span[2] = c;
        this.span[3] = d;
    }

    public int[] getHop() {
        return this.span;
    }
}

/**
 * ScanType String to IntegerArray
 * @see bozack.Chord
 */
final class ChordTypeHash extends HashMap<ChordType, NoteHop> {}

public class Chord {

    private static final ChordTypeHash getChordTypeHash() {
        ChordTypeHash m = new ChordTypeHash();
        m.put(ChordType.MAJOR, new NoteHop(0, 4, 7));
        m.put(ChordType.m,     new NoteHop(0, 3, 7));
        m.put(ChordType.SEVEN, new NoteHop(0, 4, 7, 10));
        m.put(ChordType.m7,    new NoteHop(0, 3, 7, 10));
        m.put(ChordType.M7,    new NoteHop(0, 4, 7, 11));
        return m;
    }

    private static final ChordTypeHash chordTypeHash
        = getChordTypeHash();

    private final ChromaName chromaName;
    private final ChordType  chordType;
    private final NoteHop    noteHop;
    private final ChromaSet  chromaSet;

    public Chord(ChromaName chroma, ChordType type) {
        this.chromaName = chroma;
        this.chordType  = type;
        this.noteHop    = chordTypeHash.get(type);

        int[] hops = this.noteHop.getHop();
        ChromaSet chromas = new ChromaSet();
        for (int i = 0; i < hops.length; i++) {
            int c = hops[i];
            c = c >= 12 ? (c - 12) : c;
            chromas.add(new Integer(c));
        }
        this.chromaSet = chromas;
    }

    public ChromaName getChromaName() { return this.chromaName; }
    public ChordType  getChordType()  { return this.chordType; }
    public NoteHop    getNoteHop()    { return this.noteHop; }
    public ChromaSet  getChromaSet()  { return this.chromaSet; }
}


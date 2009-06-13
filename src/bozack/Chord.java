
package bozack;

import bozack.Types;
import bozack.ChromaSet;

public class Chord {

    private static SpanTypeHash spanTypeHash
        = bozack.Types.getSpanTypeHash();
    private static ChromaNameHash chromaNameHash
        = bozack.Types.getChromaNameHash();

    private final ChromaName chromaName;
    private final SpanType   spanType;
    private final NoteHop    noteHop;
    private final ChromaSet  chromaSet;

    public Chord(ChromaName chroma, SpanType span) {
        this.chromaName = chroma;
        this.spanType   = span;
        this.noteHop    = spanTypeHash.get(span);

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
    public SpanType   getSpanType()   { return this.spanType; }
    public NoteHop    getNoteHop()    { return this.noteHop; }
    public ChromaSet  getChromaSet()  { return this.chromaSet; }
}


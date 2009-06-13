
package bozack;

import bozack.Types;

public class Chord {

    private static SpanTypeHash spanTypeHash
        = bozack.Types.getSpanTypeHash();

    private ChromaName chromaName;
    private SpanType spanType;
    private NoteHop noteHop;

    public Chord(ChromaName chroma, SpanType span) {
        this.chromaName = chroma;
        this.spanType   = span;
        this.noteHop    = spanTypeHash.get(span);
    }

    public ChromaName getChromaName() {
        return this.chromaName;
    }

    public SpanType getSpanType() {
        return this.spanType;
    }

    public NoteHop getNoteHop() {
        return this.noteHop;
    }
}


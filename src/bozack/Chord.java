
package bozack;

import bozack.Types;

public class Chord {

    private static SpanTypeHash spanTypeHash
        = bozack.Types.getSpanTypeHash();

    private PitchName pitchName;
    private SpanType spanType;
    private NoteHop noteHop;

    public Chord(PitchName pitch, SpanType span) {
        this.pitchName = pitch;
        this.spanType  = span;
        this.noteHop   = spanTypeHash.get(span);
    }

    public PitchName getPitchName() {
        return this.pitchName;
    }

    public SpanType getSpanType() {
        return this.spanType;
    }

    public NoteHop getNoteHop() {
        return this.noteHop;
    }
}


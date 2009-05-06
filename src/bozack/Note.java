
package bozack;

import java.lang.Math;
import java.math.BigDecimal;
import bozack.Types;
import bozack.NoteSet;
import bozack.NoteList;

public final class Note {

    public static final int PITCH_SCALE = 12;
    // calculate note frequency with these values
    public static final BigDecimal SEMITONE_FREQ_RATIO = new BigDecimal(
        Math.pow(10, ((Math.log(2) / Math.log(10)) / PITCH_SCALE)));
    private static int        FREQCALC_BASE_NOTE = 12;
    private static BigDecimal FREQCALC_BASE_FREQ 
        = new BigDecimal("16.351597831287418");

    private static final PitchNameHash pitchName
        = Types.getPitchNameHash();
    private static final PitchNumHash pitchNum
        = Types.getPitchNumHash();

    private static final int MAX_NOTE = 100;

    private final int note;
    private final int octav;
    private final int pitch;
    private final BigDecimal freq;

    public static int getMaxNote() {
        return MAX_NOTE;
    }

    public Note(int note) {
        if (note < 0 || note > MAX_NOTE) {
            throw new IllegalArgumentException(
                "too large note [" + note + "]");
        }
        this.note  = note;
        this.octav = (note / PITCH_SCALE);
        this.pitch = note - (this.octav * PITCH_SCALE);
        if (!pitchNum.containsKey(pitch)) {
            throw new IllegalArgumentException(
                "invalid pitch [" + pitch + "]");
        }

        { // calculate frequency
            int notediff = note - FREQCALC_BASE_NOTE;
            this.freq = SEMITONE_FREQ_RATIO.pow(
                notediff).multiply(FREQCALC_BASE_FREQ);
        }
    }

    public Note(PitchName pn, int octav) {
        this((octav * PITCH_SCALE) + 
            (Integer)pitchName.get(pn).intValue()
        );
    }

    public int getNote()  { return this.note; }
    public int getOctav() { return this.octav; }
    public int getPitch() { return this.pitch; }
    public BigDecimal getFreq() { return this.freq; }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("note="  + this.note  + " ");
        sb.append("octav=" + this.octav + " ");
        sb.append("pitch=" + this.pitch + " ");
        sb.append(pitchNum.get(this.pitch));
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Note) {
            Note n = (Note)obj;
            if (this.note == n.getNote()) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    public int hashCode() {
        return this.note;
    }
}


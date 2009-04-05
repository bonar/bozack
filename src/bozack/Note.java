
package bozack;

import java.util.HashMap;
import bozack.Types;

public final class Note {
    private static final int MAX_NOTE    = 100;
    private static final int PITCH_SCALE = 12;

    private static final PitchNameHash pitchName
        = Types.getPitchNameHash();
    private static final PitchNumHash pitchNum
        = Types.getPitchNumHash();

    private int note;
    private int octav;
    private int pitch;

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
    }

    public Note(PitchName pn, int octav) {
        this((octav * PITCH_SCALE) + 
            (Integer)pitchName.get(pn).intValue()
        );
    }

    public int getNote() {
        return this.note;
    }

    public int getOctav() {
        return this.octav;
    }

    public int getPitch() {
        return this.pitch;
    }

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


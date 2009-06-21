
package bozack;

import java.lang.Math;
import bozack.Types;
import bozack.NoteSet;
import bozack.NoteList;

public final class Note {

    public static final int PITCH_SCALE = 12;
    // calculate note frequency with these values
    public static final double SEMITONE_FREQ_RATIO = 
        Math.pow(10, ((Math.log(2) / Math.log(10)) / PITCH_SCALE));
    private static final int    FREQCALC_BASE_NOTE = 12;
    private static final double FREQCALC_BASE_FREQ = 16.351597831287418d;

    // Magic values of dissonance theory (Plomp & Levelt 1965)
    private static final double PLTHEORY_ALPHA_1 = 0.70d;
    private static final double PLTHEORY_ALPHA_2 = 1.40d;
    private static final double PLTHEORY_ALPHA_3 = 4.00d;
    private static final double PLTHEORY_BETA    = 1.25d;

    private static final double DEFAULT_OVERTONE_VELOCITY_RATIO = 0.88f;

    private static final ChromaNameHash chromaName
        = Types.getChromaNameHash();
    private static final ChromaNumHash chromaNum
        = Types.getChromaNumHash();

    private static final int MAX_NOTE = 100;

    private final int note;
    private final int octav;
    private final int chroma;
    private final double freq;

    public static int getMaxNote() {
        return MAX_NOTE;
    }

    public Note(int note) {
        if (note < 0) {
            throw new IllegalArgumentException(
                "too small note [" + note + "]");
        }
        this.note  = note;
        this.octav = (note / PITCH_SCALE);
        this.chroma = note - (this.octav * PITCH_SCALE);
        if (!chromaNum.containsKey(chroma)) {
            throw new IllegalArgumentException(
                "invalid chroma [" + chroma + "]");
        }

        { // calculate frequency
            int notediff = note - FREQCALC_BASE_NOTE;
            this.freq = FREQCALC_BASE_FREQ
                * Math.pow(SEMITONE_FREQ_RATIO, notediff);
        }
    }

    public Note(ChromaName pn, int octav) {
        this((octav * PITCH_SCALE) + 
            (Integer)chromaName.get(pn).intValue()
        );
    }

    public double getDessonance(bozack.Note target, int overtone) {
        return this.getDessonance(target, overtone
            , DEFAULT_OVERTONE_VELOCITY_RATIO);
    }
    public double getDessonance(bozack.Note target
        , int overtone, double overtone_velocity_ratio) {
        double sum = 0.0d;
        for (int r = 0; r < overtone; r++) {
            bozack.Note toneA = this.getOvertone(r);
            for (int q = 0; q < overtone; q++) {
                double velocity = 
                      Math.pow(overtone_velocity_ratio, q)
                    * Math.pow(overtone_velocity_ratio, r);
                bozack.Note toneB = target.getOvertone(q);
                double des = toneA.getDessonance(toneB);
                sum += velocity * des;
            }
        }
        return sum;
    }
    public double getDessonance(bozack.Note target) {
        if (this.getNote() == target.getNote()) {
            return 0.0d;
        }
        else if (this.getNote() > target.getNote()) {
            return getDessonance(target, this);
        }
        else {
            return getDessonance(this, target);
        }
    }
    public static double getDessonance(
        bozack.Note noteA, bozack.Note noteB) {
        double noteRatio = Math.abs(39.863d 
            * Math.log10(noteB.getFreq() / noteA.getFreq()));
        double exp1 = Math.exp(-1.0d * PLTHEORY_ALPHA_1 
            * Math.pow(noteRatio, PLTHEORY_BETA));
        double exp2 = Math.exp(-1.0d * PLTHEORY_ALPHA_2 
            * Math.pow(noteRatio, PLTHEORY_BETA));
        double dissonance = PLTHEORY_ALPHA_3 * (exp1 - exp2);
        return dissonance;
    }

    public Note getOvertone(int index) {
        int new_note = this.getNote() + (index * PITCH_SCALE);
        if (new_note < PITCH_SCALE) {
            return null;
        }
        return new Note(new_note);
    }

    public int getNote()  { return this.note; }
    public int getOctav() { return this.octav; }
    public int getChroma() { return this.chroma; }
    public double getFreq() { return this.freq; }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("note="  + this.note  + " ");
        sb.append("octav=" + this.octav + " ");
        sb.append("chroma=" + this.chroma + " ");
        sb.append(chromaNum.get(this.chroma));
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


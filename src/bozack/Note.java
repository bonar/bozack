
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
    private static final int    DEFAULT_OVERTONES = 5;

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

    public double getDessonance(Note target) {
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

    public static double getDessonance (Note smaller, Note bigger) {
        return getDessonance(
            smaller, DEFAULT_OVERTONE_VELOCITY_RATIO,
            bigger, DEFAULT_OVERTONE_VELOCITY_RATIO,
            DEFAULT_OVERTONES);
    }

    public static double getDessonance (
        Note smaller, double smallerVelocity,
        Note bigger,  double biggerVelocity,
        int overtones) {

        double base_freq   = smaller.getFreq();
        double target_freq = bigger.getFreq();

        double sum = 0.0d;
        for (int p = 0; p < overtones; p++) {
            double overtoneP = base_freq * (p + 1);
            for (int q = 0; q < overtones; q++) {
                double overtoneQ = target_freq * (q + 1);
                double amp = Math.pow(smallerVelocity, p)
                    * Math.pow(biggerVelocity, q);

                double x12 = Math.abs(39.863d * Math.log10(
                    overtoneQ / overtoneP));
                double d12 = amp * PLTHEORY_ALPHA_3 * (
                    Math.exp(-1.0d * PLTHEORY_ALPHA_1 
                    * Math.pow(x12, PLTHEORY_BETA)) -
                    Math.exp(-1.0d * PLTHEORY_ALPHA_2 
                    * Math.pow(x12, PLTHEORY_BETA))
                );
                sum += d12;
            }
        }
        return (sum / 2);
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



package bozack;

public class Note {
    private static int MAX_NOTE    = 100;
    private static int PITCH_SCALE = 12;

    private int note;
    private int octav;
    private int pitch;

    public Note(int note) {
        if (note < 0 || note > MAX_NOTE) {
            throw new IllegalArgumentException();
        }
        this.note  = note;
        this.octav = (note / PITCH_SCALE);
        this.pitch = note - (this.octav * PITCH_SCALE);
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
        sb.append("pitch=" + this.pitch);
        return sb.toString();
    }
}


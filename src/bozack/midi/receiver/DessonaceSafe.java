package bozack.midi.receiver;

import javax.sound.midi.*;
import bozack.midi.receiver.CustomReceiver;
import bozack.Note;
import bozack.NoteList;
import bozack.NoteSet;

public final class DessonaceSafe
    extends CustomReceiver {

    private static final int HISTORY_SCAN_LENGTH = 3;
    private static final int NOTE_SCAN_LENGTH = 12;
    private NoteList fixedOnNoteHistory = new NoteList();

    protected void handleShortMessage(ShortMessage sm, long timeStamp) {
        switch(sm.getCommand()) {
            case ShortMessage.NOTE_ON:
                Note note = new Note(sm.getData1());
                Note locatedNote = this.safelocate(note);

                this.defaultChannel.noteOn(
                    locatedNote.getNote(), sm.getData2());
                dumpMessage(sm);
                break;
            case ShortMessage.NOTE_OFF:
                this.defaultChannel.noteOff(
                    sm.getData1(), sm.getData2());
                dumpMessage(sm);
                break;
            case ShortMessage.CONTROL_CHANGE:
                if (176 == sm.getStatus()) {
                    System.out.println("desonance limit to: "
                        + sm.getData2());
                }
                break;
        }
    }

    private Note safelocate(Note note) {
        int size = this.fixedOnNoteHistory.size();
        if (0 == size) {
            this.fixedOnNoteHistory.add(note);
            return note;
        }
        System.out.println("fixedOnNoteHistory:" + size);

        // determin scan direction
        Note lastNote = this.fixedOnNoteHistory.get((size - 1));
        boolean isUpperScan = true;
        int iterMove = 1;
        if (lastNote.getNote() > note.getNote()) {
            isUpperScan = false;
            iterMove = -1;
        }
        System.out.println("scan mode:" + 
            (isUpperScan ? "upper" : "lower"));

        Note mostConsNote = null;
        double leastDes   = 100.0d;

        NoteSet seen = new NoteSet();

        int cur_move = 0;
        for (int note_cur = note.getNote();
            cur_move < NOTE_SCAN_LENGTH;
            note_cur += iterMove) {

            Note curNote = new Note(note_cur);
            if (seen.contains(curNote)) {
                continue;
            }
            seen.add(curNote);

            System.out.println(" <case> " + curNote.getNote()
                 + " - " + curNote.toString());

            int totalDessonance = 0;
            double a = 0.0d;
            for (int index = size;
                (index > (size - HISTORY_SCAN_LENGTH) && index > 0);
                index--) {
                Note historyNote 
                    = this.fixedOnNoteHistory.get((index - 1));
                double des = historyNote.getDessonance(curNote);
                double desPoint = des * 10000.0d;
                int intdes = (int) desPoint;
                totalDessonance += intdes;
                /* System.out.println(" -- (" + index + ") " 
                    + historyNote.getNote() + " "
                    + des + " "
                    + historyNote.toString() + " "
                    ); */
                System.out.println("    > des=" + des 
                    + " desPOint=" + desPoint 
                    + " intdes=" + intdes
                    + " totalDessonance=" + totalDessonance
                    );
            }
            System.out.println("   total dessonance = " + totalDessonance);
            cur_move++;

            if (totalDessonance < leastDes) {
                leastDes = totalDessonance;
                mostConsNote = curNote;
            }
        }

        System.out.println(" * least dessonance = " + leastDes);
        System.out.println(" * selected note = " 
            + mostConsNote.toString());

        this.fixedOnNoteHistory.add(mostConsNote);
        return mostConsNote;
    }
}


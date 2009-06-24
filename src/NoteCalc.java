
import bozack.Note;
import bozack.ChromaName;
import java.text.DecimalFormat;

class NoteCalc {

    public static void main (String[] args) {
        System.out.println("note calc");

        Note C = new Note(ChromaName.C, 5);
        int cNote = C.getNote();
        for (int i = 12; i < (12 * 8); i++) {
            Note target = new Note(i);
            double des = C.getDessonance(target);
            String desStr = new DecimalFormat("##0.0000").format(des);
            System.out.println(desStr + " : " + target);

        }
    }
}


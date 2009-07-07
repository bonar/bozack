
import java.io.*;
import bozack.BridgeFrame;
import bozack.NoteSet;

class JackInstrument {
    public static void main (String[] arg) {

        BridgeFrame frame = new BridgeFrame();

        // command line operation
        InputStreamReader cmdin = new InputStreamReader(System.in);
        try {
            while (true) {
                int input = cmdin.read();
                System.out.print("[" + input + "]");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}




package bozack.midi.receiver;
import bozack.midi.receiver.CustomReceiver;
import javax.sound.midi.*;

public class ScaleMinorize
    extends CustomReceiver {

    public void handleMessage(MidiMessage message, long timeStamp) {
        if (this.debug) {
            dumpMessage(message);
        }

        if (message instanceof ShortMessage) {
            ShortMessage sm = ((ShortMessage)message);
            switch(sm.getCommand()) {
                case ShortMessage.NOTE_ON:
                    this.defaultChannel.noteOn(
                        minorize(sm.getData1()), sm.getData2());
                    break;
                case ShortMessage.NOTE_OFF:
                    this.defaultChannel.noteOff(
                        minorize(sm.getData1()), sm.getData2());
                    break;
            }
        }
    }

    private int minorize(int origin) {
        int minorized = origin;
        switch (origin % 12) {
            case 4:
                minorized--;
                break;
            case 9:
                minorized--;
                break;
            case 11:
                minorized--;
                break;
        }
        return minorized;
    }
}


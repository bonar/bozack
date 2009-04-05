
package bozack.midi.receiver;
import bozack.midi.receiver.CustomReceiver;
import javax.sound.midi.*;

public class ScaleMinorize
    extends CustomReceiver {

    public void handleShortMessage(ShortMessage sm, long timeStamp) {
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


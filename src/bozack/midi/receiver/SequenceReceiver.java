
package bozack.midi.receiver;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class SequenceReceiver implements Receiver {
    public void close() { }
    public void send(MidiMessage message, long timeStamp) {
        System.out.println(message);
    }
}


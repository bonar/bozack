
package bozack.midi;
import javax.sound.midi.*;

public class CustomReceiver
    implements javax.sound.midi.Receiver {

    protected MidiDevice out_device;
    public void set_out_device(MidiDevice device) {
        this.out_device = device;
    }

    public void dump_message(MidiMessage message) {
        System.out.print("size:" + message.getLength() + " ");
        byte[] body = message.getMessage();
        for (int i = 0; i < message.getLength(); i++) {
            System.out.print("[" + body[i] + "]");
        }
        System.out.print(" status:" + message.getStatus() + " ");
        System.out.println("");
    }

    public void send(MidiMessage message, long timeStamp) {
        System.out.println(message.getMessage());
    }

    public void close() { }
}



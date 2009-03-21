
package bozack.midi;
import javax.sound.midi.*;

public class CustomReceiver
    implements javax.sound.midi.Receiver {

    protected Synthesizer synth;
    protected MidiChannel defaultChannel;
    public boolean debug = false;

    public void dest_synth(MidiDevice device) {
        if (!(device instanceof Synthesizer)) {
            throw new IllegalArgumentException(
                "device is not a Synthesizer");
        }
        this.synth = (Synthesizer)device;
    }

    public MidiChannel getDefaultChannel() {
        if (null == this.synth) {
            throw new IllegalStateException("synth not specified");
        }
        MidiChannel[] channels = this.synth.getChannels();
        if (0 == channels.length) {
            throw new IllegalStateException("no channels available");
        }
        return channels[0];
    }

    public void dumpMessage(MidiMessage message) {
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



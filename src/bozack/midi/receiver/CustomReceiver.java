
package bozack.midi.receiver;
import javax.sound.midi.*;

public class CustomReceiver
    implements javax.sound.midi.Receiver {

    protected Synthesizer synth;
    protected MidiChannel defaultChannel;
    public boolean debug = false;

    public void setSynth(MidiDevice device) {
        if (!(device instanceof Synthesizer)) {
            throw new IllegalArgumentException(
                "device is not a Synthesizer");
        }
        this.synth = (Synthesizer)device;

        // pickup first channel
        MidiChannel[] channels = this.synth.getChannels();
        if (0 == channels.length) {
            throw new IllegalStateException("no channels available");
        }
        this.defaultChannel = channels[0];
    }

    public void dumpMessage(MidiMessage message) {
        System.out.print("size:" + message.getLength() + " ");
        byte[] body = message.getMessage();
        for (int i = 0; i < message.getLength(); i++) {
            System.out.print("[" + body[i] + "]");
        }
        System.out.print(" status:" + message.getStatus() + " ");

        if (message instanceof ShortMessage) {
            ShortMessage sm = ((ShortMessage)message);
            System.out.print("(" + sm.getCommand() + ")");
            System.out.print("(" + sm.getData1() + ")");
            System.out.print("(" + sm.getData2() + ")");
            System.out.print(" ");
        }
        System.out.println("");
    }

    public void send(MidiMessage message, long timeStamp) {
        System.out.println(message.getMessage());
    }

    public void close() { }
}



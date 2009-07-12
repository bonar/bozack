
package bozack.ui;

import javax.swing.JPanel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

import bozack.Chord;
import bozack.ChordList;
import bozack.ChordProgression;
import bozack.midi.receiver.CustomReceiver;

public final class ConnectionPanel extends JPanel {
    private static final int WIDTH  = 1100;
    private static final int HEIGHT = 100;

    private final MidiDevice inDevice;
    private final MidiDevice outDevice;
    private final String receiverName;
    private final ChordProgression prog;

    private static final Font FONT_DEVICE_NAME
        = new Font("Serif", Font.PLAIN, 14);
    private static final Font FONT_RECEIVER_NAME
        = new Font("Serif", Font.PLAIN, 14);
    private static final Font FONT_DEVICE_DESC
        = new Font("Serif", Font.PLAIN, 10);
    private static final Font FONT_CHORD_NAME
        = new Font("Serif", Font.PLAIN, 13);
    private static final Font FONT_CHORD_KEY
        = new Font("Serif", Font.PLAIN, 9);

    public ConnectionPanel(MidiDevice in, MidiDevice out
        , String name, ChordProgression prog) {
        this.inDevice  = in;
        this.outDevice = out;
        this.receiverName = name;
        this.prog = prog;

        this. setBounds(0, 0, WIDTH, HEIGHT);
    }

    public void paintComponent(Graphics g) {
        MidiDevice.Info inDeviceInfo  = this.inDevice.getDeviceInfo();
        MidiDevice.Info outDeviceInfo = this.outDevice.getDeviceInfo();

        // connection section
        g.setColor(Color.orange.brighter().brighter());
        g.fillRect(10, 10, 200, 30);
        g.setColor(Color.black);
        g.drawRect(10, 10, 200, 30);
        g.setFont(FONT_DEVICE_NAME);
        String inName = inDeviceInfo.getName();
        g.drawString(
            inName.length() <= 25 ? inName : inName.substring(0, 24)
            , 15, 25);
        g.setColor(Color.gray.darker().darker());
        g.setFont(FONT_DEVICE_DESC);
        String inDesc = inDeviceInfo.getVendor() + " / "
            + inDeviceInfo.getDescription();
        g.drawString(
            inDesc.length() <= 45 ? inDesc : inDesc.substring(0, 44)
            , 15, 36);

        g.setColor(Color.black);
        g.drawLine(210, 25, 220, 25);

        g.setColor(Color.green.darker());
        g.fillRect(220, 10, 150, 30);
        g.setColor(Color.black);
        g.drawRect(220, 10, 150, 30);
        g.setColor(Color.black);
        g.setFont(FONT_RECEIVER_NAME);
        g.drawString("Receiver : " + this.receiverName, 227, 30);

        g.setColor(Color.black);
        g.drawLine(370, 25, 380, 25);

        g.setColor(Color.orange);
        g.fillRect(380, 10, 200, 30);
        g.setColor(Color.black);
        g.drawRect(380, 10, 200, 30);
        g.setFont(FONT_DEVICE_NAME);
        String outName = outDeviceInfo.getName();
        g.drawString(
            outName.length() <= 25 ? outName : outName.substring(0, 24)
            , 385, 25);
        g.setColor(Color.gray.darker().darker());
        g.setFont(FONT_DEVICE_DESC);
        String outDesc = outDeviceInfo.getVendor() + " / "
            + outDeviceInfo.getDescription();
        g.drawString(
            outDesc.length() <= 45 ? outDesc : outDesc.substring(0, 44)
            , 385, 36);

        // chord progression section
        g.setColor(Color.red.darker());
        g.fillRect(610, 10, 70, 30);
        g.setColor(Color.black);
        g.drawRect(610, 10, 70, 30);
        Chord currentChord = this.prog.getChord();
        g.setColor(Color.white);
        g.setFont(FONT_CHORD_KEY);
        g.drawString("CURSOR", 617, 20);
        g.setFont(FONT_CHORD_NAME);
        g.drawString(currentChord.toString(), 615, 35);

        ChordList list = this.prog.getNextChordList();
        int offset = 680;
        for (int i = 0; i < list.size(); i++) {
            int x = offset + (i * 70);
            g.setColor(Color.orange);
            g.fillRect(x, 10, 70, 30);
            g.setColor(Color.black);
            g.drawRect(x, 10, 70, 30);
            g.setFont(FONT_CHORD_KEY);
            g.drawString("F" + (i + 1), (x + 4), 20);

            Chord candidate = (Chord)list.get(i);
            g.setFont(FONT_CHORD_NAME);
            g.drawString(candidate.toString(), (x + 6), 35);
        }



    }

}



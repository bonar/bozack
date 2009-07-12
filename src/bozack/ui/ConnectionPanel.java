
package bozack.ui;

import javax.swing.JPanel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

import bozack.midi.receiver.CustomReceiver;

public final class ConnectionPanel extends JPanel {
    private static final int WIDTH  = 800;
    private static final int HEIGHT = 100;

    private final MidiDevice inDevice;
    private final MidiDevice outDevice;
    private final String receiverName;

    private static final Font FONT_DEVICE_NAME
        = new Font("Serif", Font.BOLD, 14);
    private static final Font FONT_RECEIVER_NAME
        = new Font("Serif", Font.PLAIN, 14);
    private static final Font FONT_DEVICE_DESC
        = new Font("Serif", Font.PLAIN, 10);

    public ConnectionPanel(MidiDevice in, MidiDevice out, String name) {
        this.inDevice  = in;
        this.outDevice = out;
        this.receiverName = name;

        this. setBounds(0, 0, WIDTH, HEIGHT);
    }

    public void paintComponent(Graphics g) {
        MidiDevice.Info inDeviceInfo  = this.inDevice.getDeviceInfo();
        MidiDevice.Info outDeviceInfo = this.outDevice.getDeviceInfo();

        g.setColor(Color.orange);
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
        g.drawLine(210, 25, 250, 25);

        g.setColor(Color.red);
        g.fillRect(250, 10, 150, 30);
        g.setColor(Color.black);
        g.drawRect(250, 10, 150, 30);
        g.setColor(Color.orange);
        g.setFont(FONT_RECEIVER_NAME);
        g.drawString("Receiver : " + this.receiverName, 255, 30);

        g.setColor(Color.black);
        g.drawLine(400, 25, 440, 25);

        g.setColor(Color.orange);
        g.fillRect(440, 10, 200, 30);
        g.setColor(Color.black);
        g.drawRect(440, 10, 200, 30);
        g.setFont(FONT_DEVICE_NAME);
        String outName = outDeviceInfo.getName();
        g.drawString(
            outName.length() <= 25 ? outName : outName.substring(0, 24)
            , 445, 25);
        g.setColor(Color.gray.darker().darker());
        g.setFont(FONT_DEVICE_DESC);
        String outDesc = outDeviceInfo.getVendor() + " / "
            + outDeviceInfo.getDescription();
        g.drawString(
            outDesc.length() <= 45 ? outDesc : outDesc.substring(0, 44)
            , 445, 36);


    }

}



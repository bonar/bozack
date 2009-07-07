
package bozack.ui;

import javax.swing.JFrame;
import java.util.ArrayList;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import java.awt.Container;

import bozack.Note;
import bozack.NoteSet;
import bozack.NoteHashMap;
import bozack.midi.Bridge;
import bozack.midi.PianoKeyEmulator;
import bozack.midi.receiver.CustomReceiver;
import bozack.midi.receiver.DumpRelay;
import bozack.midi.receiver.Stabilizer;
import bozack.midi.event.FramePainter;
import bozack.ui.KeyPanel;

public final class BridgeFrame extends JFrame {

    private static final int POS_X  = 60;
    private static final int POS_Y  = 10;
    private static final int WIDTH  = 1200;
    private static final int HEIGHT = 600;

    private static NoteSet noteSet;

    private static int IDX_COMP_KEYPANEL = 0;

    public BridgeFrame() {
        this.setBounds(POS_X, POS_Y, WIDTH, HEIGHT);

        ArrayList devices = Bridge.getDevices();
        bozack.midi.Bridge bridge = new bozack.midi.Bridge();
        CustomReceiver recv = new Stabilizer();
        recv.addNoteEventListener(new FramePainter(this));
        try {
            bridge.connect(
                (MidiDevice)(devices.get(0)),
                MidiSystem.getSynthesizer(),
                recv
            );
        } catch (MidiUnavailableException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

        this.noteSet = new NoteSet();
        this.paintKeyPanel(this.noteSet);

        // register Software Keyboard emulation
        this.addKeyListener(new PianoKeyEmulator(recv));

        this.setVisible(true);
    }
    
    public void paintKeyPanel (NoteSet onNote) {
        this.paintKeyPanel(onNote, new NoteSet(), new NoteHashMap());
    }

    public void paintKeyPanel (
        NoteSet onNote,
        NoteSet assistedNote,
        NoteHashMap pickupRelation)
    {
        Container contentPane = this.getContentPane();
        this.noteSet = onNote;
        KeyPanel kp = new KeyPanel(this.noteSet
            , assistedNote, pickupRelation);
        try {
            contentPane.remove(IDX_COMP_KEYPANEL);
        } catch (Exception e) { }
        contentPane.add(kp, IDX_COMP_KEYPANEL);
        contentPane.validate();
    }
}


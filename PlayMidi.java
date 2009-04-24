
package geneticmidi;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;

import java.io.File;

public class PlayMidi {

	//static Synthesizer synth;

	public static void main(String[] args) {

		Sequencer sequencer;
		//Transmitter	seqTrans;
		//Receiver synthRcvr;
		//Track [] tracks;


		try {
			// get the transmitter for the sequencer
			sequencer = MidiSystem.getSequencer();
			//seqTrans = sequencer.getTransmitter();

			// get the receiver for the sythesizer
			//synth = MidiSystem.getSynthesizer();
			//synthRcvr = synth.getReceiver(); 

			// hook the transmitter and the receiver together
			//seqTrans.setReceiver(synthRcvr);	

			//File myMidiFile = new File("../midi_files/Freestyler.mid");
			File myMidiFile = new File("midi_files/test.mid");

			// Construct a Sequence object, and
			// load it into my sequencer.
			Sequence mySeq = MidiSystem.getSequence(myMidiFile);

			System.out.println(DebugMidi.sequenceInfoToString(mySeq));

			System.out.println(DebugMidi.sequenceEventsToString(mySeq));

			sequencer.setSequence(mySeq);
			sequencer.open();
			sequencer.start();
			//sequencer.stop();
			//sequencer.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}


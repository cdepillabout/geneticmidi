
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

	static Synthesizer synth;

	static long MICROSECONDS_PER_MINUTE = 60000000;

	public static void main(String[] args) {

		Sequencer sequencer;
		Transmitter	seqTrans;
		Receiver synthRcvr;
		Track [] tracks;


		try {
			// get the transmitter for the sequencer
			sequencer = MidiSystem.getSequencer();
			seqTrans = sequencer.getTransmitter();

			// get the receiver for the sythesizer
			synth = MidiSystem.getSynthesizer();
			synthRcvr = synth.getReceiver(); 

			// hook the transmitter and the receiver together
			seqTrans.setReceiver(synthRcvr);	

			//File myMidiFile = new File("../midi_files/Freestyler.mid");
			File myMidiFile = new File("midi_files/test.mid");

			// Construct a Sequence object, and
			// load it into my sequencer.
			Sequence mySeq = MidiSystem.getSequence(myMidiFile);

			System.out.println("Sequence Division Type: " + mySeq.getDivisionType());
			System.out.println("Sequence Resolution: " + mySeq.getResolution());
			System.out.println("Sequence Microsecond Length: " + 
					mySeq.getMicrosecondLength());
			System.out.println("Sequence Tick Length: " + mySeq.getTickLength());
			System.out.println("Sequence Tracks: " + mySeq.getTracks().length);
			System.out.println();

			tracks = mySeq.getTracks();

			for (int i = 0; i < tracks.length; i++)
			{
				System.out.println("Track " + i + ":");

				for (int j = 0; j < tracks[i].size(); j++)
				{
					MidiEvent midiEvent = tracks[i].get(j);
					MidiMessage midiMessage = midiEvent.getMessage();

					System.out.print("Event " + j + ": Tick " + 
							midiEvent.getTick() + "  (" + 
							DebugMidi.eventTypeToString(midiMessage.getStatus()) 
							+ ")");
					
					System.out.println("  " + 
							DebugMidi.eventToString(midiMessage.getStatus(), 
								midiMessage.getLength(), 
								midiMessage.getMessage()));
				}

				System.out.println();
			}

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


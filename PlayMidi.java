
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

	public static void main(String[] args) {

			File myMidiFile = new File(args[0]);

			Sequence mySeq = null;

			try{
				mySeq = MidiSystem.getSequence(myMidiFile);
				MidiHelper.play(mySeq);
				System.out.println("Sequence:\n" + DebugMidi.sequenceEventsToString(mySeq));

				while (MidiHelper.isPlaying())
				{
					// I need this to be constantly doing something....
					System.out.print("");
				}

				MidiHelper.closeSequencer();


			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}


	}

}


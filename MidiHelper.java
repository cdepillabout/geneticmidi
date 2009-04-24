
package geneticmidi;

import javax.sound.midi.ShortMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Sequence;
import javax.sound.midi.MidiSystem;

/**
 * Helper class for working with midi's.
 */
public class MidiHelper {

	static Sequencer sequencer;

	static
	{
		try {
			sequencer = MidiSystem.getSequencer();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static MidiEvent createNoteOnEvent(int tick, int note, int velocity)
	{
		return MidiHelper.createNoteOnEvent(tick, 0, note, velocity);
	}

	public static MidiEvent createNoteOnEvent(int tick, int channel, 
			int note, int velocity)
	{
		ShortMessage shortMessage = new ShortMessage();
		try {
			shortMessage.setMessage(ShortMessage.NOTE_ON, channel, note, velocity);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return new MidiEvent(shortMessage, tick);
	}

	public static MidiEvent createNoteOffEvent(int tick, int note, int velocity)
	{
		return MidiHelper.createNoteOffEvent(tick, 0, note, velocity);
	}

	public static MidiEvent createNoteOffEvent(int tick, int channel, 
			int note, int velocity)
	{
		ShortMessage shortMessage = new ShortMessage();
		try {
			shortMessage.setMessage(ShortMessage.NOTE_OFF, channel, note, velocity);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return new MidiEvent(shortMessage, tick);
	}

	public static void play(Sequence sequence)
	{
		try{
			if (!sequencer.isOpen())
			{
				sequencer.open();
			}
			sequencer.setSequence(sequence);
			sequencer.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}


}



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

	/**
	 * Initialize the sequencer.
	 */
	static
	{
		try {
			sequencer = MidiSystem.getSequencer();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/** 
	 * Create a note on event.
	 */
	public static MidiEvent createNoteOnEvent(int tick, int note, int velocity)
	{
		return MidiHelper.createNoteOnEvent(tick, 0, note, velocity);
	}

	/** 
	 * Create a note on event.
	 */
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

	/** 
	 * Create a note off event.
	 */
	public static MidiEvent createNoteOffEvent(int tick, int note, int velocity)
	{
		return MidiHelper.createNoteOffEvent(tick, 0, note, velocity);
	}

	/** 
	 * Create a note off event.
	 */
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

	/** 
	 * Play a sequence.  Stops playing any currently playing sequences.
	 */
	public static void play(Sequence sequence)
	{
		try{

			if (!sequencer.isOpen())
			{
				sequencer.open();
			}
			
			if (sequencer.isRunning())
			{
				sequencer.stop();
			}

			sequencer.setSequence(sequence);
			sequencer.start();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}



}


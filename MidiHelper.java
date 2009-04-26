
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

	/**
	 * Given a musical pitch (C, C#, D...), give back the midi value.
	 */
	public static int getValueFromNote(String noteAndOctave)
	{
		// I got this from here:
		// http://www.harmony-central.com/MIDI/Doc/table2.html
		if (!noteAndOctave.matches("^(C|C#|D|D#|E|F|F#|G|G#|A|A#|B)(-1|[0-9])$"))
		{
			System.out.println("note " + noteAndOctave + " is not a muscial note.");
			System.exit(1);
		}

		char note = noteAndOctave.charAt(0);

		int noteValue = -100;

		switch(note)
		{
			case 'C':
				noteValue = 0;
				break;
			case 'D':
				noteValue = 2;
				break;
			case 'E':
				noteValue = 4;
				break;
			case 'F':
				noteValue = 5;
				break;
			case 'G':
				noteValue = 7;
				break;
			case 'A':
				noteValue = 9;
				break;
			case 'B':
				noteValue = 11;
				break;
			default:
				System.out.println("This should never be reached.");
				System.exit(1);
				break;
		}

		boolean sharp = noteAndOctave.contains("#");

		// if it's sharp, the note value goes up by one
		if (sharp) 
		{
			//System.out.println("There is a sharp");
			noteValue += 1;
		}
		
		//System.out.println("notevalue: " + noteValue);

		int octaveIndex = -100;

		// set the starting index for the octave
		if (sharp) 
			octaveIndex = 2;
		else 
			octaveIndex = 1;

		//System.out.println("octaveIndex is " + octaveIndex);

		int octave = -100;

		try {
			// figure out the octave
			octave = Integer.parseInt( noteAndOctave.substring(octaveIndex) );
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		//System.out.println("octave is " + octave);

		return (octave + 1) * 12 + noteValue;
	}

	public static String getNoteFromValue(int value)
	{
		int octave = -100;

		octave = (int) Math.floor(value / 12) - 1;

		System.out.println("octave: " + octave);

		int noteValue = value - ((octave + 1) * 12);

		System.out.println("noteValue: " + noteValue);

		String note = "";

		switch (noteValue)
		{
			case 0:
				note = "C";	
				break;
			case 1:
				note = "C#";	
				break;
			case 2:
				note = "D";	
				break;
			case 3:
				note = "D#";	
				break;
			case 4:
				note = "E";	
				break;
			case 5:
				note = "F";	
				break;
			case 6:
				note = "F#";	
				break;
			case 7:
				note = "G";	
				break;
			case 8:
				note = "G#";	
				break;
			case 9:
				note = "A";	
				break;
			case 10:
				note = "A#";	
				break;
			case 11:
				note = "B";	
				break;
		}

		return note + octave;
	}


	public static void main(String [] args)
	{
		/*
		if (args.length > 0)
		{
			System.out.println(getValueFromNote(args[0]));
		}
		else
		{
			System.out.println(getValueFromNote("A#5"));
		}
		*/

		System.out.println(getNoteFromValue(1));

	}
		
}


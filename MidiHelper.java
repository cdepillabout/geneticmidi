
package geneticmidi;

import javax.sound.midi.ShortMessage;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;
import javax.sound.midi.Sequence;
import javax.sound.midi.MidiSystem;

import java.util.Vector;

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
	public static MidiEvent createNoteOnEvent(long tick, int note, int velocity)
	{
		return MidiHelper.createNoteOnEvent(tick, 0, note, velocity);
	}

	/** 
	 * Create a note on event.
	 */
	public static MidiEvent createNoteOnEvent(long tick, int channel, 
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
	public static MidiEvent createNoteOffEvent(long tick, int note, int velocity)
	{
		return MidiHelper.createNoteOffEvent(tick, 0, note, velocity);
	}

	/** 
	 * Create a note off event.
	 */
	public static MidiEvent createNoteOffEvent(long tick, int channel, 
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
	 * Given a musical pitch (C4, C#-1, D9...), give back the midi value.
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

	/**
	 * Given a midi picth value (0-127), give back the musical note.
	 */
	public static String getNoteFromValue(int value)
	{
		if (value > 127 || value < 0)
		{
			System.out.println("value is out of range");
			System.exit(1);
		}

		int octave = -100;

		octave = (int) Math.floor(value / 12) - 1;

		//System.out.println("octave: " + octave);

		int noteValue = value - ((octave + 1) * 12);

		//System.out.println("noteValue: " + noteValue);

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


	/** 
	 * Return true if event is a Note On event.
	 */
	public static boolean isNoteOnEvent(MidiEvent event)
	{
		return isNoteOnMessage(event.getMessage());
	}

	/** 
	 * Return true if message is a Note On message.
	 */
	public static boolean isNoteOnMessage(MidiMessage message)
	{
		// TODO: make sure velocity is not zero
		return message.getStatus() == ShortMessage.NOTE_ON;
	}

	/** 
	 * Return true if event is a Note Off event.
	 */
	public static boolean isNoteOffEvent(MidiEvent event)
	{
		return isNoteOffMessage(event.getMessage());
	}

	/** 
	 * Return true if message is a Note Off message.
	 */
	public static boolean isNoteOffMessage(MidiMessage message)
	{
		// TODO: change this so that it can be a note on event with 
		// velocity 0.
		return message.getStatus() == ShortMessage.NOTE_OFF;
	}

	/**
	 * Take the NOTE_ON event noteOn (which is event i in track), and 
	 * find the matching NOTE_OFF event.
	 */
	public static MidiEvent findMatchingNoteOff(Track track, int noteOnIndex, MidiEvent noteOn)
	{
		assert isNoteOnEvent(noteOn);

		for (int i = noteOnIndex; i < track.size(); i++)
		{
			MidiEvent event = track.get(i);
			if (isNoteOffEvent(event) && 
					(getNoteValue(noteOn) == getNoteValue(event)))
			{
				return event;
			}
		}

		System.out.println("Could not find note off event for: " + 
				DebugMidi.midiEventToString(noteOn));
		System.exit(1);
		return null;

	}

	/**
	 * Return the note value for a note on or note off event.
	 */
	public static int getNoteValue(MidiEvent noteOnOff)
	{
		assert isNoteOnEvent(noteOnOff) || isNoteOffEvent(noteOnOff);

		return getNoteValue(noteOnOff.getMessage());
	}

	/**
	 * Return the note value for a note on or off message.
	 */
	public static int getNoteValue(MidiMessage noteOnOff)
	{
		assert isNoteOnMessage(noteOnOff) || isNoteOffMessage(noteOnOff);

		return noteOnOff.getMessage()[1];
	}

	/**
	 * Return the velocity for a note on or note off event.
	 */
	public static int getVelocity(MidiEvent noteOnOff)
	{
		assert isNoteOnEvent(noteOnOff) || isNoteOffEvent(noteOnOff);

		return getVelocity(noteOnOff.getMessage());
	}

	/**
	 * Return the velocity for a note on or off message.
	 */
	public static int getVelocity(MidiMessage noteOnOff)
	{
		assert isNoteOnMessage(noteOnOff) || isNoteOffMessage(noteOnOff);

		return noteOnOff.getMessage()[2];
	}

	/**
	 * Return an array of all of the Notes in sequence.
	 */
	public static Vector<Note> getNotesFromTrack(Track track)
	{
		Vector<MidiEvent> midiEvents = new Vector<MidiEvent>();
		Vector<Note> notes = new Vector<Note>();

		for(int i = 0; i < track.size(); i++)
		{
			MidiEvent event = track.get(i);
			if (isNoteOnEvent(event))
			{
				MidiEvent noteOff = findMatchingNoteOff(track, i, event);
				// TODO: should this not be channel 0?
				notes.add(new Note(event.getTick(), noteOff.getTick() - event.getTick(),
							0, getNoteValue(event), getVelocity(event)));

			}
		}


		return notes;

	}

	public static Vector<Note> getNotesPlayingAtTick(Vector<Note> allNotes, long tick)
	{
		Vector<Note> playingNotes = new Vector<Note>();

		for (Note n : allNotes)
		{
			if (tick >= n.getStartTick() && tick < n.getEndTick())
			{
				playingNotes.add(n);
			}
		}

		return playingNotes;
	}


	public static void main(String [] args)
	{
		System.out.println("All notes:");
		System.out.println(
				getNotesFromTrack(IdealSequence.getIdealSequence().getTracks()[0]));


		long tick = 100;
		System.out.println("Notes playing at tick:" + tick);
		System.out.println(getNotesPlayingAtTick(
					getNotesFromTrack(IdealSequence.getIdealSequence().getTracks()[0]),
					tick));


	}
		
}


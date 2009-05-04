
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

			// if the sequencer hasn't been initialized, 
			// initialize it
			if (sequencer == null)
			{
				sequencer = MidiSystem.getSequencer();
			}

			// if the sequencer hasn't been opened, open it for playing
			if (!sequencer.isOpen())
			{
				sequencer.open();
			}
			
			// if the sequencer is running, we need to stop it
			if (sequencer.isRunning())
			{
				sequencer.stop();
			}

			// rewind it to the beginning and start playing again
			sequencer.setTickPosition(0);
			sequencer.setSequence(sequence);
			sequencer.start();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/** 
	 * Stop playing a sequence.
	 */
	public static void stopPlaying()
	{
		try{

			if (sequencer != null)
			{
				if (sequencer.isOpen())
				{
					if (sequencer.isRunning())
					{
						sequencer.stop();
						sequencer.setTickPosition(0);
					}
				}
			}

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

		// TODO: make sure this is correct --
		// if the the message is not on channel 0, it will
		// not be equal to ShortMessage.NOTE_ON, but 
		// ShortMessage.NOTE_ON + channel.
		// So, we have to check for a range of possible values.

		// Just this won't work.
		//return message.getStatus() == ShortMessage.NOTE_ON;

		return message.getStatus() >= 144 && 
			message.getStatus() < 160;
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
		
		// TODO: see note at isNoteOnMessage()

		//return message.getStatus() == ShortMessage.NOTE_OFF;
		

		return message.getStatus() >= 128 && 
			message.getStatus() < 144;
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
	 * Return the number of note on and note off events in track.
	 */
	public static int noteOnOffsInTrack(Track track)
	{
		int count = 0;

		for (int i = 0; i < track.size(); i++)
		{
			MidiEvent event = track.get(i);
			if (isNoteOnEvent(event) || isNoteOffEvent(event))
			{
				count++;
			}
		}

		return count;
	}


	/**
	 * Return a clone of a track.
	 */
	public static Track cloneTrack(Track track)
	{
		Track newTrack = null;

		try {
			Sequence seq = new Sequence(IdealSequence.getDivisionType(),
					IdealSequence.getResolution());
			newTrack = seq.createTrack();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		assert newTrack != null;


		for (int i = 0; i < track.size(); i++)
		{
			MidiEvent event = track.get(i);
			newTrack.add(event);
		}

		assert track.size() == newTrack.size();

		return newTrack;

	}

	/**
	 * Return an array of all of the Notes in sequence.
	 */
	public static Vector<Note> getNotesFromTrack(Track track)
	{
		Vector<MidiEvent> midiEvents = new Vector<MidiEvent>();
		Vector<Note> notes = new Vector<Note>();

		Track tempTrack = cloneTrack(track);

		while (noteOnOffsInTrack(tempTrack) > 0)
		{
			MidiEvent event = tempTrack.get(0);
			if (isNoteOnEvent(event))
			{
				MidiEvent noteOff = findMatchingNoteOff(tempTrack, 0, event);
				// TODO: should this not be channel 0?
				// TODO: is it okay to add the tempTrack here?
				notes.add(new Note(track, event.getTick(), noteOff.getTick() - event.getTick(),
							0, getNoteValue(event), getVelocity(event)));

				boolean removeNoteOn = tempTrack.remove(event);
				boolean removeNoteOff = tempTrack.remove(noteOff);

				if (!removeNoteOn)
				{
					System.out.println("Could not remove note on event" + 
							" in MidiHelper.getNotesFromTrack()");
					System.exit(1);
				}
				if (!removeNoteOff)
				{
					System.out.println("Could not remove note off event" + 
							" in MidiHelper.getNotesFromTrack()");
					System.exit(1);
				}

			}


		}


		return notes;

	}

	/**
	 * Return a vector containing all the notes from allNotes that are playing at tick.
	 */
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

	/**
	 * Find the same MidiEvent in track as the argument midiEvent.
	 */
	public static MidiEvent findSameEvent(Track track, MidiEvent midiEvent)
		throws Exception
	{
		//TODO: I don't know if this actually works
		for (int i = 0; i < track.size(); i++)
		{
			MidiEvent trackEvent = track.get(i);

			if (trackEvent.getTick() == midiEvent.getTick())
			{
				MidiMessage trackMessage = trackEvent.getMessage();
				MidiMessage thisMessage = midiEvent.getMessage();
				if (java.util.Arrays.equals(trackMessage.getMessage(),
							thisMessage.getMessage()))
				{
					return trackEvent;
				}
			}
		}

		System.out.println("There is no event " + 
				DebugMidi.midiEventToString(midiEvent) +
				" in track: " + DebugMidi.trackEventsToString(track));
		//System.exit(1);
		throw new Exception();

		//return null;

	}

	public static boolean isEqualMidiEvents(MidiEvent aEvent, MidiEvent bEvent)
	{
		assert aEvent != null && bEvent != null;
		assert isNoteOnEvent(aEvent) || isNoteOffEvent(aEvent); 
		assert isNoteOnEvent(bEvent) || isNoteOffEvent(bEvent);

		if (aEvent.getTick() != bEvent.getTick())
		{
			return false;
		}

		MidiMessage a = aEvent.getMessage();
		MidiMessage b = bEvent.getMessage();

		// test if they are note on messages
		if (isNoteOnMessage(a))
		{
			if (!isNoteOnMessage(b))
			{
				return false;
			}
		}

		// test if they are note off messages
		if (isNoteOnMessage(a))
		{
			if (!isNoteOnMessage(b))
			{
				return false;
			}
		}

		// make sure the rest of them is the same
		if (java.util.Arrays.equals(a.getMessage(), b.getMessage()))
		{
			return true;
		}

		return false;
	}

	public static void main(String [] args)
	{
		System.out.println("ideal sequence track: " +
				DebugMidi.trackEventsToString(
					IdealSequence.getIdealSequence().getTracks()[0]));
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


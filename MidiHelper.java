
package geneticmidi;

import javax.sound.midi.ShortMessage;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;
import javax.sound.midi.Sequence;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MetaMessage;

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
	 * Create a Set Tempo meta event. Takes a tempo in BPMs.
	 */
	public static MidiEvent createSetTempoEvent(long tick, long tempo)
	{
		// microseconds per quarternote
		long mpqn = DebugMidi.MICROSECONDS_PER_MINUTE / tempo;

		MetaMessage metaMessage = new MetaMessage();

		// create the tempo byte array
		byte [] array = new byte[]{0, 0, 0};

		for (int i = 0; i < 3; i++)
		{
			int shift = (3 - 1 - i) * 8;
			array[i] = (byte) (mpqn >> shift);
		}

		// now set the message
		try {
			metaMessage.setMessage(81, array, 3);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return new MidiEvent(metaMessage, tick);
	}

	/**
	 * Detect whether or not there is anything playing.
	 */
	public static boolean isPlaying()
	{
		try{
			if (sequencer == null)
			{
				return false;
			}

			// if it is not open, then it's not playing
			if (!sequencer.isOpen())
			{
				return false;
			}
			
			return sequencer.isRunning();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
			return false;
		}
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
	 * Close the sequencer (so that the program can exit instead of just hanging).
	 */
	public static void closeSequencer()
	{
		try{

			if (sequencer != null)
			{
				if (sequencer.isOpen())
				{
					stopPlaying();
					sequencer.close();
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
		// The status can be a range of values, depending on what channel it is on.
		// Also, the velocity cannot be zero, otherwise it is a note off message.
		return message.getStatus() >= 144 && 
			message.getStatus() < 160 && 
			getVelocity(message) > 0;
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
		// It is a note off event if the status indicates it is a 
		// note off message.  Or, if it is a note on message and
		// the velocity is zero.
		return (message.getStatus() >= 128 && 
				message.getStatus() < 144) ||
			(message.getStatus() >= 144 && 
			 message.getStatus() < 160 &&
			 getVelocity(message) == 0);
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
	public static Vector<Note> getNotesFromTrack(Track track, int channelNumber)
	{
		Vector<MidiEvent> midiEvents = new Vector<MidiEvent>();
		Vector<Note> notes = new Vector<Note>();

		Track tempTrack = cloneTrack(track);

		int index = 0;

		while (noteOnOffsInTrack(tempTrack) > 0)
		{
			MidiEvent event = tempTrack.get(index);

			// If this is a note on event, we want to remove it, and the
			// cooresponding note off from the track.
			if (isNoteOnEvent(event))
			{
				MidiEvent noteOff = findMatchingNoteOff(tempTrack, 0, event);

				notes.add(new Note(track, event.getTick(), noteOff.getTick() - event.getTick(),
							channelNumber, getNoteValue(event), getVelocity(event)));

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
			// We shouldn't ever get to a note off event
			// (because we should have removed it when we found it's note 
			// on event).
			else if (isNoteOffEvent(event))
			{
					System.out.println("Accidentally got to a note off event" + 
							" in MidiHelper.getNotesFromTrack()");
					System.exit(1);
			}
			// since we got to something that is neither a note on or a note off,
			// we have to increase our index and look at the next note.
			// Otherwise we will have an infinite loop.
			else 
			{
				index++;
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
		for (int i = 0; i < track.size(); i++)
		{
			MidiEvent trackEvent = track.get(i);

			if (isEqualMidiEvents(trackEvent, midiEvent))
			{
				return trackEvent;
			}
		}

		System.out.println("There is no event " + 
				DebugMidi.midiEventToString(midiEvent) +
				" in track: " + DebugMidi.trackEventsToString(track));
		throw new Exception();
	}

	/** 
	 * Tests it two events are the same event.
	 * (Their tick is the same, their note number is the same,
	 * and their velocity is the same.)
	 * Note: This does not make sure their channel is the same.
	 */
	public static boolean isEqualMidiEvents(MidiEvent aEvent, MidiEvent bEvent)
	{
		assert aEvent != null && bEvent != null;
		//assert isNoteOnEvent(aEvent) || isNoteOffEvent(aEvent); 
		//assert isNoteOnEvent(bEvent) || isNoteOffEvent(bEvent);
		

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
			else if (isNoteOnMessage(b))
			{
				return getVelocity(aEvent) == getVelocity(bEvent) &&
					getNoteValue(aEvent) == getNoteValue(bEvent);
			}
		}

		// test if they are note off messages
		if (isNoteOffMessage(a))
		{
			if (!isNoteOffMessage(b))
			{
				return false;
			}
			else if (isNoteOffMessage(b))
			{
				return getNoteValue(aEvent) == getNoteValue(bEvent);
			}
		}
		
		// test if they are metamessages
		if (a.getMessage()[0] == (byte)255 && b.getMessage()[0] == (byte)255)
		{
			return java.util.Arrays.equals(a.getMessage(), b.getMessage());
		}


		return false;
	}

	public static void main(String [] args)
	{
		System.out.println("ideal sequence track: " +
				DebugMidi.trackEventsToString(
					IdealSequence.getIdealSequence().getTracks()[1]));
		System.out.println("All notes:");
		System.out.println(
				getNotesFromTrack(IdealSequence.getIdealSequence().getTracks()[1], 1));


		long tick = 100;
		System.out.println("Notes playing at tick:" + tick);
		System.out.println(getNotesPlayingAtTick(
					getNotesFromTrack(IdealSequence.getIdealSequence().getTracks()[1], 1),
					tick));


	}
		
}


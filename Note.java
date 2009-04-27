
package geneticmidi;

import javax.sound.midi.Track;
import javax.sound.midi.MidiEvent;

/**
 * Helper class for adding notes easily to tracks.
 */
public class Note 
{
	Track track;

	long startTick;
	long lengthTicks;
	int channel;
	int note;
	int velocity;

	MidiEvent noteOnEvent;
	MidiEvent noteOffEvent;

	public Note (long startTick, long lengthTicks, int channel, int note, int velocity)
	{
		this.startTick = startTick;
		this.lengthTicks = lengthTicks;
		this.channel = channel;
		this.note = note;
		this.velocity = velocity;

		noteOnEvent = MidiHelper.createNoteOnEvent(startTick, channel, note, velocity);
		noteOffEvent = MidiHelper.createNoteOffEvent(startTick + lengthTicks, 
				channel, note, 127);
	}

	public Note (long startTick, long lengthTicks, int channel, String note, int velocity)
	{
		this(startTick, lengthTicks, channel, MidiHelper.getValueFromNote(note), velocity);
	}

	public Note (Track track, long startTick, long lengthTicks, 
			int channel, String note, int velocity)
	{
		this(track, startTick, lengthTicks, channel, 
				MidiHelper.getValueFromNote(note), velocity);

	}

	public Note (Track track, long startTick, long lengthTicks, 
			int channel, int note, int velocity)
	{
		this(startTick, lengthTicks, channel, note, velocity);

		this.track = track;
	}

	/**
	 * Remove this note from the track it is in.
	 * Errors out if it is not in a track.
	 */
	public void removeFromTrack()
	{
		assert track != null;

		// This is needed because track.remove() will only take a
		// reference to an exact MidiEvent.  It can't be an identical event. :-(
		track.remove(MidiHelper.findSameEvent(track, noteOnEvent));
		track.remove(MidiHelper.findSameEvent(track, noteOffEvent));
	}

	/**
	 * Set the note value for this Note.  If you want to update the MidiMessage on the
	 * track, you have to first removeFromTrack(), then setNoteValue(),
	 * then addToTrack().
	 */
	public void setNoteValue(int noteValue)
	{
		this.note = noteValue;

		noteOnEvent = MidiHelper.createNoteOnEvent(startTick, channel, note, velocity);
		noteOffEvent = MidiHelper.createNoteOffEvent(startTick + lengthTicks, 
				channel, note, 127);
	}

	/**
	 * Returns two if two notes have the same note value.
	 * This is used in the fitness function for a midi individual.
	 */
	@Override
	public boolean equals(Object otherNote)
	{
		if (otherNote instanceof Note)
		{
			return this.getNoteValue() == ((Note)otherNote).getNoteValue();
		}
		else
		{
			return false;
		}
	}

	public int getNoteValue()
	{
		return note;
	}

	public long getStartTick()
	{
		return startTick;
	}

	public long getEndTick()
	{
		return startTick + lengthTicks;
	}

	/**
	 * Add this note to the track it is associated with.
	 */
	public void addToTrack()
	{
		assert track != null;

		this.track.add(noteOnEvent);
		this.track.add(noteOffEvent);
	}

	/**
	 * Associate this note with a track.
	 */
	public void setTrack(Track track)
	{
		this.track = track;
	}

	public String toString()
	{
		String result = "";

		result += "Note: track=" + track + ", ";
		result += "start=" + startTick + ", ";
		result += "length=" + lengthTicks + ", ";
		result += "channel=" + channel + ", ";
		result += "value=" + note + " (" + 
			MidiHelper.getNoteFromValue(note) + "), ";
		result += "velocity=" + velocity;

		return result;
	}

}



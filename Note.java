
package geneticmidi;

import javax.sound.midi.Track;
import javax.sound.midi.MidiEvent;

/**
 * Helper class for adding notes easily to tracks.
 */
public class Note 
{
	Track track;

	int startTick;
	int lengthTicks;
	int channel;
	int note;
	int velocity;

	MidiEvent noteOnEvent;
	MidiEvent noteOffEvent;

	public Note (int startTick, int lengthTicks, int channel, int note, int velocity)
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

	public Note (Track track, int startTick, int lengthTicks, 
			int channel, int note, int velocity)
	{
		this(startTick, lengthTicks, channel, note, velocity);

		this.track = track;

		this.track.add(noteOnEvent);
		this.track.add(noteOffEvent);
	}

}



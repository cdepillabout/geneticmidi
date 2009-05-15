
package geneticmidi;

import javax.sound.midi.Track;
import javax.sound.midi.Sequence;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;

public class DebugMidi {

	public static long MICROSECONDS_PER_MINUTE = 60000000;

	/**
	 * Return the string representations of info about a sequence.
	 */
	public static StringBuilder sequenceInfoToString(Sequence sequence)
	{
		StringBuilder result = new StringBuilder();

		result.append("Sequence Division Type: ");
		result.append(sequence.getDivisionType());
		result.append("\n");

		result.append("Sequence Resolution: ");
		result.append(sequence.getResolution()); 
		result.append("\n");

		result.append("Sequence Microsecond Length: ");
		result.append(sequence.getMicrosecondLength());
		result.append("\n");

		result.append("Sequence Tick Length: ");
		result.append(sequence.getTickLength());
		result.append("\n");

		result.append("Sequence Tracks: ");
		result.append(sequence.getTracks().length);
		result.append("\n");


		return result;
	}

	/** 
	 * Return the string representation of a sequence.
	 */
	public static StringBuilder sequenceEventsToString(Sequence sequence)
	{
		StringBuilder result = new StringBuilder();

		Track[] tracks = sequence.getTracks();

		for (int i = 0; i < tracks.length; i++)
		{
			result.append("Track ");
			result.append(i);
			result.append(":\n");

			result.append(trackEventsToString(tracks[i]));
			result.append("\n");
		}

		return result;
	}

	/**
	 * Return the string representation of a Track.
	 */
	public static StringBuilder trackEventsToString(Track track)
	{
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < track.size() - 1; i++)
		{
			MidiEvent midiEvent = track.get(i);

			result.append("Event ");
			result.append(i);
			result.append(": ");

			result.append(midiEventToString(midiEvent));
			result.append("\n");

		}

		if (track.size() >= 0)
		{
			MidiEvent midiEvent = track.get(track.size() - 1);

			result.append("Event ");
			result.append((track.size() - 1));
			result.append(": ");
			result.append(midiEventToString(midiEvent));

		}

		return result;

	}


	/**
	 * Return the string representations of a MidiEvent.
	 */
	public static StringBuilder midiEventToString(MidiEvent midiEvent)
	{
		StringBuilder result = new StringBuilder();

		MidiMessage midiMessage = midiEvent.getMessage();

		result.append("Tick ");
		result.append(midiEvent.getTick());
		result.append("  (");
		result.append(DebugMidi.eventTypeToString(midiMessage.getStatus()));
		result.append(")");

		result.append("  ");
		result.append(midiMessageToString(midiMessage.getStatus(), 
						midiMessage.getLength(), 
						midiMessage.getMessage()));

		return result;
	}

	/**
	 * Return the midi event type from a status value from a MidiEvent.
	 * Example: eventType(midiMessage.getStatus())
	 */
	public static String eventTypeToString(int value)
	{
		// value modulates based on what channel is?
		// For example, if the event is a note on message
		// and it's on channel 0, value will be 144,
		// but if it's on channel 1, value will be 145.

		// 0x80
		if (value >= 128 && value < 144) {
			return "Note Off";
		}

		// 0x90
		if (value >= 144 && value < 160) {
			return "Note On [" + value + "]";
		}

		// 0xA0
		if (value >= 160 && value < 176) {
			return "Note Aftertouch";
		}

		// 0xB0
		if (value >= 176 && value < 192) {
			return "Controller";
		}

		// 0xC0
		if (value >= 192 && value < 208) {
			return "Program Change";
		}

		// 0xD0
		if (value >= 208 && value < 224) {
			return "Channel Aftertouch";
		}

		// 0xE0
		if (value >= 224 && value < 240) {
			return "Pitch Bend";
		}

		// 0xFF
		if (value == 255) {
			return "Meta Event";
		}


		return String.valueOf(value);
	}

	/**
	 * Return info about the midi event specified by bytes.
	 * value is the event type, and length is the length of 
	 * bytes.  bytes should be an array of bytes returned from
	 * the getMessage() method of the MidiMessage class.
	 */
	public static StringBuilder midiMessageToString(int value, int length, byte [] bytes)
	{
		// this is the default result		
		StringBuilder result = new StringBuilder("[");

		if (length > 1)
		{
			result.append(bytes[1]);

			for (int k = 2; k < length; k++)
			{
				result.append(", "); 
				result.append(bytes[k]);

			}
		}

		result.append("]");

		// Note Off
		if (value >= 128 && value < 144)
		{
			return getInfoNoteEvent(bytes[1], bytes[2]);
		}
		// Note On
		else if (value >= 144 && value < 160)
		{
			return getInfoNoteEvent(bytes[1], bytes[2]);
		}
		// Note Aftertouch
		else if (value >= 160 && value < 176)
		{
			return result;
		}
		// Controller
		else if (value >= 176 && value < 192)
		{
			return getInfoControllerEvent(bytes[1], bytes[2]);
		}
		// Program Change
		else if (value >= 192 && value < 208)
		{
			return getInfoProgramChangeEvent(bytes[1]);
		}
		// Channel Aftertouch
		else if (value >= 208 && value < 224)
		{
			return result;
		}
		// Pitch Bend
		else if (value >= 224 && value < 240)
		{
			return result;
		}
		// Meta Event
		else if (value == 255)
		{
			return getInfoMetaEvent(length, bytes);
		}
		// Unknown
		else 
		{
			return result;
		}
	}

	/**
	 * Return a string containing information about a Note event.
	 */
	protected static StringBuilder getInfoNoteEvent(byte note, byte velocity)
	{
		StringBuilder result = new StringBuilder();

		result.append("[");
		result.append("Note: ");
		result.append(note);
		result.append(" (");
		result.append(MidiHelper.getNoteFromValue(note));
		result.append(")");
		result.append(", ");
		result.append("Velocity: ");
		result.append(velocity);
		result.append("]");

		return result;
	}

	/** 
	 * Return a string containing information about a controller event.
	 */
	protected static StringBuilder getInfoControllerEvent(byte type, byte value)
	{
		StringBuilder result = new StringBuilder();
		String typeResult = "";


		switch (type)
		{
			// Bank Select
			case 0:
				typeResult = "Bank Select";
				break;
			// Main Volume
			case 7:
				typeResult = "Main Volume";
				break;
			// Pan
			case 10:
				typeResult = "Pan";
				break;
			default:
				typeResult = String.valueOf(type);
				break;
		}

		result.append("[");
		result.append("Controller Type: ");
		result.append(typeResult);
		result.append(", ");
		result.append("Value: ");
		result.append(value);
		result.append("]");


		return result;
	}

	/**
	 * Return a string containing information about instrument.
	 * (Right now it just returns the same byte).
	 */
	protected static StringBuilder getInfoProgramChangeEvent(byte instrument)
	{
		StringBuilder result = new StringBuilder();

		//Instrument tempInstrument = 
		//	synth.getDefaultSoundbank().getInstruments()[instrument];

		result.append("[");
		result.append("Instrument: ");
		result.append(instrument);
		result.append("]");


		return result;
	}

	/** 
	 * Return a string containing the info for a meta event.
	 * bytes should be the byte string returned from getMessage()
	 * of the MidiMessage class. length should be the length
	 * of bytes.
	 */
	protected static StringBuilder getInfoMetaEvent(int length, byte [] bytes)
	{
		StringBuilder metaString = new StringBuilder();
		
		int metaEvent = bytes[1];

		switch (metaEvent)
		{
			// Copyright notice
			case 2:
				// Make sure the lengths we know are the same.
				// Some thing is weird if this is not the case.
				assert (bytes[2] == length - 3);
				metaString.append("Copyright notice: ");
				metaString.append(new String(bytes, 3, bytes[2]));
				break;

			// Name
			case 3:
				// Make sure the lengths we know are the same.
				// Some thing is weird if this is not the case.
				assert (bytes[2] == length - 3);
				metaString.append("Name: ");
				metaString.append(new String(bytes, 3, bytes[2]));
				break;

			// Cue point
			case 7:
				// Make sure the lengths we know are the same.
				// Some thing is weird if this is not the case.
				assert (bytes[2] == length - 3);
				metaString.append("Cue point: ");
				metaString.append(new String(bytes, 3, bytes[2]));
				break;

			// End of Track
			case 47:
				metaString.append("End of Track");
				break;

			// Set Tempo
			case 81:
				// System.out.println(java.util.Arrays.toString(bytes));
				// Make sure the length is 3
				assert (bytes[2] == 3);
				metaString.append("Set Tempo (BPM): ");
				metaString.append(MICROSECONDS_PER_MINUTE / byteArrayToLong(bytes, 3, 3));
				break;

			// Set Time Signature
			case 88:
				//System.out.println(java.util.Arrays.toString(bytes));
				// Make sure the length is 4
				assert (bytes[2] == 4);
				metaString.append("Numerator: ");
				metaString.append(bytes[3]);
				metaString.append(", ");

				metaString.append("Denominator: ");
				metaString.append((int) Math.pow(bytes[4], 2));
				metaString.append(", ");

				metaString.append("Metronome Pulse: ");
				metaString.append(bytes[5]);
				metaString.append(", ");

				metaString.append("32nd Notes Per Quarter Note: ");
				metaString.append(bytes[6]);
				break;



			default:
				if (length > 1)
				{
					metaString.append(bytes[1]);
					for (int k = 2; k < length; k++)
					{
						metaString.append(", ");
						metaString.append(bytes[k]);
					}
				}
				break;
		}
	
		metaString.insert(0, "[");
		metaString.append("]");
		return metaString;
	}

	/** 
	 * Take a byte array and return the long value.
	 */
	protected static long byteArrayToLong(byte[] bytes, int offset, int length)
	{
		long value = 0;

		for (int i = 0; i < length; i ++)
		{
			int shift = (length - 1 - i) * 8;
			value += (bytes[i + offset] & 0x000000FF) << shift;
		}

		return value;
	}
}


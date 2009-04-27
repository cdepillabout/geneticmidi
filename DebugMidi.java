
package geneticmidi;

import javax.sound.midi.Track;
import javax.sound.midi.Sequence;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;

public class DebugMidi {

	static long MICROSECONDS_PER_MINUTE = 60000000;

	public static String sequenceInfoToString(Sequence sequence)
	{
		String result = "";

		result += "Sequence Division Type: " + sequence.getDivisionType() + "\n";
		result += "Sequence Resolution: " + sequence.getResolution() + "\n";
		result += "Sequence Microsecond Length: " + sequence.getMicrosecondLength() + "\n";
		result += "Sequence Tick Length: " + sequence.getTickLength() + "\n";
		result += "Sequence Tracks: " + sequence.getTracks().length + "\n";

		return result;
	}

	public static String sequenceEventsToString(Sequence sequence)
	{
		String result = "";

		Track[] tracks = sequence.getTracks();

		for (int i = 0; i < tracks.length; i++)
		{
			result += "Track " + i + ":" + "\n";
			result += trackEventsToString(tracks[i]);
			result += "\n";
		}

		return result;
	}

	public static String trackEventsToString(Track track)
	{
		String result = "";

		for (int i = 0; i < track.size() - 1; i++)
		{
			MidiEvent midiEvent = track.get(i);

			result += "Event " + i + ": ";
			result += midiEventToString(midiEvent) + "\n";
		}

		if (track.size() >= 0)
		{
			MidiEvent midiEvent = track.get(track.size() - 1);

			result += "Event " + (track.size() - 1) + ": ";
			result += midiEventToString(midiEvent);

		}

		return result;

	}

	public static String midiEventToString(MidiEvent midiEvent)
	{
		String result = "";

		MidiMessage midiMessage = midiEvent.getMessage();

		result += "Tick " + midiEvent.getTick() + "  (" + 
			DebugMidi.eventTypeToString(midiMessage.getStatus()) 
			+ ")";

		result += "  " + 
			DebugMidi.midiMessageToString(midiMessage.getStatus(), 
					midiMessage.getLength(), 
					midiMessage.getMessage());

			//System.out.println("getStatus() = " + midiMessage.getStatus());
			//System.out.println("getLength() = " + midiMessage.getLength());
			//System.out.println("getMessage() = " + midiMessage.getMessage());

		return result;
	}



	/**
	 * Return the midi event type from a status value from a MidiEvent.
	 * Example: eventType(midiMessage.getStatus())
	 */
	public static String eventTypeToString(int value)
	{
			// 0x80
			if (value >= 128 && value < 144) {
				return "Note Off";
			}

			// 0x90
			if (value >= 144 && value < 160) {
				return "Note On";
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
	public static String midiMessageToString(int value, int length, byte [] bytes)
	{
		String result = "";


		// this is the default result		
		result = "[";

		if (length > 1)
		{
			result += bytes[1];

			for (int k = 2; k < length; k++)
			{
				result += ", "  + bytes[k];
			}
		}

		result += "]";

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
	protected static String getInfoNoteEvent(byte note, byte velocity)
	{
		String result = "";

		result = "[";
		result += "Note: " + note + " (" + MidiHelper.getNoteFromValue(note) + ")";
		result += ", ";
		result += "Velocity: " + velocity;
		result += "]";

		return result;
	}

	/** 
	 * Return a string containing information about a controller event.
	 */
	protected static String getInfoControllerEvent(byte type, byte value)
	{
		String result = "";
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

		result = "[";
		result += "Controller Type: " + typeResult;
		result += ", ";
		result += "Value: " + value;
		result += "]";


		return result;
	}

	/**
	 * Return a string containing information about instrument.
	 * (Right now it just returns the same byte).
	 */
	protected static String getInfoProgramChangeEvent(byte instrument)
	{
		String result = "";

		//Instrument tempInstrument = 
		//	synth.getDefaultSoundbank().getInstruments()[instrument];

		result = "[";
		result += "Instrument: " + instrument;
		result += "]";


		return result;
	}

	/** 
	 * Return a string containing the info for a meta event.
	 * bytes should be the byte string returned from getMessage()
	 * of the MidiMessage class. length should be the length
	 * of bytes.
	 */
	protected static String getInfoMetaEvent(int length, byte [] bytes)
	{
		String metaString = "";
		
		int metaEvent = bytes[1];

		switch (metaEvent)
		{
			// Copyright notice
			case 2:
				// Make sure the lengths we know are the same.
				// Some thing is weird if this is not the case.
				assert (bytes[2] == length - 3);
				metaString = "Copyright notice: ";
				metaString += new String(bytes, 3, bytes[2]);
				break;

			// Name
			case 3:
				// Make sure the lengths we know are the same.
				// Some thing is weird if this is not the case.
				assert (bytes[2] == length - 3);
				metaString = "Name: ";
				metaString += new String(bytes, 3, bytes[2]);
				break;

			// Cue point
			case 7:
				// Make sure the lengths we know are the same.
				// Some thing is weird if this is not the case.
				assert (bytes[2] == length - 3);
				metaString = "Cue point: ";
				metaString += new String(bytes, 3, bytes[2]);
				break;

			// End of Track
			case 47:
				metaString = "End of Track";
				break;

			// Set Tempo
			case 81:
				// System.out.println(java.util.Arrays.toString(bytes));
				// Make sure the length is 3
				assert (bytes[2] == 3);
				metaString = "Set Tempo (BPM): ";
				metaString += MICROSECONDS_PER_MINUTE / byteArrayToLong(bytes, 3, 3);
				break;

			// Set Time Signature
			case 88:
				//System.out.println(java.util.Arrays.toString(bytes));
				// Make sure the length is 4
				assert (bytes[2] == 4);
				metaString += "Numerator: " + bytes[3] + ", ";
				metaString += "Denominator: " + (int) Math.pow(bytes[4], 2) + ", ";
				metaString += "Metronome Pulse: " + bytes[5] + ", ";
				metaString += "32nd Notes Per Quarter Note: " + bytes[6];
				break;



			default:
				if (length > 1)
				{
					metaString += bytes[1];
					for (int k = 2; k < length; k++)
					{
						metaString += ", "  + bytes[k];
					}
				}
				break;
		}
	
		return "[" + metaString + "]";
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


package geneticmidi;

import javax.sound.midi.Sequence;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import java.util.Vector;

public class IdealSequence {

	static Sequence sequence;

	static 
	{
		try 
		{
			sequence = new Sequence(0, 480);

			//Track myTrack0 = myNewMidi.createTrack();
			Track myTrack1 = sequence.createTrack();

			//MetaMessage metaMessage = new MetaMessage();
			//metaMessage.setMessage(MetaMessage.META, 
			//		new byte[]{(byte)255, (byte)192, 0}, 2);
			//myTrack.add(new MidiEvent(metaMessage, 0));


			/*
			// set tempo
			MetaMessage tempo = new MetaMessage();
			tempo.setMessage(81, new byte[]{7, -95, 32}, 3);
			myTrack0.add(new MidiEvent(tempo, 0));

			// set time signature
			MetaMessage timeSignature = new MetaMessage();
			timeSignature.setMessage(88, new byte[]{4, 2, 24, 8}, 4);
			myTrack0.add(new MidiEvent(timeSignature, 0));

*/


			/*
			// change to piano
			ShortMessage programChange = new ShortMessage();
			programChange.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 0);
			myTrack1.add(new MidiEvent(programChange, 0));




			// set volume to 100
			ShortMessage volume = new ShortMessage();
			volume.setMessage(ShortMessage.CONTROL_CHANGE, 7, 100);
			myTrack1.add(new MidiEvent(volume, 0));



			// set pan to 64
			ShortMessage pan = new ShortMessage();
			pan.setMessage(ShortMessage.CONTROL_CHANGE, 10, 64);
			myTrack1.add(new MidiEvent(pan, 0));

*/

			
			(new Note(myTrack1, 0, 480, 0, "C4", 100)).addToTrack();
			(new Note(myTrack1, 480, 480, 0, "E4", 100)).addToTrack();
			(new Note(myTrack1, 960, 480, 0, "G4", 100)).addToTrack();
			(new Note(myTrack1, 1440, 480, 0, "C5", 100)).addToTrack();
			
			

			
			/*
			(new Note(myTrack1, 100, 1102, 0, "C4", 100)).addToTrack();
			(new Note(myTrack1, 100, 480, 0, "E4", 100)).addToTrack();
			(new Note(myTrack1, 100, 20, 0, "G4", 100)).addToTrack();
			(new Note(myTrack1, 100, 480, 0, "C5", 100)).addToTrack();
			*/
			


		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		} 

		//System.out.println(DebugMidi.sequenceInfoToString(sequence));
		//System.out.println(DebugMidi.sequenceEventsToString(sequence));


	}

	public static Sequence getIdealSequence() {
		return sequence;
	}

	public static float getDivisionType() {
		return sequence.getDivisionType();
	}

	public static int getResolution() {
		return sequence.getResolution();
	}

	/**
	 * Return an array with all the notes from track 0.
	 */
	public static Vector<Note> getNotes()
	{
		return MidiHelper.getNotesFromTrack(sequence.getTracks()[0]);
	}

	public static void main(String[] args) {
		//FlowerBed bed = new FlowerBed();
		//System.out.println("fitness: " + bed.fitness());

		System.out.println(DebugMidi.sequenceEventsToString(sequence));
		MidiHelper.play(sequence);
	}

}


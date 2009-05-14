package geneticmidi;

import javax.sound.midi.Sequence;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.Track;
import javax.sound.midi.MidiSystem;

import java.util.Vector;

import java.io.File;

public class IdealSequence {

	static Sequence sequence;

	static 
	{
		//setProgrammedSequence();
		//setSequenceFromFile("../test-what-what-what.mid");
		setSequenceFromFile("../midi_files/Ibiza.mid");
	}

	protected static void setProgrammedSequence()
	{
		try 
		{
			sequence = new Sequence(0, 480);

			Track myTrack0 = sequence.createTrack();
			Track myTrack1 = sequence.createTrack();
			Track myTrack2 = sequence.createTrack();
			
			// create tempo event
			myTrack0.add(MidiHelper.createSetTempoEvent(0, 120));

			// set time signature
			MetaMessage timeSignature = new MetaMessage();
			timeSignature.setMessage(88, new byte[]{4, 2, 24, 8}, 4);
			myTrack0.add(new MidiEvent(timeSignature, 0));

			

			//Change instrument
			ShortMessage programChange1 = new ShortMessage();
			programChange1.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 115, 999);
			myTrack0.add(new MidiEvent(programChange1, 0));

			ShortMessage programChange = new ShortMessage();
			programChange.setMessage(ShortMessage.PROGRAM_CHANGE, 1, 10, 999);
			myTrack1.add(new MidiEvent(programChange, 0));

			ShortMessage programChange2 = new ShortMessage();
			programChange2.setMessage(ShortMessage.PROGRAM_CHANGE, 2, 33, 999);
			myTrack2.add(new MidiEvent(programChange2, 0));

			

			// set volume to 100 -- channel 1
			ShortMessage volume = new ShortMessage();
			volume.setMessage(ShortMessage.CONTROL_CHANGE, 1, 7, 100);
			myTrack1.add(new MidiEvent(volume, 0));

			// set pan to 64 -- channel 1
			ShortMessage pan = new ShortMessage();
			pan.setMessage(ShortMessage.CONTROL_CHANGE, 1, 10, 64);
			myTrack1.add(new MidiEvent(pan, 0));

			// two C major arpeggios
			for (int i = 0; i < 4; i++)
			{
				int noteValue = 0;

				switch (i % 4)
				{
					case 0:
						noteValue = 60;
						break;
					case 1:
						noteValue = 64;
						break;
					case 2:
						noteValue = 67;
						break;
					case 3:
						noteValue = 72;
						break;
				}

				int length = 2500;
				int total_notes = 3;

				/*
				(new Note(myTrack0, i * length + 0 * length / total_notes, 
						  length / total_notes, 
						  0, noteValue, 100)).addToTrack();
						  */

				(new Note(myTrack1, i * length + 1 * length / total_notes, 
						  length / total_notes, 
						  1, noteValue, 100)).addToTrack();

				
				(new Note(myTrack2, i * length + 2 * length / total_notes, 
						  length / total_notes, 
						  2, noteValue, 100)).addToTrack();
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		} 

	}

	public static void setSequenceFromFile(String filename)
	{
		File midiFile = new File(filename);

		try {
			sequence = MidiSystem.getSequence(midiFile);
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		} 

	}

	public static Sequence getIdealSequence() 
	{
		return sequence;
	}

	public static float getDivisionType() 
	{
		return sequence.getDivisionType();
	}

	public static int getResolution() 
	{
		return sequence.getResolution();
	}

	public static double perfectFitness()
	{
		try {
			Sequence tempSequence = new Sequence(sequence.getDivisionType(),
					sequence.getResolution());

			Vector <MidiIndividualTrack> tracks = new Vector<MidiIndividualTrack>();

			for (int i = 0; i < sequence.getTracks().length; i++)
			{
				tracks.add(new MidiIndividualTrack(tempSequence.createTrack(),
							i, IdealSequence.getNotes(i)));
			}

			MidiIndividual perfectIndividual = new MidiIndividual(tracks);

			return perfectIndividual.fitness();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return 0;

	}

	/**
	 * Return an array with all the notes from track (channel) trackNumber.
	 */
	public static Vector<Note> getNotes(int trackNumber)
	{
		assert trackNumber >= 0 && trackNumber < sequence.getTracks().length;

		return MidiHelper.getNotesFromTrack(sequence.getTracks()[trackNumber], trackNumber);
	}

	public static void main(String[] args) {

		//System.out.println(MidiHelper.getNotesPlayingAtTick(
					//getNotes(0), 110));
		MidiHelper.play(sequence);
		System.out.println(DebugMidi.sequenceEventsToString(sequence));

		while (MidiHelper.isPlaying())
		{
			// pass
		}

		MidiHelper.closeSequencer();
	}

}


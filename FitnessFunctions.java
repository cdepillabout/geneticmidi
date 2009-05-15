package geneticmidi;

import javax.sound.midi.Track;
import java.util.Vector;

/**
 * This class holds static functions for determining the fitness of a track
 * compared to the ideal sequence.
 */
public class FitnessFunctions 
{


	public static double atTick(Track track, int channel) 
	{
		double fitness = 0;
		
		Vector<Note> idealSequenceNotes = IdealSequence.getNotes(channel);
		Vector<Note> ourNotes = MidiHelper.getNotesFromTrack(
				track, channel);

		// only do this if it is not empty
		if (!idealSequenceNotes.isEmpty())
		{
			// test this individual every FITNESS_TICK_AMOUNT ticks to see
			// if the right notes are being played
			for (long i = 0; i < idealSequenceNotes.lastElement().getEndTick(); 
					i += Population.FITNESS_TICK_AMOUNT)
			{
				Vector<Note> idealSequencePlayingNotes = 
					MidiHelper.getNotesPlayingAtTick(idealSequenceNotes, i);
				Vector<Note> ourSequencePlayingNotes = 
					MidiHelper.getNotesPlayingAtTick(ourNotes, i);

				// if the correct notes are being played, the fitness will increase.
				// if the wrong number of notes are being played, the fitness will decrease.
				if (idealSequencePlayingNotes.size() != 
						ourSequencePlayingNotes.size())
				{
					fitness -= 0.25;
				}
				else if (idealSequencePlayingNotes.equals(ourSequencePlayingNotes))
				{
					fitness += 1;
				}
			}
		}
		
		return fitness;
	}
	
}


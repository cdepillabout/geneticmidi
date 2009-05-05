package geneticmidi;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import java.util.Vector;


public class MidiHelperTest {
	
	@Before
	public void setUp() 
	{

	}

	@Test
	public void testGetNoteFromValue() 
	{
		assertEquals("C4", MidiHelper.getNoteFromValue(60));
		assertEquals("C#-1", MidiHelper.getNoteFromValue(1));
		assertEquals("F6", MidiHelper.getNoteFromValue(89));
		assertEquals("A#8", MidiHelper.getNoteFromValue(118));
		assertEquals("G2", MidiHelper.getNoteFromValue(43));
		assertEquals("E9", MidiHelper.getNoteFromValue(124));
		assertEquals("B0", MidiHelper.getNoteFromValue(23));
		assertEquals("G9", MidiHelper.getNoteFromValue(127));
		assertEquals("C-1", MidiHelper.getNoteFromValue(0));
	}

	@Test
	public void testGetValueFromNote() 
	{
		assertEquals(MidiHelper.getValueFromNote("C4"), 60);
		assertEquals(MidiHelper.getValueFromNote("C#-1"), 1);
		assertEquals(MidiHelper.getValueFromNote("F6"), 89);
		assertEquals(MidiHelper.getValueFromNote("A#8"), 118);
		assertEquals(MidiHelper.getValueFromNote("G2"), 43);
		assertEquals(MidiHelper.getValueFromNote("E9"), 124);
		assertEquals(MidiHelper.getValueFromNote("B0"), 23);
		assertEquals(MidiHelper.getValueFromNote("G9"), 127);
		assertEquals(MidiHelper.getValueFromNote("C-1"), 0);
	}

	@Test
	public void testCreateNoteOnEvent()
	{
		MidiEvent event1 = MidiHelper.createNoteOnEvent(0, 0, 0, 100);
		MidiEvent event2 = MidiHelper.createNoteOnEvent(0, 0, 0, 100);
		MidiEvent event3 = MidiHelper.createNoteOnEvent(10, 0, 0, 100);
		MidiEvent event4 = MidiHelper.createNoteOnEvent(20, 1, 5, 55);
		MidiEvent event5 = MidiHelper.createNoteOnEvent(20, 1, 5, 55);
		MidiEvent event6 = MidiHelper.createNoteOnEvent(20, 5, 6, 45);

		/*
		System.out.println("event1: " + DebugMidi.midiEventToString(event1));
		System.out.println("event2: " + DebugMidi.midiEventToString(event2));
		System.out.println("event3: " + DebugMidi.midiEventToString(event3));
		System.out.println("event4: " + DebugMidi.midiEventToString(event4));
		System.out.println("event5: " + DebugMidi.midiEventToString(event5));
		System.out.println("event6: " + DebugMidi.midiEventToString(event6));
		*/

		// make sure they are happening on the correct tick
		assertEquals(event1.getTick(), 0);
		assertEquals(event3.getTick(), 10);

		assertTrue(event1.getTick() == event2.getTick());
		assertTrue(event1.getTick() != event3.getTick());

		// make sure they are the same MidiMessage
		assertTrue(event1.getMessage().equals(event1.getMessage()));
		assertFalse(event1.getMessage().clone().equals(event1.getMessage()));
		assertFalse(event2.getMessage().equals(event1.getMessage()));

		// make event1 and event2 are the same midimessage
		assertTrue(java.util.Arrays.equals(event1.getMessage().getMessage(),
					event2.getMessage().getMessage()));
		assertEquals(event1.getMessage().getLength(), event2.getMessage().getLength());
		assertEquals(event1.getMessage().getStatus(), event2.getMessage().getStatus());

		// make sure event4 and event5 are the same midimessage
		assertTrue(java.util.Arrays.equals(event4.getMessage().getMessage(),
					event5.getMessage().getMessage()));
		assertEquals(event4.getMessage().getLength(), event5.getMessage().getLength());
		assertEquals(event4.getMessage().getStatus(), event5.getMessage().getStatus());

		// make sure event3 and event4 are not the same midimessage
		assertFalse(java.util.Arrays.equals(event4.getMessage().getMessage(),
					event3.getMessage().getMessage()));
		assertEquals(event4.getMessage().getLength(), event3.getMessage().getLength());
		assertFalse(event4.getMessage().getStatus() == event3.getMessage().getStatus());

		// make sure they are all note on events
		assertTrue(MidiHelper.isNoteOnEvent(event1));
		assertTrue(MidiHelper.isNoteOnEvent(event2));
		assertTrue(MidiHelper.isNoteOnEvent(event3));
		assertTrue(MidiHelper.isNoteOnEvent(event4));
		assertTrue(MidiHelper.isNoteOnEvent(event5));
		assertTrue(MidiHelper.isNoteOnEvent(event6));

		// make sure they are all not note off events
		assertFalse(MidiHelper.isNoteOffEvent(event1));
		assertFalse(MidiHelper.isNoteOffEvent(event2));
		assertFalse(MidiHelper.isNoteOffEvent(event3));
		assertFalse(MidiHelper.isNoteOffEvent(event4));
		assertFalse(MidiHelper.isNoteOffEvent(event5));
		assertFalse(MidiHelper.isNoteOffEvent(event6));

	}

	@Test
	public void testCreateNoteOffEvent()
	{
		MidiEvent event1 = MidiHelper.createNoteOffEvent(0, 0, 0, 100);
		MidiEvent event2 = MidiHelper.createNoteOffEvent(0, 0, 0, 100);
		MidiEvent event3 = MidiHelper.createNoteOffEvent(10, 0, 0, 100);
		MidiEvent event4 = MidiHelper.createNoteOffEvent(20, 1, 5, 55);
		MidiEvent event5 = MidiHelper.createNoteOffEvent(20, 1, 5, 55);
		MidiEvent event6 = MidiHelper.createNoteOffEvent(20, 5, 6, 45);

		/*
		System.out.println("event1: " + DebugMidi.midiEventToString(event1));
		System.out.println("event2: " + DebugMidi.midiEventToString(event2));
		System.out.println("event3: " + DebugMidi.midiEventToString(event3));
		System.out.println("event4: " + DebugMidi.midiEventToString(event4));
		System.out.println("event5: " + DebugMidi.midiEventToString(event5));
		System.out.println("event6: " + DebugMidi.midiEventToString(event6));
		*/

		// make sure they are happening on the correct tick
		assertEquals(event1.getTick(), 0);
		assertEquals(event3.getTick(), 10);

		assertTrue(event1.getTick() == event2.getTick());
		assertTrue(event1.getTick() != event3.getTick());

		// make sure they are the same MidiMessage
		assertTrue(event1.getMessage().equals(event1.getMessage()));
		assertFalse(event1.getMessage().clone().equals(event1.getMessage()));
		assertFalse(event2.getMessage().equals(event1.getMessage()));

		// make event1 and event2 are the same midimessage
		assertTrue(java.util.Arrays.equals(event1.getMessage().getMessage(),
					event2.getMessage().getMessage()));
		assertEquals(event1.getMessage().getLength(), event2.getMessage().getLength());
		assertEquals(event1.getMessage().getStatus(), event2.getMessage().getStatus());

		// make sure event4 and event5 are the same midimessage
		assertTrue(java.util.Arrays.equals(event4.getMessage().getMessage(),
					event5.getMessage().getMessage()));
		assertEquals(event4.getMessage().getLength(), event5.getMessage().getLength());
		assertEquals(event4.getMessage().getStatus(), event5.getMessage().getStatus());

		// make sure event3 and event4 are not the same midimessage
		assertFalse(java.util.Arrays.equals(event4.getMessage().getMessage(),
					event3.getMessage().getMessage()));
		assertEquals(event4.getMessage().getLength(), event3.getMessage().getLength());
		assertFalse(event4.getMessage().getStatus() == event3.getMessage().getStatus());

		// make sure they are not note on events
		assertFalse(MidiHelper.isNoteOnEvent(event1));
		assertFalse(MidiHelper.isNoteOnEvent(event2));
		assertFalse(MidiHelper.isNoteOnEvent(event3));
		assertFalse(MidiHelper.isNoteOnEvent(event4));
		assertFalse(MidiHelper.isNoteOnEvent(event5));
		assertFalse(MidiHelper.isNoteOnEvent(event6));

		// make sure they are all note off events
		assertTrue(MidiHelper.isNoteOffEvent(event1));
		assertTrue(MidiHelper.isNoteOffEvent(event2));
		assertTrue(MidiHelper.isNoteOffEvent(event3));
		assertTrue(MidiHelper.isNoteOffEvent(event4));
		assertTrue(MidiHelper.isNoteOffEvent(event5));
		assertTrue(MidiHelper.isNoteOffEvent(event6));

	}

	/** 
	 * This is hard because it will System.exit() if it can't 
	 * find a matching note off event.
	 */
	@Test
	public void testFindMatchingNoteOff()
	{
		try 
		{
			Sequence sequence = new Sequence(0, 480);
			Track track = sequence.createTrack();

			// add a new note to the track
			Note note1 = new Note(track, 0, 480, 0, "C5", 100);
			note1.addToTrack();

			// make sure that the first note in the track is the same as
			// note1's getNoteOnEvent().
			assertTrue(MidiHelper.isEqualMidiEvents(track.get(0), note1.getNoteOnEvent())); 

			// make sure the matching noteOff for this track is the same
			// as note1's getNoteOffEvent().
			assertTrue(
					MidiHelper.isEqualMidiEvents(
						MidiHelper.findMatchingNoteOff(track, 0, note1.getNoteOnEvent()),
						note1.getNoteOffEvent()
					) 
				);

			// add new note to track
			Note note2 = new Note(track, 100, 200, 0, "C5", 100);
			note2.addToTrack();
			
			// make sure it's the second note on the track and it's not equal to 
			// the first note
			assertTrue(MidiHelper.isEqualMidiEvents(track.get(1), note2.getNoteOnEvent()));
			assertFalse(MidiHelper.isEqualMidiEvents(track.get(1), note1.getNoteOnEvent()));

			// make sure the second note on event corresponds to it's own note off
			assertTrue(
					MidiHelper.isEqualMidiEvents(
						MidiHelper.findMatchingNoteOff(track, 0, note2.getNoteOnEvent()),
						note2.getNoteOffEvent()
					) 
				);

			// now note1's note off will correspond to note2's note off
			// (since they are the same note and note2's note off comes sooner)
			assertTrue(
					MidiHelper.isEqualMidiEvents(
						MidiHelper.findMatchingNoteOff(track, 0, note1.getNoteOnEvent()),
						note2.getNoteOffEvent()
					) 
				);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}

	}

	@Test
	public void testGetNoteValue()
	{
		Note note1 = new Note(0, 480, 0, 60, 100);
		Note note2 = new Note(0, 480, 0, 0, 100);
		Note note3 = new Note(0, 480, 0, "C#5", 100);
		Note note4 = new Note(0, 480, 0, "C#5", 100);

		assertEquals(MidiHelper.getNoteValue(note1.getNoteOnEvent()), 60);
		assertEquals(MidiHelper.getNoteValue(note1.getNoteOffEvent()), 60);

		assertEquals(MidiHelper.getNoteValue(note2.getNoteOnEvent()), 0);
		assertEquals(MidiHelper.getNoteValue(note2.getNoteOffEvent()), 0);

		assertFalse(MidiHelper.getNoteValue(note2.getNoteOffEvent()) ==
				MidiHelper.getNoteValue(note1.getNoteOffEvent()));

		assertEquals(MidiHelper.getNoteValue(note2.getNoteOffEvent()),
				MidiHelper.getNoteValue(note2.getNoteOnEvent()));

		assertEquals(MidiHelper.getNoteValue(note3.getNoteOffEvent()),
				MidiHelper.getNoteValue(note4.getNoteOffEvent()));

		assertEquals(MidiHelper.getNoteValue(note3.getNoteOnEvent()),
				MidiHelper.getNoteValue(note4.getNoteOnEvent()));
	}

	@Test
	public void testGetVelocity()
	{
		Note note1 = new Note(0, 480, 0, 60, 100);
		Note note2 = new Note(0, 480, 0, 0, 50);
		Note note3 = new Note(0, 480, 0, "C#5", 77);
		Note note4 = new Note(0, 480, 0, "C#5", 77);

		assertEquals(MidiHelper.getVelocity(note1.getNoteOnEvent()), 100);
		assertEquals(MidiHelper.getVelocity(note1.getNoteOffEvent()), 127);

		assertEquals(MidiHelper.getVelocity(note2.getNoteOnEvent()), 50);
		assertEquals(MidiHelper.getVelocity(note2.getNoteOffEvent()), 127);

		assertEquals(MidiHelper.getVelocity(note2.getNoteOffEvent()),
				MidiHelper.getVelocity(note1.getNoteOffEvent()));

		assertFalse(MidiHelper.getVelocity(note2.getNoteOnEvent()) ==
				MidiHelper.getVelocity(note1.getNoteOnEvent()));

		assertFalse(MidiHelper.getVelocity(note2.getNoteOffEvent()) ==
				MidiHelper.getVelocity(note2.getNoteOnEvent()));

		assertEquals(MidiHelper.getVelocity(note3.getNoteOffEvent()),
				MidiHelper.getVelocity(note4.getNoteOffEvent()));

		assertEquals(MidiHelper.getVelocity(note3.getNoteOnEvent()),
				MidiHelper.getVelocity(note4.getNoteOnEvent()));
	}


	@Test
	public void testNoteOnOffsInTrack()
	{

		try 
		{
			Sequence sequence = new Sequence(0, 480);
			Track track = sequence.createTrack();

			assertEquals(MidiHelper.noteOnOffsInTrack(track), 0);

			// add a new note to the track
			Note note1 = new Note(track, 0, 480, 0, "C5", 100);
			note1.addToTrack();

			assertEquals(MidiHelper.noteOnOffsInTrack(track), 2);
			track.remove(track.get(0));
			assertEquals(MidiHelper.noteOnOffsInTrack(track), 1);

			// add new note to track
			Note note2 = new Note(track, 100, 200, 0, "C5", 100);
			note2.addToTrack();
			
			assertEquals(MidiHelper.noteOnOffsInTrack(track), 3);
			track.remove(track.get(0));
			track.remove(track.get(0));
			track.remove(track.get(0));
			assertEquals(MidiHelper.noteOnOffsInTrack(track), 0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Test
	public void testCloneTrack()
	{
		try 
		{
			Sequence sequence = new Sequence(0, 480);
			Track trackA = sequence.createTrack();

			// add a new note to the track
			Note note1 = new Note(trackA, 0, 480, 0, "C5", 100);
			note1.addToTrack();
			
			// add new note to track
			Note note2 = new Note(trackA, 100, 200, 0, "C5", 100);
			note2.addToTrack();
			
			Track trackB = MidiHelper.cloneTrack(trackA);

			for (int i = 0; i < trackA.size(); i++)
			{
				assertTrue(MidiHelper.isEqualMidiEvents(trackA.get(i),
							trackB.get(i)));
			}

			assertTrue(MidiHelper.isEqualMidiEvents(trackB.get(0),
						note1.getNoteOnEvent()));
			assertTrue(MidiHelper.isEqualMidiEvents(trackB.get(1),
						note2.getNoteOnEvent()));
			assertTrue(MidiHelper.isEqualMidiEvents(trackB.get(2),
						note2.getNoteOffEvent()));
			assertTrue(MidiHelper.isEqualMidiEvents(trackB.get(3),
						note1.getNoteOffEvent()));

		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Test
	public void testGetNotesFromTrack()
	{
		try 
		{
			Sequence sequence = new Sequence(0, 480);
			Track trackA = sequence.createTrack();

			// add a new note to the track
			Note note1 = new Note(trackA, 0, 480, 0, "C5", 100);
			note1.addToTrack();
			
			// add new note to track
			Note note2 = new Note(trackA, 100, 200, 0, "C5", 100);
			note2.addToTrack();

			Vector<Note> trackNotes = MidiHelper.getNotesFromTrack(trackA);

			assertEquals(trackNotes.size(), 2);

			assertTrue(!note1.completelyEquals(trackNotes.get(0)));
			assertTrue(!note2.completelyEquals(trackNotes.get(1)));

			Track trackB = sequence.createTrack();

			// add a new note to the track
			Note note3 = new Note(trackB, 0, 200, 0, "C5", 100);
			note3.addToTrack();
			
			// add new note to track
			Note note4 = new Note(trackB, 200, 200, 0, "A4", 100);
			note4.addToTrack();

			Vector<Note> trackNotes2 = MidiHelper.getNotesFromTrack(trackB);

			assertEquals(trackNotes2.size(), 2);

			assertTrue(trackNotes2.get(0).completelyEquals(note3));
			assertTrue(trackNotes2.get(1).completelyEquals(note4));

			assertFalse(trackNotes2.get(0).completelyEquals(note1));
			assertFalse(trackNotes2.get(1).completelyEquals(note2));



			Track trackC = sequence.createTrack();
			Vector<Note> trackNotes3 = MidiHelper.getNotesFromTrack(trackC);
			assertEquals(trackNotes3.size(), 0);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Test
	public void testGetNotesPlayingAtTick()
	{
		try 
		{
			Sequence sequence = new Sequence(0, 480);
			Track trackA = sequence.createTrack();

			// add a new note to the track
			Note note1 = new Note(trackA, 0, 480, 0, "C5", 100);
			note1.addToTrack();
			
			// add new note to track
			Note note2 = new Note(trackA, 100, 200, 0, "C5", 100);
			note2.addToTrack();

			Note note3 = new Note(trackA, 0, 300, 0, "C5", 100);
			Note note4 = new Note(trackA, 100, 380, 0, "C5", 100);

			Vector<Note> trackNotes = MidiHelper.getNotesFromTrack(trackA);

			assertEquals(trackNotes.size(), 2);

			assertFalse(note1.completelyEquals(trackNotes.get(0)));
			assertFalse(note2.completelyEquals(trackNotes.get(1)));
			assertTrue(note3.completelyEquals(trackNotes.get(0)));
			assertTrue(note4.completelyEquals(trackNotes.get(1)));

			Vector<Note> playingNotes = MidiHelper.getNotesPlayingAtTick(trackNotes, 0);

			assertEquals(playingNotes.size(), 1);
			assertEquals(playingNotes.get(0), note3);

			playingNotes = MidiHelper.getNotesPlayingAtTick(trackNotes, 100);

			assertEquals(playingNotes.size(), 2);
			assertEquals(playingNotes.get(0), note3);
			assertEquals(playingNotes.get(1), note4);

			playingNotes = MidiHelper.getNotesPlayingAtTick(trackNotes, 400);

			assertEquals(playingNotes.size(), 1);
			assertEquals(playingNotes.get(0), note4);

			playingNotes = MidiHelper.getNotesPlayingAtTick(trackNotes, 500);

			assertEquals(playingNotes.size(), 0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}

	}

	@Test
	public void testFindSameEvent()
	{
		try 
		{
			Sequence sequence = new Sequence(0, 480);
			Track trackA = sequence.createTrack();

			// add a new note to the track
			Note note1 = new Note(trackA, 0, 480, 0, "C5", 100);
			note1.addToTrack();
			
			// add new note to track
			Note note2 = new Note(trackA, 100, 200, 0, "C5", 100);
			note2.addToTrack();

			assertEquals(MidiHelper.findSameEvent(trackA, note1.getNoteOnEvent()),
					note1.getNoteOnEvent());
			assertEquals(MidiHelper.findSameEvent(trackA, note2.getNoteOnEvent()),
					note2.getNoteOnEvent());
			assertEquals(MidiHelper.findSameEvent(trackA, note1.getNoteOffEvent()),
					note1.getNoteOffEvent());
			assertEquals(MidiHelper.findSameEvent(trackA, note2.getNoteOffEvent()),
					note2.getNoteOffEvent());

			Note note3 = new Note(0, 300, 0, "C5", 100);
			Note note4 = new Note(100, 380, 0, "C5", 100);


			assertEquals(MidiHelper.findSameEvent(trackA, note3.getNoteOnEvent()),
					note1.getNoteOnEvent());
			assertEquals(MidiHelper.findSameEvent(trackA, note3.getNoteOffEvent()),
					note2.getNoteOffEvent());

			Note note5 = new Note(0, 300, 1, "C5", 100);
			Note note6 = new Note(100, 380, 1, "C5", 100);

			assertEquals(MidiHelper.findSameEvent(trackA, note5.getNoteOnEvent()),
					note1.getNoteOnEvent());
			assertEquals(MidiHelper.findSameEvent(trackA, note5.getNoteOffEvent()),
					note2.getNoteOffEvent());
			assertEquals(MidiHelper.findSameEvent(trackA, note6.getNoteOnEvent()),
					note2.getNoteOnEvent());
			assertEquals(MidiHelper.findSameEvent(trackA, note6.getNoteOffEvent()),
					note1.getNoteOffEvent());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}

	}

	@Test
	public void testIsEqualMidiEvents()
	{
		Note note1 = new Note(0, 480, 0, "C5", 100);
		Note note2 = new Note(0, 480, 0, "C5", 100);
		Note note3 = new Note(0, 480, 1, "C5", 100);
		Note note4 = new Note(100, 480, 0, "C5", 100);
		Note note5 = new Note(0, 500, 0, "C5", 100);
		Note note6 = new Note(0, 480, 0, "C6", 100);
		Note note7 = new Note(0, 480, 0, "C5", 50);

		assertTrue(MidiHelper.isEqualMidiEvents(note1.getNoteOnEvent(),
					note1.getNoteOnEvent()));
		assertTrue(MidiHelper.isEqualMidiEvents(note2.getNoteOnEvent(),
					note1.getNoteOnEvent()));

		assertFalse(MidiHelper.isEqualMidiEvents(note1.getNoteOnEvent(),
					note1.getNoteOffEvent()));

		assertTrue(MidiHelper.isEqualMidiEvents(note3.getNoteOnEvent(),
					note1.getNoteOnEvent()));

		assertFalse(MidiHelper.isEqualMidiEvents(note4.getNoteOnEvent(),
					note1.getNoteOnEvent()));
		assertFalse(MidiHelper.isEqualMidiEvents(note4.getNoteOffEvent(),
					note1.getNoteOffEvent()));

		assertTrue(MidiHelper.isEqualMidiEvents(note5.getNoteOnEvent(),
					note1.getNoteOnEvent()));
		assertFalse(MidiHelper.isEqualMidiEvents(note5.getNoteOffEvent(),
					note1.getNoteOffEvent()));

		assertFalse(MidiHelper.isEqualMidiEvents(note6.getNoteOnEvent(),
					note1.getNoteOnEvent()));
		assertFalse(MidiHelper.isEqualMidiEvents(note6.getNoteOffEvent(),
					note1.getNoteOffEvent()));

		assertFalse(MidiHelper.isEqualMidiEvents(note7.getNoteOnEvent(),
					note1.getNoteOnEvent()));
		assertTrue(MidiHelper.isEqualMidiEvents(note7.getNoteOffEvent(),
					note1.getNoteOffEvent()));
	}

}

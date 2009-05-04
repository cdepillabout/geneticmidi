package geneticmidi;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiEvent;


public class MidiHelperTest {
	
	@Before
	public void setUp() 
	{

	}

	@Test
	public void testGetNoteFromValue() {
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
	public void testGetValueFromNote() {
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
}

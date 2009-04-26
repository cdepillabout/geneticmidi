package geneticmidi;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
}

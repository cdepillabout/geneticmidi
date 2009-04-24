
package geneticmidi;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MidiUnavailableException;

import java.io.File;

public class CreateMidi {


	static long MICROSECONDS_PER_MINUTE = 60000000;

	public static void main(String[] args) {

		Sequencer sequencer;
		Synthesizer synth;
		Transmitter	seqTrans;
		Receiver synthRcvr;
		Track [] tracks;

		Sequence myNewMidi = null;

		try {
			//File myMidiFile = new File("../midi_files/Freestyler.mid");
			File myMidiFile = new File("midi_files/test.mid");

			// Construct a Sequence object, and
			// load it into my sequencer.
			Sequence diskMidiSeq = MidiSystem.getSequence(myMidiFile);

			System.out.println(DebugMidi.sequenceInfoToString(diskMidiSeq));

			myNewMidi = new Sequence(diskMidiSeq.getDivisionType(),
					diskMidiSeq.getResolution());
			//Track myTrack0 = myNewMidi.createTrack();
			Track myTrack1 = myNewMidi.createTrack();

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
			

			


			ShortMessage shortMessage = new ShortMessage();

			shortMessage = (ShortMessage)shortMessage.clone();
			shortMessage.setMessage(ShortMessage.NOTE_ON, 0, 60, 100);
			myTrack1.add(new MidiEvent(shortMessage, 0));

			shortMessage = (ShortMessage)shortMessage.clone();
			shortMessage.setMessage(ShortMessage.NOTE_OFF, 0, 60, 127);
			myTrack1.add(new MidiEvent(shortMessage, 480));

			
			shortMessage = (ShortMessage)shortMessage.clone();
			shortMessage.setMessage(ShortMessage.NOTE_ON, 0, 64, 100);
			myTrack1.add(new MidiEvent(shortMessage, 480));

			shortMessage = (ShortMessage)shortMessage.clone();
			shortMessage.setMessage(ShortMessage.NOTE_OFF, 0, 64, 127);
			myTrack1.add(new MidiEvent(shortMessage, 960));

			shortMessage = (ShortMessage)shortMessage.clone();
			shortMessage.setMessage(ShortMessage.NOTE_ON, 0, 67, 100);
			myTrack1.add(new MidiEvent(shortMessage, 960));

			shortMessage = (ShortMessage)shortMessage.clone();
			shortMessage.setMessage(ShortMessage.NOTE_OFF, 0, 67, 127);
			myTrack1.add(new MidiEvent(shortMessage, 1440));

			shortMessage = (ShortMessage)shortMessage.clone();
			shortMessage.setMessage(ShortMessage.NOTE_ON, 0, 72, 100);
			myTrack1.add(new MidiEvent(shortMessage, 1440));

			shortMessage = (ShortMessage)shortMessage.clone();
			shortMessage.setMessage(ShortMessage.NOTE_OFF, 0, 72, 127);
			myTrack1.add(new MidiEvent(shortMessage, 1920));
		


		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}


		try {
			// get the transmitter for the sequencer
			sequencer = MidiSystem.getSequencer();

			// Construct a Sequence object, and
			// load it into my sequencer.
			//File myMidiFile = new File("midi_files/Freestyler.mid");
			//File myMidiFile = new File("midi_files/test.mid");
			//Sequence mySeq = MidiSystem.getSequence(myMidiFile);

			Sequence mySeq = myNewMidi;
			
			//MidiSystem.write(myNewMidi, 1, new File("./test-what.mid"));
			//File myMidiFile = new File("./test-what.mid");
			//Sequence mySeq = MidiSystem.getSequence(myMidiFile);

			System.out.println(DebugMidi.sequenceInfoToString(mySeq));
	
			System.out.println(DebugMidi.sequenceEventsToString(mySeq));

			sequencer.addMetaEventListener(new MetaEventListener()
					{
						public void meta(MetaMessage event)
						{
							if (event.getType() == 47)
							{
								System.exit(0);
							}
						}
					});


			sequencer.open();
			sequencer.setSequence(mySeq);

			seqTrans = sequencer.getTransmitter();

			// get the receiver for the sythesizer
			synth = MidiSystem.getSynthesizer();
			synthRcvr = synth.getReceiver(); 

			System.out.println("Synth Receiver: " + synthRcvr);
			System.out.println("Seq Trans' Receiver: " + seqTrans.getReceiver());
			System.out.println("Seq's Receiver: " + sequencer.getReceiver());
			//System.out.println("Sequencer Device Info: " + sequencer.getDeviceInfo());

			// hook the transmitter and the receiver together
			//seqTrans.setReceiver(synthRcvr);	


			sequencer.start();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}


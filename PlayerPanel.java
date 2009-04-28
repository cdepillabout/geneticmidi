
package geneticmidi; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.sound.midi.Sequence;


public class PlayerPanel extends JPanel
{

	/** 
	 * The sequence that this PlayerPanel is dealing with.
	 */
	protected Sequence sequence;

	/** 
	 * The label that holds the name of the sequence.
	 */
	protected JLabel name;

	public PlayerPanel(Sequence sequence, String sequenceName)
	{

		this.sequence = sequence;

		// add a border
		this.setBorder(BorderFactory.createEtchedBorder());

		// TODO: add pause button

		// create the buttons
		JButton startButton = new JButton(new ImageIcon("btnStart.png"));
		JButton stopButton = new JButton(new ImageIcon("btnStop.png"));

		// add action listeners to the buttons
		startButton.addActionListener(new StartPlayingAction());
		stopButton.addActionListener(new StopPlayingAction());

		
		// add the buttons to the panel
		add(startButton);
		add(stopButton);

		// TODO: add slider to show where in the midi file we are playing
		// (pg. 394 in Core Java Volume I);
		// JSlider slider = new JSlider(min, max, initialValue);

		// create a label with the name of the sequence
		name = new JLabel(sequenceName);

		// add the label to the panel
		add(name);
	}

	public void setName(String newSequenceName)
	{
		name.setText(newSequenceName);
	}

	public void setSequence(Sequence newSequence)
	{
		sequence = newSequence;
	}


	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
	}


	private class StartPlayingAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			//System.out.println("In start event listener, this sequence is: " + sequence);

			if (sequence != null)
			{
				MidiHelper.play(sequence);
			}
			else
			{
				System.out.println("This sequence is NULL!!!");
			}
		}
	}

	private class StopPlayingAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			//System.out.println("In stop event listener, this sequence is: " + sequence);

			MidiHelper.stopPlaying();
		}
	}

}

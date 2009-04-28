
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

	public static void main(String [] args)
	{
	}

	public PlayerPanel(Sequence sequence, String sequenceName)
	{
		/*
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setTitle("Midi Population Viewer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		*/

		this.sequence = sequence;

		System.out.println("This sequence is: " + sequence);

		//JButton startButton = new JButton("Start");
		//JButton stopButton = new JButton("Stop");

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

		// create a label with the name of the sequence
		JLabel name = new JLabel(sequenceName);

		// add the label to the panel
		add(name);
	}

	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
	}


	private class StartPlayingAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			System.out.println("In start event listener, this sequence is: " + sequence);

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
			System.out.println("In stop event listener, this sequence is: " + sequence);

			MidiHelper.stopPlaying();
		}
	}

}

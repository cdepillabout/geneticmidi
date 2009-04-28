
package geneticmidi;

import javax.swing.*;
import java.awt.*;

import javax.sound.midi.Sequence;

public class PopulationViewer extends JFrame
{
	public static final int DEFAULT_WIDTH = 600;
	public static final int DEFAULT_HEIGHT = 600;

	/** A main panel to hold everything in this frame.  Do I need this? */
	JPanel mainPanel;

	/**
	 * A panel holding the player for the ideal sequence.
	 */
	PlayerPanel idealSequencePlayer;

	/**
	 * A panel holding the player for the best sequence
	 * in the current generation.
	 */
	PlayerPanel bestIndividual;

	public static void main(String [] args)
	{
		PopulationViewer frame = new PopulationViewer();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public PopulationViewer(Population pop)
	{
		/*
		// all look and feels
		UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();

		for (UIManager.LookAndFeelInfo i : infos)
		{
			System.out.println("name = " + i.getName() + ", className = " +
					i.getClassName());
		}

		// update look and feel
		String plaf = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
		try {
			UIManager.setLookAndFeel(plaf);
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		*/

		// Create main panel to hold all the other panels
		mainPanel = new JPanel();
		add(mainPanel);

		// lets use the border layout manager with 20 pixel horizontal and vertical gaps
		mainPanel.setLayout(new BorderLayout(20, 20));

		// Create the players to play the two midi sequences
		PlayerPanel idealSequencePlayer = 
			new PlayerPanel(IdealSequence.getIdealSequence(), "Ideal");
		PlayerPanel bestIndividual = new PlayerPanel(null, "Best Individual");

		// add the players to the main panel
		mainPanel.add(idealSequencePlayer, BorderLayout.NORTH);
		mainPanel.add(bestIndividual, BorderLayout.SOUTH);

		// create the panel to hold list of individuals
		PopulationListPanel popPanel = new PopulationListPanel();

		// add the population list panel to the main panel
		mainPanel.add(popPanel, BorderLayout.CENTER);


		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setTitle("Midi Population Viewer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;

	}

}

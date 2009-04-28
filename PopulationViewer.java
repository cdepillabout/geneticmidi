
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

	public PopulationViewer()
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

		mainPanel = new JPanel();
		add(mainPanel);

		PlayerPanel idealSequencePlayer = 
			new PlayerPanel(IdealSequence.getIdealSequence());
		PlayerPanel bestIndividual = new PlayerPanel(null);

		mainPanel.add(idealSequencePlayer);
		mainPanel.add(bestIndividual);

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

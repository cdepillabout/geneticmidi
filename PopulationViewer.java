
package geneticmidi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
	PlayerPanel bestIndividualPlayer;

	/**
	 * A panel holding the list for the population.
	 */
	PopulationListPanel popPanel;



	public static void main(String [] args)
	{
		Population<MidiIndividual> pop = new Population<MidiIndividual>(
				new MidiIndividual());

		System.out.println("Generation " + 0 + ": ");
		MidiIndividual best = pop.bestIndividual();
		System.out.println("Best Individual: " + best + "'s fitness is "
				+ best.fitness());
		System.out.println();

		PopulationViewer frame = new PopulationViewer(pop);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public PopulationViewer(Population<MidiIndividual> pop)
	{
		// Create menu
		createMenu();
		
		// TODO: add file dialog for opening new midi file for ideal sequence
		// file dialog --> pg. 475 in core java

		// Create main panel to hold all the other panels
		mainPanel = new JPanel();
		add(mainPanel);

		// lets use the border layout manager with 20 pixel horizontal and vertical gaps
		mainPanel.setLayout(new BorderLayout(20, 20));

		// Create the players to play the two midi sequences
		idealSequencePlayer = 
			new PlayerPanel(IdealSequence.getIdealSequence(), "Ideal");
		bestIndividualPlayer = 
			new PlayerPanel( ((MidiIndividual)pop.bestIndividual()).getSequence(), 
					"Best Individual" );

		// add the players to the main panel
		mainPanel.add(idealSequencePlayer, BorderLayout.NORTH);
		mainPanel.add(bestIndividualPlayer, BorderLayout.SOUTH);

		// create the panel to hold list of individuals
		popPanel = new PopulationListPanel(pop, bestIndividualPlayer);

		// add the population list panel to the main panel
		mainPanel.add(popPanel, BorderLayout.CENTER);


		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setTitle("Midi Population Viewer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	protected void createMenu()
	{
		// create the menubar
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		// create the file menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		menuBar.add(fileMenu);

		Action exitAction = new 
			AbstractAction("Quit")
			{
				public void actionPerformed(ActionEvent event)
				{
					System.exit(0);
				}
			};
		
		exitAction.putValue(Action.MNEMONIC_KEY, new Integer('Q'));

		JMenuItem exitItem = new JMenuItem(exitAction);
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		fileMenu.add(exitItem);
	}

}

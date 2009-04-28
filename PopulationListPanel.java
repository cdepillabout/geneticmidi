
package geneticmidi; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//import javax.sound.midi.Sequence;


public class PopulationListPanel extends JPanel
{

	Population<MidiIndividual> pop;

	PlayerPanel bestIndividualPlayer;

	public static void main(String [] args)
	{
	}

	public PopulationListPanel(Population<MidiIndividual> pop,
			PlayerPanel bestIndividualPlayer)
	{
		this.pop = pop;
		this.bestIndividualPlayer = bestIndividualPlayer;

		JButton evolveOneGeneration = new JButton("Evolve 1 Generation");
		JButton evolveTenGenerations = new JButton("Evolve 10 Generations");
		JButton evolveOneHundredGenerations = new JButton("Evolve 100 Generations");

		// add action listeners to the buttons
		evolveOneGeneration.addActionListener(new EvolveAction(1));
		evolveTenGenerations.addActionListener(new EvolveAction(10));
		evolveOneHundredGenerations.addActionListener(new EvolveAction(100));

		
		// add the buttons to the panel
		add(evolveOneGeneration);
		add(evolveTenGenerations);
		add(evolveOneHundredGenerations);
	}

	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
	}



	private class EvolveAction implements ActionListener
	{
		private int evolutions;

		public EvolveAction(int evolutions)
		{
			this.evolutions = evolutions;
		}

		public void actionPerformed(ActionEvent event)
		{
			// set mouse cursor
			getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// TODO: make this multithreaded
			
			// TODO: actually evolve population -- be sure to update the
			// best individual player panel with the correct sequence

			MidiIndividual best = null;

			for (int i = 0; i < evolutions; i++)
			{
				pop.evolve();
				System.out.println("Generation " + pop.getGeneration() + ": ");
				best = pop.bestIndividual();
				System.out.println("Best Individual: " + best + "'s fitness is "
						+ best.fitness());
				System.out.println();
			}

			// set best individual sequence for playuer
			bestIndividualPlayer.setSequence(best.getSequence());
			bestIndividualPlayer.setName("Generation " + pop.getGeneration() + 
					" -- Best Individual");


			getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			//System.out.println("Called evolve action with evolutions = " + evolutions);
		}
	}


}

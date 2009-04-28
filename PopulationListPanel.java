
package geneticmidi; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//import javax.sound.midi.Sequence;


public class PopulationListPanel extends JPanel
{


	public static void main(String [] args)
	{
	}

	public PopulationListPanel()
	{

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
			
			// TODO: actually evolve population

			// TODO: uncomment this when I need it:
			// put mouse cursor back to normal
			//getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			System.out.println("Called evolve action with evolutions = " + evolutions);
		}
	}


}


package geneticmidi; 

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

//import javax.sound.midi.Sequence;


public class PopulationListPanel extends JPanel
{

	Population<MidiIndividual> pop;

	PlayerPanel bestIndividualPlayer;

	JTable populationTable; 

	public static void main(String [] args)
	{
	}

	public PopulationListPanel(Population<MidiIndividual> population,
			PlayerPanel bestIndivPlyr)
	{
		this.pop = population;
		this.bestIndividualPlayer = bestIndivPlyr;

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


		// TODO: create table.
		// to add scrollbar to table, just add table to a scroll pane
		// textArea = new JTextArea(8, 40);
		// JScrollPane scrollPane = new JScrollPane(textArea);
		TableModel model = new PopulationTableModel(this.pop);
		final SortFilterModel sorter = new SortFilterModel(model);
		populationTable = new JTable(sorter);
		add(new JScrollPane(populationTable));

		// make sure only one row is selectable at a time
		populationTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);

		// set up a listener to deal with a list item being selected
		// (update the player, etc...)
		final ListSelectionModel listSelectionModel = populationTable.getSelectionModel();
		listSelectionModel.addListSelectionListener(new 
				ListSelectionListener()
				{
					public void valueChanged(ListSelectionEvent e)
					{
						
						ListSelectionModel lsm = (ListSelectionModel)e.getSource();
						int minIndex = lsm.getMinSelectionIndex();
						int maxIndex = lsm.getMaxSelectionIndex();

						assert minIndex == maxIndex;

						if (maxIndex != -1)
						{

							int selection = (Integer) sorter.getValueAt(minIndex, 0);

							assert selection >= 0;

							Individual [] indvs = pop.getIndividuals();
							MidiIndividual midiIndv = (MidiIndividual)indvs[selection];

							// set player to play the selected sequence
							bestIndividualPlayer.setSequence(midiIndv.getSequence());
							bestIndividualPlayer.setName("Generation " + pop.getGeneration() + 
									" -- Individual " + selection);

							bestIndividualPlayer.getParent().repaint();

						}

					}

				});
		populationTable.setSelectionModel(listSelectionModel);
		


		// TODO: make only 18 rows visible -- the table has weird formatting
		// and you can't see the bottom of it.
		//populationTable.setVisibleRowCount(18);
		//populationTable.doLayout();

		// TODO: change this so that it will sort in decreaseing 
		// or increasing order.
		// set up click handler to sort the for column header
		populationTable.getTableHeader().addMouseListener(new
				MouseAdapter()
				{
					public void mouseClicked(MouseEvent event)
					{
						// find column of click
						int tableColumn = populationTable.columnAtPoint(event.getPoint());

						System.out.println("column point of click: " + tableColumn);

						// translate to table model index and sort
						int modelColumn = 
							populationTable.convertColumnIndexToModel(tableColumn);

						System.out.println("model column: " + tableColumn);

						sorter.sort(modelColumn);
					}
				});

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
			// (pg. 1 in core java volume 2)

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

			getParent().repaint();

			//System.out.println("Called evolve action with evolutions = " + evolutions);
		}
	}


}

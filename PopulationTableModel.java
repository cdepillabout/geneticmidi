
package geneticmidi;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

class PopulationTableModel extends AbstractTableModel
{

	protected Population<MidiIndividual> pop;

	protected String [] columnHeadings = { "Individual", "Fitness" };

	public PopulationTableModel(Population<MidiIndividual> pop)
	{
		this.pop = pop;
	}

	public int getRowCount()
	{
		Individual [] midiInds = pop.getIndividuals();
		return midiInds.length;

	}

	public int getColumnCount()
	{
		return columnHeadings.length;

	}

	public Object getValueAt(int row, int column)
	{
		Individual [] midiInds = pop.getIndividuals();

		assert row >= 0 && row < midiInds.length;

		if (column == 0)
		{
			return row;
		}
		else
		{
			return midiInds[row].fitness();
		}
	}

	public String getColumnName(int column)
	{
		return columnHeadings[column];
		
	}
}

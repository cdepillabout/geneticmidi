
package geneticmidi;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

/**
 * This table model takes an existing table model and produces a new model that
 * sorts the rows so that the entries in a given column are sorted.
 */
class SortFilterModel extends AbstractTableModel
{
	private TableModel model;
	private int sortColumn;
	private Row[] rows;

	/**
	 * Constructs a sort filter model.
	 * @param m the table whose rows should be sorted
	 */
	public SortFilterModel(TableModel m)
	{
		model = m;
		rows = new Row[model.getRowCount()];
		for (int i = 0; i < rows.length; i++)
		{
			rows[i] = new Row();
			rows[i].index = i;
		}
	}

	/**
	 * Sorts the rows.
	 * @param c the column that should be sorted.
	 */
	public void sort(int c)
	{
		sortColumn = c;
		Arrays.sort(rows);
		fireTableDataChanged();
	}

	// Compute the moved row for the three methods that access model elements
	
	public Object getValueAt(int r, int c)
	{
		return model.getValueAt(rows[r].index, c);
	}

	public boolean isCellEditable(int r, int c)
	{
		return model.isCellEditable(rows[r].index, c);
	}

	public void setValueAt(Object aValue, int r, int c)
	{
		model.setValueAt(aValue, rows[r].index, c);
	}

	// delegate all remaining methods to the model
	
	public int getRowCount() { return model.getRowCount(); }
	public int getColumnCount() { return model.getColumnCount(); }
	public String getColumnName(int c) { return model.getColumnName(c); }
	public Class getColumnClass(int c) { return model.getColumnClass(c); }




	/**
	 * This inner class holds the index of the model row.
	 * Rows are compared by looking at the model row entries
	 * in the sort column.
	 */
	private class Row implements Comparable<Row>
	{
		public int index;

		@SuppressWarnings("unchecked")
		public int compareTo(Row other)
		{
			Object a = model.getValueAt(index, sortColumn);
			Object b = model.getValueAt(other.index, sortColumn);

			if (a instanceof Comparable)
				return ((Comparable) a).compareTo(b);
			else
				return a.toString().compareTo(b.toString());
		}
	}

}

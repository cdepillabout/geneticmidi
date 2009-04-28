
import javax.swing.*;
import java.awt.*;

public class PopulationViewer extends JFrame
{
	public static final int DEFAULT_WIDTH = 600;
	public static final int DEFAULT_HEIGHT = 600;

	public static void main(String [] args)
	{
		PopulationViewer frame = new PopulationViewer();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public PopulationViewer()
	{
		PlayerPanel idealSequencePlayer = new PlayerPanel();
		PlayerPanel bestIndividual = new PlayerPanel();
		add(idealSequencePlayer);
		add(bestIndividual);

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

package genetic;

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.Point2D;
import static java.awt.Color.*;
import javax.swing.*;
import static javax.swing.JFrame.*;
import javax.imageio.*;

/** A program that draws and saves flower beds. */
@SuppressWarnings("serial")
public class BedViewer extends JPanel {

	/** The colors of the picture. */
	public static final Color BED = new Color(153, 102, 51),
			BACKGROUND = new Color(50, 100, 50), FLOWER1 = YELLOW,
			FLOWER2 = RED;

	/** Screen dimensions for both window and jpg views. */
	private static final int S_WIDTH = 1000, S_HEIGHT = 570;

	/** Constants used to convert from bed to screen coordinate systems. */
	private static final int X0 = 100, Y0 = S_HEIGHT - 100, SCALE = 400;

	private Population population;

	private JFrame window;

	public BedViewer(Population population) {
		this.population = population;

		//this.display();

		for(int i = 0; i < 500; i++)
		{
			if (i % 50 == 0)
			{
				System.out.println("Generation " + i);
			}

			/*
			try {
				Thread.sleep(2000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			*/
			population.evolve();
			//this.display();
		}

		this.save("bed-" + 
				String.format("%02d", FlowerBed.TOTAL_FLOWERS) +
				".jpg");
		this.writeCoordinateFile("coordinates-" + 
				String.format("%02d", FlowerBed.TOTAL_FLOWERS) +
				".txt");
	}

	/** Draw or redraw the FlowerBed in this BedViewer. */
	public void display() {
		if (window == null) {
			window = new JFrame();
			window.setSize(S_WIDTH, S_HEIGHT);
			window.setDefaultCloseOperation(EXIT_ON_CLOSE);
			window.add(this);
			window.setVisible(true);
		}
		window.repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		renderBed(g);
	}

	/** Draws the FlowerBed into the graphics object input. */
	private void renderBed(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		// draw the surrounding grassy area
		g2.setBackground(BACKGROUND);
		g2.clearRect(0, 0, S_WIDTH, S_HEIGHT);

		// draw the generation #
		Font font = new Font("Serif", Font.PLAIN, 28);
		g2.setFont(font);
		g2.setColor(Color.red);
		g2.setFont(font);
		String temp = "Generation " + population.getGeneration() + ": best fitness " +
			population.bestIndividual().fitness();
		g2.drawString(temp, 40, 40);


		// draw the flower bed
		g2.setColor(BED);
		// the rectangular base
		g2.fillRect(cx(0), cy(Math.sqrt(5)), 2 * SCALE,
				(int) ((Math.sqrt(5) - 2) * SCALE));
		// the curved top, with lots of overhang
		int radius = 3 * SCALE;
		int degrees = (int) Math.toDegrees(Math.acos(2.0 / 3.0));
		g2.fillArc(cx(0) - radius, cy(0) - radius, radius * 2, radius * 2,
				degrees, 90 - degrees);
		g2.clearRect(cx(0), cy(2), 2 * SCALE, 1 * SCALE); // cleaning up beneath
		// the bed
		g2.clearRect(cx(2), cy(3), 50, 1 * SCALE); // cleaning the right side


		FlowerBed bed = (FlowerBed) population.bestIndividual();
		// draw small flowers
		int length = bed.getSize();
		g2.setColor(FLOWER1);
		for (int i = 0; i < length; i++) {
			Point2D p = bed.getFlower(i);
			g2.fillOval(cx(p.getX()) - 6, cy(p.getY()) - 6, 10, 10);
		}

		// // draw the large flowers
		// if (bed.hasBigFlowers()) {
		// length = bed.getBigCount();
		// g2.setColor(FLOWER2);
		// for (int i = 0; i < length; i++) {
		// Point2D p = bed.getBigFlower(i);
		// g2.fillOval(cx(p.getX()) - 9, cy(p.getY()) - 9, 16, 16);
		// }
		// }
	}

	public void writeCoordinateFile(String filename)
	{
		try 
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			out.write("Flowers: " + FlowerBed.TOTAL_FLOWERS + "\n");
			out.write(((FlowerBed)population.bestIndividual()).getCoordinates());
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Save a FlowerBed as an image. <br>
	 * <br>
	 * Unless you specify an absolute filepath, the file will be relative to
	 * your "ai" directory in the Eclipse workspace. <br>
	 * <br>
	 * If no file extension is provided, the default is ".jpg".
	 * */
	public void save(String fileName) {

		BufferedImage image = new BufferedImage(S_WIDTH, S_HEIGHT,
				BufferedImage.TYPE_INT_RGB);

		renderBed(image.createGraphics());

		try {
			writeImage(image, fileName);
		} catch (IOException e) {
			System.out
					.println("Warning: Unable to save bed to the following filename: "
							+ fileName);
			e.printStackTrace();
		}
	}

	/** Converts raw x coordinate into screen position. */
	private static int cx(double x) {
		return (int) (X0 + x * SCALE);
	}

	/** Converts raw y coordinate into screen position. */
	private static int cy(double y) {
		return (int) (Y0 - (y - 2) * SCALE);
	}

	public static void main(String[] args) throws InterruptedException {
		//FlowerBed bed = new FlowerBed(200);
		
		for(int numOfFlowers = 1; numOfFlowers <= 20; numOfFlowers++)
		{
			System.out.println("\nFlowers: " + numOfFlowers);
			FlowerBed.TOTAL_FLOWERS = numOfFlowers;
			Population<FlowerBed> pop = new Population<FlowerBed>(new FlowerBed());
			BedViewer view = new BedViewer(pop);
		}

		//view.display();
		//view.save("bed.jpg");
	}

	/** Write a BufferedImage to a File. */
	private static void writeImage(BufferedImage image, String fileName)
			throws IOException {
		// check input
		if (fileName == null)
			return;

		// determine the file type or set to jpg if there is no extension
		int offset = fileName.lastIndexOf(".");
		String type = offset == -1 ? "jpg" : fileName.substring(offset + 1);

		ImageIO.write(image, type, new File(fileName));
	}
}

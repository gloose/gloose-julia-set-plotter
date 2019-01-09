import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class JuliaSet extends JPanel {
	final int FRAME_SIZE = 1500;
	final int POINT_RADIUS = 1;
	double A;
	double B;
	final double POINT_INTERVAL = 0.008;
	JFrame frame;
	
	//JFrame setup
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JuliaSet js = new JuliaSet(f);
		f.add(js);
		f.pack();
	}
	
	//JPanel setup
	public JuliaSet(JFrame f) {
		setPreferredSize(new Dimension(FRAME_SIZE, FRAME_SIZE));
		frame = f;
	}
	
	/* Weird JPanel thing
	 * Overrides a JPanel function that's called whenever you need to draw on the frame
	 */
	public void paintComponent(Graphics g) {
		//Clear everything on the frame so we can redraw it
		g.clearRect(0, 0, FRAME_SIZE, FRAME_SIZE);
		
		/* Scale A between -1 and 0 based on mouse horizontal position.
		 * Scale B between -0.5 and 0 based on mouse vertical position.
		 */
		Point p = MouseInfo.getPointerInfo().getLocation();
		A = 1.0 * (p.x - frame.getX()) / FRAME_SIZE - 1;
		B = 1.0 * (p.y - frame.getY()) / FRAME_SIZE / 2 - 0.5;
		
		/* For every point within -2 <= x <= 2, -1 <= y <= 1,
		 * plot the point if it does not approach infinity
		 */
		for(double x = -2; x <= 2; x += POINT_INTERVAL) {
			for(double y = -1; y <= 1; y += POINT_INTERVAL) {
				if(!diverges(x, y)) {
					drawPoint(g, x, y);
				}
			}
		}
		
		/* As soon as it's done drawing, it does it again
		 * This is another weird JPanel thing
		 */
		repaint();	
	}
	
	/* Returns true if the point approaches infinity when the function is applied,
	 * returns false otherwise
	 */
	public boolean diverges(double x, double y) {
		double xMax = 0;
		double yMax = 0;
		
		/* Checks if the function diverges within 50 iterations.
		 * Also keeps track of the maximum value for use with the next loop
		 */
		for(int i = 0; i < 50; i ++) {
			double[] f = func(x, y);
			x = f[0];
			y = f[1];
			
			if(Double.isInfinite(x) || Double.isInfinite(y))
				return true;
			if(Math.abs(x) > xMax)
				xMax = Math.abs(x);
			if(Math.abs(y) > yMax)
				yMax = Math.abs(y);
		}
		
		/* I don't think this part is actually necessary,
		 * but it's meant to handle cases that take longer than 50-60 cycles to diverge
		 * by checking if it's still increasing significantly after 50 iterations
		 */
		for(int i = 0; i < 10; i ++) {
			if(Double.isInfinite(x) || Double.isInfinite(y)
			|| Math.abs(x) > xMax * 1.001 || Math.abs(y) > yMax * 1.001)
				return true;
		}
		
		return false;
	}
	
	//Applies the function z^2 + C, where z = x + yi and C = A + Bi
	public double[] func(double x, double y) {
		return new double[] {x * x - y * y + A, 2 * x * y + B};
	}
	
	//Plots x and y, adjusted for visibility
	public void drawPoint(Graphics g, double xActual, double yActual) {
		int x = (int) (xActual * FRAME_SIZE / 4) + FRAME_SIZE / 2;
		int y = (int) (yActual * FRAME_SIZE / 4) * -1 + FRAME_SIZE / 2;
		g.fillOval(x - POINT_RADIUS, y - POINT_RADIUS, 2 * POINT_RADIUS, 2 * POINT_RADIUS);
	}
}
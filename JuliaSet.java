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
		
		/* Scale A between -1.5 and 0.5 based on mouse horizontal position.
		 * Scale B between -1 and 1 based on mouse vertical position.
		 */
		Point p = MouseInfo.getPointerInfo().getLocation();
		A = 1.0 * (p.x - frame.getX()) * 2 / FRAME_SIZE - 1.5;
		B = 1.0 * (p.y - frame.getY()) * 2 / FRAME_SIZE - 1;
		
		/* For every point within -2 <= x <= 2, -2 <= y <= 2,
		 * plot the point if it does not approach infinity
		 */
		for(double x = -2; x <= 2; x += POINT_INTERVAL) {
			for(double y = -2; y <= 2; y += POINT_INTERVAL) {
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
		// Checks if the function diverges within 50 iterations.
		for(int i = 0; i < 50; i ++) {
			double[] f = func(x, y);
			x = f[0];
			y = f[1];
			
			if(Double.isInfinite(x) || Double.isInfinite(y))
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
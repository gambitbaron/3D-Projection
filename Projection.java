import java.awt.*;

import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.KeyAdapter;

public class Projection extends Component implements KeyListener, MouseListener{
	/**
	 * System for projecting a 3D cube to a projection screen using Java Swing GUI,
	 * and accepting "player" input for movement within 3D space
	 */
	int pixelToFootRatio;			// scaling factor for projection
	double distance;				// distance of "player" from projection screen in space
	double x;						// projection screen width
	double y;						// projection screen height
	Vector user;					// coordinates of the "player" in space
	Vector direction;				// camera angle vector
	Vector camera;					// camera location vector
	ArrayList<Vector> corners;		// cube vertex coordinates in space
	ArrayList<Vector> cornersXY;	// cube vertex coordinates in projection screen
	
	public Projection(double userDist, int pixels, double screenXFoot, double screenYFoot){
		/**
		 * @param userDist distance of "player" from projection screen in space
		 * @param pixels scaling factor for projection
		 * @param screenXFoot projection screen width
		 * @param screenYFoot projection screen height
		 */
		addKeyListener(this);
		addMouseListener(this);
		x = screenXFoot;
		y = screenYFoot;
		pixelToFootRatio = pixels;						
		distance = userDist;
		user = new Vector();
		direction = new Vector(Math.PI/2, 0, 0);
		camera = new Vector();
		setCamera();
		corners = new ArrayList<Vector>();
		cornersXY = new ArrayList<Vector>();
		corners.add(0, new Vector(-2.5, -2.5, 10));
		corners.add(1, new Vector(-2.5, 2.5, 10));
		corners.add(2, new Vector(2.5, 2.5, 10));
		corners.add(3, new Vector(2.5, -2.5, 10));
		corners.add(4, new Vector(-2.5, -2.5, 15));
		corners.add(5, new Vector(-2.5, 2.5, 15));
		corners.add(6, new Vector(2.5, 2.5, 15));
		corners.add(7, new Vector(2.5, -2.5, 15));
		cornersXY.add(0, new Vector(-2.5, -2.5, 10));
		cornersXY.add(1, new Vector(-2.5, 2.5, 10));
		cornersXY.add(2, new Vector(2.5, 2.5, 10));
		cornersXY.add(3, new Vector(2.5, -2.5, 10));
		cornersXY.add(4, new Vector(-2.5, -2.5, 15));
		cornersXY.add(5, new Vector(-2.5, 2.5, 15));
		cornersXY.add(6, new Vector(2.5, 2.5, 15));
		cornersXY.add(7, new Vector(2.5, -2.5, 15));
		
		repaint();
	}
	
	public void paint(Graphics g){
		/**
		 * Updates projection screen
		 * Note: This function needn't be called specifically;
		 * it's automatically called by "repaint()", which should be used instead.
		 * @param g Object for implementing graphical components to be displayed
		 */
		
		Vector xExamp = new Vector();
		VectorMath.setVector(xExamp, new Vector(direction.x - Math.PI/2, 0, 0), distance);
		Vector yExamp = new Vector();
		VectorMath.setVector(yExamp, new Vector(Math.PI - direction.x, Math.PI/2 - direction.y, 0), distance);
		
		// Find where each cube vertex will be represented in the projection screen:
		for(int i=0; i<=7; i++){
			Vector init = VectorMath.subtract(corners.get(i), user);
			Vector projX = VectorMath.projAOntoB(init, xExamp);
			Vector projY = VectorMath.projAOntoB(init, yExamp);
			Vector projZ = VectorMath.projAOntoB(init, camera);
			Vector xPlusY = VectorMath.add(projX, projY);
			Vector xY = new Vector(Math.cos(VectorMath.angle(xPlusY, xExamp)), Math.cos(VectorMath.angle(xPlusY, yExamp)), 0);
			cornersXY.set(i, VectorMath.scale(xY, pixelToFootRatio*distance*VectorMath.subtract(projZ, init).magnitude()/projZ.magnitude()));
		}
		
		//Draw each vertex of the cube, to/from each vertex as represented in the projection screen:
		for(int i=0; i<=2; i++){
			Vector a = cornersXY.get(i);
			Vector b = cornersXY.get(i+1);
			g.drawLine((int)(a.x+pixelToFootRatio*x/2), (int)(a.y+pixelToFootRatio*y/2), (int)(b.x+pixelToFootRatio*x/2), (int)(b.y+pixelToFootRatio*y/2));
		}
		g.drawLine((int)(cornersXY.get(3).x+pixelToFootRatio*x/2), (int)(cornersXY.get(3).y+pixelToFootRatio*y/2), (int)(cornersXY.get(0).x+pixelToFootRatio*x/2), (int)(cornersXY.get(0).y+pixelToFootRatio*y/2));
		for(int i=4; i<=6; i++){
			Vector a = cornersXY.get(i);
			Vector b = cornersXY.get(i+1);
			g.drawLine((int)(a.x+pixelToFootRatio*x/2), (int)(a.y+pixelToFootRatio*y/2), (int)(b.x+pixelToFootRatio*x/2), (int)(b.y+pixelToFootRatio*y/2));
		}
		g.drawLine((int)(cornersXY.get(7).x+pixelToFootRatio*x/2), (int)(cornersXY.get(7).y+pixelToFootRatio*y/2), (int)(cornersXY.get(4).x+pixelToFootRatio*x/2), (int)(cornersXY.get(4).y+pixelToFootRatio*y/2));
		for(int i=0; i<=3; i++){
			Vector a = cornersXY.get(i);
			Vector b = cornersXY.get(i+4);
			g.drawLine((int)(a.x+pixelToFootRatio*x/2), (int)(a.y+pixelToFootRatio*y/2), (int)(b.x+pixelToFootRatio*x/2), (int)(b.y+pixelToFootRatio*y/2));
		}
	}
	public void setCamera(){
		/**
		 * Updates camera location
		 */
		VectorMath.setVector(camera, direction, distance);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	
	public void keyTyped(KeyEvent e){
		/**
		 * Moves player within space upon key input
		 * @param e Info regarding the typed key
		 */
		Vector close = corners.get(0);	// min cube vertex coordinates
		Vector far = corners.get(6);	// max cube vertex coorinates
		
		if(e.getKeyChar() == 115){
			// S
			// Move player left
			double a = user.x-.1*Math.sin(direction.x);
			double b = user.z+.1*Math.cos(direction.x);
			if(!(a>=close.x-1 && a<=far.x+1 && b>=close.z-1 && b<=far.z+1)){	// forbidding movement inside the cube
				user.x = a;
				user.z = b;
			}
		} else if(e.getKeyChar() == 102){
			// F
			// Move player right
			double a = user.x+.1*Math.sin(direction.x);
			double b = user.z-.1*Math.cos(direction.x);
			if(!(a>=close.x-1 && a<=far.x+1 && b>=close.z-1 && b<=far.z+1)){
				user.x = a;
				user.z = b;
			}
		} else if(e.getKeyChar() == 100){
			// D
			// Move player backward
			double a = user.x-.1*Math.cos(direction.x);
			double b = user.z-.1*Math.sin(direction.x);
			if(!(a>=close.x-1 && a<=far.x+1 && b>=close.z-1 && b<=far.z+1)){
				user.x = a;
				user.z = b;
			}
		} else if(e.getKeyChar() == 101){
			// E
			// Move player forward
			double a = user.x+.1*Math.cos(direction.x);
			double b = user.z+.1*Math.sin(direction.x);
			if(!(a>=close.x-1 && a<=far.x+1 && b>=close.z-1 && b<=far.z+1)){
				user.x = a;
				user.z = b;
			}
		}
		
		else if(e.getKeyChar() == 106){
			// J
			// Turn player left
			double a = direction.x+Math.PI/144;
			direction.x = a;
		} else if(e.getKeyChar() == 108){
			// L
			// Turn player right
			double a = direction.x-Math.PI/144;
			direction.x = a;
		}
		setCamera();	// update player camera
		repaint();		// update the projection screen
	}
	
	public void mouseClicked(MouseEvent e){
		/**
		 * Sets focus to Projection window, allowing key commands 
		 */
		requestFocus();
	}
	
	// The following must be implemented as a Key- and MouseListener Interface:
	public void keyReleased(KeyEvent e){}
	public void keyPressed(KeyEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	
}

//import layout.TableLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class drive3DProjection {
	private static int PIXELS = 700;
	private static double x = 2;		//horizontal window scaling factor
	private static double y = x/1.618;	//vertical window scaling factor
	
	public static void main(String[] args) {
		Frame frame = new Frame();
		frame.setBounds(10, 10, (int)(PIXELS*x), (int)(PIXELS*y));
		frame.add(new Projection(1, PIXELS, x, y));
		
		frame.addWindowListener (new WindowAdapter(){
			public void windowClosing (WindowEvent e){
				System.exit (0);
            }
        });
		frame.show();
	}

}

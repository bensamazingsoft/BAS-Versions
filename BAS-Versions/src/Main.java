import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.bas.versions.gui.MainPnl;

public class Main {


	public static void main(String[] args) {


		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				makeFrame();
			}
		});

	}

	
	public static void makeFrame(){
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	
		JFrame frame = new JFrame();
		frame.setBounds(0,0,900,600);
		MainPnl mainpnl = new MainPnl();
		frame.getContentPane().add(mainpnl);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
		
		
	}
}

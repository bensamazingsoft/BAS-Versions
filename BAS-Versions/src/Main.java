import java.io.File;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.bas.versions.gui.MainPnl;
import com.bas.versions.utils.CheckPoint;
import com.bas.versions.xml.CheckPointParser;

public class Main {


	public static void main(String[] args) {


		CheckPoint cp = new CheckPoint(new CheckPointParser(new File("E:\\projet\\BAS-CheckPoints\\Checkpoint0001[2017-08-13_19-04]\\frefre.xml")).getDoc());
		
		System.out.println(cp.toString());
		System.out.println(cp.getChckPtFileList().toString());
		
		


//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				makeFrame();
//			}
//		});
//
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

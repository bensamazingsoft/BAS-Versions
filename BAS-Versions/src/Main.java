import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.bas.versions.gui.MainPnl;

public class Main {

	public static void main(String[] args) {

//		Path path = Paths.get("e:\\projet");
//		Project pro = new Project(path);
//		pro.commitCheckPoint();
//		pro.commitCheckPoint();
//		pro.commitCheckPoint();
//		
//		
//		Project pr = new Project(new ProjectParser(new File("e:\\projet\\BAS-CheckPoints\\projet.basv")).getDoc());
//		
//		System.out.println(pr.getCheckPointStack().toString());
		
		
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				makeFrame();
			}
		});
	}

	public static void makeFrame() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame();
		frame.setBounds(0, 0, 900, 600);
		MainPnl mainpnl = new MainPnl();
		frame.getContentPane().add(mainpnl);
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);

	}
}

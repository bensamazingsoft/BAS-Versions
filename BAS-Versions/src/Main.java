import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.bas.versions.gui.MainFrame;

public class Main {

	public static void main(String[] args) {

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

		MainFrame frame = new MainFrame();
		frame.setTitle("Ben's Amazing Versions - the Tool you needed your whole life");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);

	}
}

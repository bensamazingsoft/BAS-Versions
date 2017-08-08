import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.bas.versions.gui.MainPnl;

public class Main {


	public static void main(String[] args) {
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
		
		frame.setVisible(true);

//		Project project = new Project(Paths.get("E:\\projet"));
//		System.out.println(project.getListFile().toString());
//		System.out.println(project.getListFilteredFile());
		
//		project.commitCheckPoint();

	}

}

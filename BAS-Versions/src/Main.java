import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;

import com.bas.versions.gui.BasJTree;
import com.bas.versions.utils.FileList;

public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Path path = Paths.get("E:\\projet\\");
		Set<File> files = new FileList(new FileList(path).getResult(),".aep,.prpj,.psd,.ai,.txt,.srt","BAS-Checkpoints,patate",path).getResult();
		

		JFrame frame = new JFrame();
		frame.setTitle("test tree");
		frame.setBounds(0,0,300,300);
		
		BasJTree tree = new BasJTree(path,files);
		frame.getContentPane().add(tree);
		frame.setVisible(true);
	
		



	}

}

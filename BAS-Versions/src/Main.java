import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;

import com.bas.versions.utils.Version;

public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		File file = new File("E:\\projet\\sousdossier\\frites.srt");
		Path projectPath = Paths.get("E:\\projet\\");
		Path versionPath = Paths.get("E:\\projet\\version\\");
		HashSet<File> projectFiles = new HashSet<>();
		File frites = new File("E:\\projet\\sousdossier\\frites.srt");
		File a = new File("E:\\projet\\sousdossier\\01.txt");
		File b = new File("E:\\projet\\sousdossier\\02.txt");
		projectFiles.add(frites);
		projectFiles.add(a);
		projectFiles.add(b);
		Date date =  new Date();
		String msg = "nouvelle version du jour c'est super cool et puis c'est tout";
		Version version = new Version(date, projectPath, projectFiles, msg);
		

		
		try {
			version.writeFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}

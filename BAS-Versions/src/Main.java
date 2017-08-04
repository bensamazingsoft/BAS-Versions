import java.nio.file.Path;
import java.nio.file.Paths;

import com.bas.versions.utils.Project;

public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		Path projectPath = Paths.get("E:\\projet\\");



		Project proj = new Project(projectPath);
		proj.commitVersion();

	
		



	}

}

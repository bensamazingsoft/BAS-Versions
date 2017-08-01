package com.bas.versions.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileManager {

	
	
	/**
	 * returns file with the Version path
	 * @param file
	 * @param projectPath
	 * @param versionPath
	 * @return newFile
	 */
	public static File changeFilePath(File file, Path projectPath, Path versionPath){
		

		String filePth = file.getAbsolutePath();
		String projectPth = projectPath.toFile().getAbsolutePath();
		String versionPth = versionPath.toFile().getAbsolutePath() + "\\";
		File newFile = new File(filePth.replace(projectPth, versionPth));
	
		return newFile;
	}
	
	/**
	 * write files in the Version folder
	 * @param file
	 * @param fileDest
	 * @param versionPath
	 * @return true if succeed
	 * @throws IOException
	 */
	public static boolean copyFile2VersionPath(File file, File fileDest, Path versionPath) throws IOException{
		
		boolean success;
		Path filePth = file.toPath();
		Path fileDestPt = fileDest.toPath();
		fileDest.mkdirs();

		
		Files.copy(filePth, fileDestPt, StandardCopyOption.REPLACE_EXISTING);
		
		success = fileDest.exists();
		
		return success;
		
		
	}
	
	/**
	 * writes a .txt file on disk
	 * @param msg
	 * @param title
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static boolean writeTxtFile(String msg, String title, Path path) throws IOException{
		String separator = System.getProperty("line.separator");
		String strOut = msg.replace("\\n", separator);
		File file = new File(path.toFile().getAbsolutePath() + "\\" + title + ".txt");
		FileWriter fw = new FileWriter(file, true); 
		fw.append(strOut);
		fw.close();
		
		Boolean success;
		if (file.exists()) {
			success = true;
		}else{
			 success = false;
		}
		
		return success;
	}
	
	public FileManager() {

	}
	
	

}

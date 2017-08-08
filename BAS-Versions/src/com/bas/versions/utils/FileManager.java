package com.bas.versions.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class FileManager {

	/**
	 * returns file with the CheckPoint path
	 * 
	 * @param file
	 * @param originePath
	 * @param DestinationPath
	 * @return File
	 */
	public static File changeFilePath(File file, Path originePath, Path DestinationPath) {

		String filePth = file.getAbsolutePath();
		String projectPth = originePath.toFile().getAbsolutePath();
		String chkptPth = DestinationPath.toFile().getAbsolutePath() + "\\";
		File newFile = new File(filePth.replace(projectPth, chkptPth));

		return newFile;
	}

	/**
	 * write files in the CheckPoint folder
	 * 
	 * @param file
	 * @param fileDest
	 * @param versionPath
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean copyFile2CheckPointPath(File file, File fileDest) throws IOException {

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
	 * 
	 * @param msg
	 * @param title
	 * @param path
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean writeTxtFile(String msg, String title, Path path) throws IOException {
		String separator = System.getProperty("line.separator");
		String strOut = msg.replace("\\n", separator);
		File file = new File(path.toFile().getAbsolutePath() + "\\" + title + ".txt");
		FileWriter fw = new FileWriter(file, true);
		fw.append(strOut);
		fw.close();

		Boolean success;
		if (file.exists()) {
			success = true;
		} else {
			success = false;
		}

		return success;
	}

	public static void saveFile(Object obj, File file) {
		
		ObjectOutputStream oos;
		
		
		
		try {
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			oos.writeObject(obj);
			oos.close();
		} catch (FileNotFoundException e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(null, "File not found", "Error", JOptionPane.ERROR_MESSAGE);
				}
			});
		} catch (IOException e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(null, "Can't write file", "Error", JOptionPane.ERROR_MESSAGE);
					
				}
			});
			e.printStackTrace();
		}
		
		if (file.exists()){
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(null, "Project saved", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			});
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(null, "Project not saved", "Error", JOptionPane.ERROR_MESSAGE);
				}
			});
		}
	}
	
	/**
	 * @param file
	 * @param dirPath
	 * @return
	 */
	public static String getRelativeFileName(File file, Path dirPath){
		
		String str;		
		str = file.getAbsolutePath().replace(dirPath.toFile().getAbsolutePath(), "");
		
		return str;
	}
	
	
	public FileManager() {

	}

}

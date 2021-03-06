package com.bas.versions.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.bas.versions.gui.BasPgBarUpdtePair;
import com.bas.versions.gui.BasProgressBar;

public class FileList extends Observable {

	private Set<File> result = new HashSet<>();
	private BasProgressBar pgBar;
	private BasPgBarUpdtePair pair;

	/**
	 * lists all files in Path
	 * 
	 * @param projectPath
	 */
	public FileList(Path projectPath) {

		result = new HashSet<>();

		try {
			Files.walkFileTree(projectPath, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {

					result.add(file.toFile());

					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException e) {

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JOptionPane.showMessageDialog(null, "Error", "Can't read file", JOptionPane.ERROR_MESSAGE);
						}
					});
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(null, "Error", "Can't read file", JOptionPane.ERROR_MESSAGE);
				}
			});
		}
	}

	/**
	 * filter and lists files in a Set<File> using Strings to accept/reject file
	 * paths from @param projectPath
	 * 
	 * @param setFile
	 * @param filterIn
	 * @param filterOut
	 * @param projectPath
	 */
	public FileList(final Set<File> setFile, String filterIn, String filterOut, Path projectPath) {

		String regexIn;
		if (filterIn.equals("*") || filterIn.equals("*,")) {
			regexIn = (".");
		} else {
			String[] inTab = filterIn.split(",");
			regexIn = "";
			for (String str : inTab) {
				regexIn += "(" + str + ")" + "|";
			}
			regexIn = regexIn.substring(0, regexIn.lastIndexOf("|"));
		}
		Pattern pattIn = Pattern.compile(regexIn, Pattern.CASE_INSENSITIVE);

		String[] outTab = filterOut.split(",");
		String regexOut = "";
		for (String str : outTab) {
			regexOut += "(" + str + ")" + "|";
		}
		regexOut = regexOut.substring(0, regexOut.lastIndexOf("|"));
		Pattern pattOut = Pattern.compile(regexOut, Pattern.CASE_INSENSITIVE);

		String pathName = projectPath.toFile().getAbsolutePath();

		pgBar = new BasProgressBar("Filtering Files ", setFile.size());
		addObserver(pgBar);
		pgBar.setVisible(true);

		int i = 0;
		for (File file : setFile) {
			String name = file.getAbsolutePath().replace(pathName, "");
			Matcher matchIn = pattIn.matcher(name);
			Matcher matchOut = pattOut.matcher(name);

			if (matchIn.find() && !(matchOut.find() && filterOut.length() != 0)) {
				result.add(file);

			}

			pair = new BasPgBarUpdtePair("Filtering Files ", ++i);
			setChanged();
			notifyObservers(pair);
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pgBar.dispose();
			}
		});
	}

	/**
	 * add more recent (than @param dateCreated) files to result Set
	 * 
	 * @param listFilteredFile
	 * @param dateCreated
	 */
	public FileList(Set<File> listFilteredFile, Date dateCreated) {
		for (File file : listFilteredFile) {
			Date fileDate = new Date(file.lastModified());
			if (fileDate.compareTo(dateCreated) > 0) {

				result.add(file);
			}
		}
	}

	public Set<File> getResult() {
		return result;
	}

}

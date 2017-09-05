package com.bas.versions.utils;

import static com.bas.versions.utils.LogGenerator.log;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.bas.versions.gui.BasPgBarUpdtePair;
import com.bas.versions.gui.BasProgressBar;

public class ProjectRestorer extends Observable {

	private Project project;
	private Path restorePath;
	private Deque<CheckPoint> cpList = new LinkedList<>();
	private Map<String, File> cpFiles2Restore = new HashMap<>();
	private Set<File> archiveFile = new HashSet<>();
	private Map<File, File> files2Copy = new HashMap<>();
	private BasProgressBar bar;
	private int nbFiles;
	private boolean archive = false;

	public ProjectRestorer(Project project, Path newPath, int level) {

		// initiate fields for archiving process
		this.project = project;
		cpList = project.getCheckPointStack();
		restorePath = newPath;
		archive = !(restorePath.equals(project.getProjectPath()));

		// remove unwanted CheckPoints
		cpList.removeIf(cp -> (cp.getId() > level));

		// collect all files, in reversed order, to have the latest version of
		// each file put first in the Map, rejecting files already there.
		cpList.stream().sorted(Collections.reverseOrder()).forEach(cp -> cp.getChckPtFileList().stream().forEach(f -> {
			if (!cpFiles2Restore.containsKey(f.getName())) {
				cpFiles2Restore.put(f.getName(), f);
			}
		}));

		// getting project files that were filtered out (not in any Checkpoint)
		if (archive) {
			archiveFile = new FileList(project.getListFile(), "*", "BAS-CheckPoints,", project.getProjectPath())
					.getResult().stream().filter(file -> !cpFiles2Restore.containsKey(file.getName()))
					.collect(Collectors.toSet());
		}

		// for the progrees bar
		nbFiles = cpFiles2Restore.size() + archiveFile.size();

		// filling the map of files to process
		cpFiles2Restore.values().stream().forEach(file -> files2Copy.put(file, reverseFilename(file, true)));
		archiveFile.stream().forEach(file -> files2Copy.put(file, reverseFilename(file, false)));

	}

	/**
	 * restore the files from checkpoints to the project folders. counts the
	 * missing files along the way and signals user
	 * 
	 */
	public void restoreFiles() {

		System.err.println(SwingUtilities.isEventDispatchThread());

		boolean live = NightsWatcher.isPause();
		NightsWatcher.setPause(true);

		log("Start restore process ");

		bar = new BasProgressBar("restoring files", nbFiles);
		addObserver(bar);

		Set<File> missing = new HashSet<>();

		Iterator<File> it = files2Copy.keySet().iterator();
		int count = 0;

		if (!it.hasNext()) {
			JOptionPane.showMessageDialog(null, "No file to restore", "error", ERROR_MESSAGE);
		} else {
			bar.setVisible(true);
			while (it.hasNext()) {
				File file = it.next();
				try {
					files2Copy.get(file).mkdirs();
					Files.copy(file.toPath(), files2Copy.get(file).toPath(), StandardCopyOption.REPLACE_EXISTING);
					setChanged();
					notifyObservers(new BasPgBarUpdtePair("copying file : " + file.getName(), count++));
					log("\trestored file : " + file.getName());
				} catch (NoSuchFileException n) {
					missing.add(file);
				} catch (IOException e) {
					showMessageDialog(null, e.getMessage(), "error copying file", ERROR_MESSAGE);
					e.printStackTrace();
					log("Error in restore process : IOException" + "\n\t" + e.getStackTrace());
				}
			}
			bar.dispose();
		}
		showMessageDialog(null, count + " file copied", "info", INFORMATION_MESSAGE);
		if (!missing.isEmpty()) {
			String mess = "";
			missing.stream().forEach(f -> {
				mess.concat("\n\t" + f.getName());
			});
			showMessageDialog(null, (missing.size() + " file missing " + mess), "info", INFORMATION_MESSAGE);
		}
		NightsWatcher.setPause(live);
		project.reScan();
	}

	/**
	 * changes one specific filename back from checkpoint to project or user
	 * defined path
	 * 
	 * @param original
	 *            file from checkpoint
	 * @return new file to copy back to the project
	 */
	private File reverseFilename(File in, boolean cp) {

		// ex. c:/projectPath/workPath/Checkpoint0001/file.txt
		String baseFileName = in.getAbsolutePath();
		// ex. c:/projectPath/workPath/Checkpoint0001/file.txt ->
		// /Checkpoint0001/file.txt
		String outFileName = baseFileName.replace(project.getWorkPath().toFile().getAbsolutePath(), "");
		if (cp) {
			// ex. /Checkpoint0001/file.txt -> file.txt
			Path temp = new File(outFileName).toPath();
			outFileName = outFileName.replace("\\" + temp.getName(0) + "\\", "");
		}
		if (!cp) {
			// ex. c:/projectPath/file.txt -> file.txt
			outFileName = outFileName.replace(project.getProjectPath().toFile().getAbsolutePath(), "");
		}
		File out = new File(restorePath.toFile().getAbsolutePath() + "\\" + outFileName);

		return out;

	}
}

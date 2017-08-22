package com.bas.versions.utils;

import static com.bas.versions.utils.LogGenerator.log;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import javax.swing.JOptionPane;

public class NightsWatcher {

	Path path2Watch;
	WatchService watcher;
	WatchKey key;
	Project project;

	static boolean pause = false;

	public NightsWatcher(Project theProject) {

		log("Started watch service");

		this.project = theProject;
		this.path2Watch = this.project.getProjectPath();

		try {
			watcher = FileSystems.getDefault().newWatchService();

			registerPaths();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error in the NightsWatch", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		for (;;) {
			if (!pause) {
				try {
					key = watcher.take();
				} catch (InterruptedException e) {
					return;
				}

				for (WatchEvent<?> event : key.pollEvents()) {

					WatchEvent.Kind<?> kind = event.kind();

					if (kind == StandardWatchEventKinds.OVERFLOW) {
						continue;
					}

					@SuppressWarnings("unchecked")
					WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;

					System.err.println("Event : " + kind + " in key : " + key.watchable().toString());

					// (Path)key.watchable()).resolve(pathEvent.context())
					// below is to get the actual path that triggered the
					// watcher, the API returns a relative path that need to
					// be resolved against the actual path that was
					// registered.
					Path trigPath = ((Path) key.watchable()).resolve(pathEvent.context());
					if (trigPath.toFile().isDirectory() && !(trigPath.startsWith(this.project.getWorkPath()))) {
						try {
							trigPath.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
									StandardWatchEventKinds.ENTRY_MODIFY);
							log("Watch Service : registered folder : " + pathEvent.context().toString());
						} catch (IOException e) {
							JOptionPane.showMessageDialog(null, e.getMessage(),
									"Error in the NightsWatch, folder registering failed", JOptionPane.ERROR_MESSAGE);
							log("Watch Service : Error in the NightsWatch, folder registering failed" + "\n\t"
									+ e.getStackTrace());
							e.printStackTrace();
						}
						this.project.reScan();
					}
					if (trigPath.toFile().isFile()) {
						this.project.reScan();
					}
					boolean valid = key.reset();
					if (!valid) {
						break;
					}
				}
			}
		}
	}

	/**
	 * Recursively registers all folders to the NightsWatch
	 * 
	 * @throws IOException
	 */
	private void registerPaths() throws IOException {

		log("Watch Service : registering paths");

		Files.walkFileTree(path2Watch, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult postVisitDirectory(Path file, IOException exc) throws IOException {
				if (!file.startsWith(NightsWatcher.this.project.getWorkPath())) {
					@SuppressWarnings("unused")
					WatchKey wk = file.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
							StandardWatchEventKinds.ENTRY_MODIFY);
					log("\t registered : " + file.toFile().getAbsolutePath());
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error in the NightsWatch, walking the tree failed",
						JOptionPane.ERROR_MESSAGE);
				log("Watch Service : Error in the NightsWatch, walking the tree failed" + "\n\t" + e.getStackTrace());
				e.printStackTrace();
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * @return the pause
	 */
	public static boolean isPause() {
		return pause;
	}

	/**
	 * @param pause
	 *            the pause to set
	 */
	public static void setPause(boolean pause) {
		NightsWatcher.pause = pause;
	}

}

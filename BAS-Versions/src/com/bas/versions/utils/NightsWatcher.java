package com.bas.versions.utils;

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

	public NightsWatcher(Project theProject) {

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

				if ((kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY)
						&& key.watchable() != this.project.getWorkPath()) {

					@SuppressWarnings("unchecked")
					WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
					if (!pathEvent.context().startsWith(NightsWatcher.this.project.getWorkPath())) {
						this.project.reScan();
					}
				}
				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}
		}
	}

	/**
	 * recusively registers all folders to the NightsWatch
	 * 
	 * @throws IOException
	 */
	private void registerPaths() throws IOException {

		System.err.println("registering paths");

		Files.walkFileTree(path2Watch, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult postVisitDirectory(Path file, IOException exc) throws IOException {
				if (!file.startsWith(NightsWatcher.this.project.getWorkPath())) {
					@SuppressWarnings("unused")
					WatchKey wk = file.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
							StandardWatchEventKinds.ENTRY_MODIFY);
					System.err.println("\t registered : " + file.toFile().getAbsolutePath());
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error in the NightsWatch, walking the tree failed",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				return FileVisitResult.CONTINUE;
			}
		});
	}

}
